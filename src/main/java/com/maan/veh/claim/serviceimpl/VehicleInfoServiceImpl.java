package com.maan.veh.claim.serviceimpl;



import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.veh.claim.dto.FilterGarageReq;
import com.maan.veh.claim.dto.GarageClaimListDataDto;
import com.maan.veh.claim.dto.GetAllClaimNoResponse;
import com.maan.veh.claim.entity.DamageSectionDetails;
import com.maan.veh.claim.entity.GarageWorkOrder;
import com.maan.veh.claim.entity.InsuredVehicleInfo;
import com.maan.veh.claim.entity.VcFlowMaster;
import com.maan.veh.claim.repository.DamageSectionDetailsRepository;
import com.maan.veh.claim.repository.GarageWorkOrderRepository;
import com.maan.veh.claim.repository.InsuredVehicleInfoRepository;
import com.maan.veh.claim.repository.VcFlowMasterRepository;
import com.maan.veh.claim.request.ExternalVehicleGarageViewRequest;
import com.maan.veh.claim.request.VehicleGarageViewRequest;
import com.maan.veh.claim.request.VehicleInfoRequest;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.SurveyorViewResponse;
import com.maan.veh.claim.response.VehicleInfoResponse;
import com.maan.veh.claim.service.VehicleInfoService;



@Service
public class VehicleInfoServiceImpl implements VehicleInfoService {
	
	@Autowired
	private DamageSectionDetailsRepository damageRepository;

    @Autowired
    private InsuredVehicleInfoRepository insuredVehicleInfoRepository;
    
    @Autowired
    private GarageWorkOrderRepository garageWorkOrderRepository;
    
    @Autowired
    private ExternalApiServiceImpl ExternalApiServiceImpl;
    
	@Autowired
    private VcFlowMasterRepository flowMasterRepo;

    @Override
    public CommonResponse getVehicleInfoByCompanyId(VehicleGarageViewRequest request) {
        CommonResponse response = new CommonResponse();    
        try {
            // Fetch the list of vehicle info based on company ID and Garage ID, ordered by EntryDate Desc
            List<InsuredVehicleInfo> vehicleInfoList = insuredVehicleInfoRepository
                    .findByCompanyIdAndGarageIdOrderByEntryDateDesc(
                            Integer.valueOf(request.getCompanyId()), request.getGarageId());

            if (vehicleInfoList.isEmpty()) {
                response.setErrors(Collections.emptyList());
                response.setMessage("Failed");
                response.setIsError(true);
                response.setResponse(Collections.emptyList());
                return response;
            }

            // Fetch all work orders in smaller batches to avoid ORA-01795
            Set<String> claimNos = vehicleInfoList.stream()
                    .map(InsuredVehicleInfo::getClaimNo)
                    .collect(Collectors.toSet());
            
            vehicleInfoList.sort(Comparator.comparing(InsuredVehicleInfo::getEntryDate).reversed());

            List<String> claimNoList = vehicleInfoList.stream()
                    .map(InsuredVehicleInfo::getClaimNo)
                    .distinct()
                    .collect(Collectors.toList());


            Map<String, String> workOrderMap = new HashMap<>();
//            List<String> claimNoList = new ArrayList<>(claimNos);
            int batchSize = 500; // Oracle limit
            for (int i = 0; i < claimNoList.size(); i += batchSize) {
                List<String> batch = claimNoList.subList(i, Math.min(i + batchSize, claimNoList.size()));
                garageWorkOrderRepository.findByClaimNoInAndGarageId(new HashSet<>(batch), request.getGarageId())
                        .forEach(workOrder -> workOrderMap.putIfAbsent(workOrder.getClaimNo(), workOrder.getQuotationNo()));
            }

            // Convert each entity to a response object using streams
            List<VehicleInfoResponse> vehList = vehicleInfoList.stream()
                    .map(vehicle -> {
                        VehicleInfoResponse veh = new VehicleInfoResponse();
                        veh.setCompanyId(vehicle.getCompanyId() != null ? String.valueOf(vehicle.getCompanyId()) : null);
                        veh.setPolicyNo(vehicle.getPolicyNo());
                        veh.setClaimNo(vehicle.getClaimNo());
                        veh.setVehicleMake(vehicle.getVehicleMake());
                        veh.setVehicleModel(vehicle.getVehicleModel());
                        veh.setMakeYear(vehicle.getMakeYear() != null ? String.valueOf(vehicle.getMakeYear()) : null);
                        veh.setChassisNo(vehicle.getChassisNo());
                        veh.setInsuredName(vehicle.getInsuredName());
                        veh.setType(vehicle.getType());
                        veh.setVehicleRegNo(vehicle.getVehicleRegNo());
                        veh.setEntryDate(vehicle.getEntryDate());
                        veh.setFnolSgsId(vehicle.getFnolSgsId());
                        veh.setLossLocation(vehicle.getLossLocation());
                        veh.setQuoteStatus("Y".equalsIgnoreCase(vehicle.getStatus()) ? "PFG" : vehicle.getStatus());

                        List<VcFlowMaster> statusList = flowMasterRepo.findByCompanyIdAndSubStatus(request.getCompanyId(),"Y".equalsIgnoreCase(vehicle.getStatus()) ? "PFG" : vehicle.getStatus());
                        if(statusList!=null && statusList.size()>0) {
                        	veh.setStatus(statusList.get(0).getSubStatusDescription());
                        }
                        // Set quotation number from pre-fetched workOrderMap
                        veh.setQuotationNo(workOrderMap.getOrDefault(vehicle.getClaimNo(), ""));
                        
                        veh.setWorkOrderType(vehicle.getWorkOrderType());
                        veh.setEngineNo(vehicle.getEngineNo());
                        veh.setClaimantType(vehicle.getClaimantType());
                        veh.setLossLocationDesc(vehicle.getLossLocationDesc());
                        veh.setClaimStatus(vehicle.getClaimStatus());
                        veh.setFileNo(vehicle.getFileNo());
                        veh.setGarageAddress(vehicle.getGarageAddress());
                        veh.setPlateType(vehicle.getPlateType());
                        veh.setGarageLoginId(request.getGarageId());
                        veh.setSurveyorId(vehicle.getSurveyorId());
                        
                        return veh;
                    }).collect(Collectors.toList());

            response.setErrors(Collections.emptyList());
            response.setMessage("Success");
            response.setResponse(vehList);

        } catch (Exception e) {
            e.printStackTrace();
            response.setMessage("Error occurred");
            response.setIsError(true);
            response.setErrors(Collections.singletonList(e.getMessage()));
        }

        return response;
    }


    public CommonResponse getVehicleInfoByCompanyIdV0(VehicleGarageViewRequest request) {
    	CommonResponse response = new CommonResponse(); 	
    	List<VehicleInfoResponse> vehList = new ArrayList<>();
        
        try {
        	
        	List<InsuredVehicleInfo> vehicleInfoList = insuredVehicleInfoRepository.findByCompanyIdAndGarageIdOrderByEntryDateDesc(Integer.valueOf(request.getCompanyId()),request.getGarageId());
        	
            if(vehicleInfoList.size()>0) {
	            // Convert each entity to a response object and add to the response list
	            for (InsuredVehicleInfo vehicle : vehicleInfoList) {
	                // Create a new VehicleInfoResponse object
	                VehicleInfoResponse veh = new VehicleInfoResponse();
	                
	                // Set each property one by one
	                veh.setCompanyId(vehicle.getCompanyId() != null ? String.valueOf(vehicle.getCompanyId()) : null);
	                veh.setPolicyNo(vehicle.getPolicyNo());
	                veh.setClaimNo(vehicle.getClaimNo());
	                veh.setVehicleMake(vehicle.getVehicleMake());
	                veh.setVehicleModel(vehicle.getVehicleModel());
	                veh.setMakeYear(vehicle.getMakeYear() != null ? String.valueOf(vehicle.getMakeYear()) : null);
	                veh.setChassisNo(vehicle.getChassisNo());
	                veh.setInsuredName(vehicle.getInsuredName());
	                veh.setType(vehicle.getType());
	                veh.setVehicleRegNo(vehicle.getVehicleRegNo()); 
	                veh.setEntryDate(vehicle.getEntryDate());
	                veh.setFnolSgsId(vehicle.getFnolSgsId());
	                veh.setLossLocation(vehicle.getLossLocation());
	                if("Y".equalsIgnoreCase(vehicle.getStatus())) {
	                	//veh.setStatus("P");
	                	veh.setQuoteStatus("PFG");
	                }else {
	                	//veh.setStatus(vehicle.getStatus());
	                	veh.setQuoteStatus(vehicle.getStatus());
	                }
	                //setting quote number
	                Optional<GarageWorkOrder> workorder = garageWorkOrderRepository.findByClaimNoAndGarageId(vehicle.getClaimNo(),vehicle.getGarageId());
	                veh.setQuotationNo(workorder.map(GarageWorkOrder::getQuotationNo).orElse(""));
	                
	                // Add the populated response to the list
	                vehList.add(veh);
	            }
	            response.setErrors(Collections.emptyList());
	            response.setMessage("Success");
				response.setResponse(vehList);
			
			}else {
				response.setErrors(Collections.emptyList());
				response.setMessage("Failed");
				response.setIsError(true);
				response.setResponse(Collections.emptyList());
			}
        } catch (Exception e) {
            e.printStackTrace();
            return response;
        }

        return response;
    }

    @Override
    public CommonResponse rejectClaim(VehicleInfoRequest request) {
        CommonResponse response = new CommonResponse(); 
        List<VehicleInfoResponse> vehList = new ArrayList<>();
        
        try {
            // Fetch the vehicle info based on claim No
            Optional<InsuredVehicleInfo> optionalVehicleInfo = insuredVehicleInfoRepository.findByClaimNoAndGarageId(request.getClaimNo(),request.getGarageId());
            
            // Check if the vehicle info is present
            if (optionalVehicleInfo.isPresent()) {
                InsuredVehicleInfo insuredVehicleInfo = optionalVehicleInfo.get();
                
                // Update the status of the vehicle info to "Rejected"
                insuredVehicleInfo.setStatus(request.getQuoteStatus());
                
                // Save the updated vehicle info
                insuredVehicleInfoRepository.save(insuredVehicleInfo);
                
                // Prepare success response
                response.setErrors(Collections.emptyList());
                response.setMessage("Success");
                response.setResponse(vehList);
            } else {
                // Prepare failure response when no vehicle info is found
                response.setErrors(Collections.singletonList("Vehicle info not found"));
                response.setMessage("Failed");
                response.setIsError(true);
                response.setResponse(Collections.emptyList());
            }
        } catch (Exception e) {
            // Prepare failure response in case of an exception
            response.setErrors(Collections.singletonList(e.getMessage()));
            response.setMessage("Failed");
            response.setIsError(true);
            response.setResponse(Collections.emptyList());
            e.printStackTrace();
        }

        return response;
    }

    @Override
    public CommonResponse surveyorView(VehicleInfoRequest request) {
        CommonResponse response = new CommonResponse(); 
        List<VehicleInfoResponse> vehList = new ArrayList<>();
        
        try {
        	  List<InsuredVehicleInfo> vehicleInfoLists = new ArrayList<>();

        	  if("GPC".equalsIgnoreCase(request.getQuoteStatus()) && request.getSurveyorId() !=null) {
        		  vehicleInfoLists=insuredVehicleInfoRepository.findByStatusAndSurveyorId("GPC",request.getSurveyorId());
              
        } else if (request.getGarageId() != null && request.getClaimNo() != null && request.getVehicleRegNo() != null && request.getSurveyorId()!=null) {
                  vehicleInfoLists = insuredVehicleInfoRepository.findByGarageIdAndClaimNoAndVehicleRegNoAndSurveyorId(
                          request.getGarageId(), request.getClaimNo(), request.getVehicleRegNo(),request.getSurveyorId());

              } else if (request.getGarageId() != null && request.getClaimNo() != null && request.getSurveyorId()!=null) {
                  vehicleInfoLists = insuredVehicleInfoRepository.findByGarageIdAndClaimNoAndSurveyorId(
                          request.getGarageId(), request.getClaimNo(),request.getSurveyorId());

              } else if(request.getClaimNo()!=null && request.getSurveyorId()!=null) {
            	  vehicleInfoLists = insuredVehicleInfoRepository.findByClaimNoAndSurveyorId(request.getClaimNo(), request.getSurveyorId());
              }
              
              else {

            // Fetch the work orders based on the garage ID
            List<GarageWorkOrder> workOrders = garageWorkOrderRepository.findByGarageId(request.getGarageId());

            // Check if there are any work orders for this garage
            if (workOrders.isEmpty()) {
                response.setMessage("Failed");
                response.setIsError(true);
                return response;
            }

            // Collect claim numbers and map claim to quotation numbers in one step
            Map<String, String> claimToQuotationMap = workOrders.stream()
                .collect(Collectors.toMap(GarageWorkOrder::getClaimNo, GarageWorkOrder::getQuotationNo));
            
            Map<String, String> claimToDealerMap = workOrders.stream()
            	    .collect(Collectors.toMap(
            	        GarageWorkOrder::getClaimNo,
            	        workOrder -> workOrder.getSparepartsDealerId() != null ? workOrder.getSparepartsDealerId() : ""
            	    ));

            
            List<String> claimNumbers = new ArrayList<>(claimToQuotationMap.keySet());
            
            List<String> claimWithReplacement = new ArrayList<>();
            
            List<DamageSectionDetails> damageDetailsList = damageRepository.findByClaimNoInAndGarageLoginId(claimNumbers,request.getGarageId());
            
            claimWithReplacement = damageDetailsList.stream()
            	    //.filter(damage -> "Replace".equalsIgnoreCase(damage.getRepairReplace())) // Filter condition
            	    .map(DamageSectionDetails::getClaimNo) // Map to claimNo
            	    .distinct() // Ensure distinct claimNo values
            	    .collect(Collectors.toList()); // Collect the results into a list
            
            // Fetch the list of vehicle info based on claim numbers and status
            List<InsuredVehicleInfo> vehicleInfoList = insuredVehicleInfoRepository.findByClaimNoInAndGarageIdAndSurveyorId(claimWithReplacement,request.getGarageId(),request.getSurveyorId());
            
            // Check if any vehicles were found for the provided claim numbers and status
            if (!vehicleInfoList.isEmpty()) {
                for (InsuredVehicleInfo vehicle : vehicleInfoList) {
                    // Create a new VehicleInfoResponse object and populate it
                    VehicleInfoResponse veh = new VehicleInfoResponse();
                    
                    veh.setCompanyId(vehicle.getCompanyId() != null ? String.valueOf(vehicle.getCompanyId()) : null);
                    veh.setPolicyNo(vehicle.getPolicyNo());
                    veh.setClaimNo(vehicle.getClaimNo());
                    veh.setVehicleMake(vehicle.getVehicleMake());
                    veh.setVehicleModel(vehicle.getVehicleModel());
                    veh.setMakeYear(vehicle.getMakeYear() != null ? String.valueOf(vehicle.getMakeYear()) : null);
                    veh.setChassisNo(vehicle.getChassisNo());
                    veh.setInsuredName(vehicle.getInsuredName());
                    veh.setType(vehicle.getType());
                    veh.setVehicleRegNo(vehicle.getVehicleRegNo()); 
                    veh.setEntryDate(vehicle.getEntryDate());
                    //veh.setStatus(vehicle.getStatus());
                    veh.setQuoteStatus(vehicle.getStatus());
                    veh.setQuotationNo(claimToQuotationMap.get(vehicle.getClaimNo()));
                    
                    veh.setDealerLogin(claimToDealerMap.get(vehicle.getClaimNo()));
                    veh.setGarageLoginId(vehicle.getGarageId());
                    // Add the populated response to the list
                    vehList.add(veh);
                }
                response.setErrors(Collections.emptyList());
                response.setMessage("Success");
                response.setResponse(vehList);
                return response;
            }
              }
            if (!vehicleInfoLists.isEmpty()) {
                for (InsuredVehicleInfo vehicle : vehicleInfoLists) {
                    VehicleInfoResponse veh = new VehicleInfoResponse();
                    veh.setCompanyId(vehicle.getCompanyId() != null ? String.valueOf(vehicle.getCompanyId()) : null);
                    veh.setPolicyNo(vehicle.getPolicyNo());
                    veh.setClaimNo(vehicle.getClaimNo());
                    veh.setVehicleMake(vehicle.getVehicleMake());
                    veh.setVehicleModel(vehicle.getVehicleModel());
                    veh.setMakeYear(vehicle.getMakeYear() != null ? String.valueOf(vehicle.getMakeYear()) : null);
                    veh.setChassisNo(vehicle.getChassisNo());
                    veh.setInsuredName(vehicle.getInsuredName());
                    veh.setType(vehicle.getType());
                    veh.setVehicleRegNo(vehicle.getVehicleRegNo());
                    veh.setEntryDate(vehicle.getEntryDate());
                    veh.setQuoteStatus(vehicle.getStatus());
                    veh.setQuotationNo(vehicle.getQuotationNo());
                    veh.setDealerLogin(vehicle.getDealerId());
                    veh.setGarageLoginId(vehicle.getGarageId());
                    vehList.add(veh);
                }

                response.setErrors(Collections.emptyList());
                response.setMessage("Success");
                response.setIsError(false);
                response.setResponse(vehList);
            
            
            } else {
                //response.setErrors(Collections.singletonList("No vehicles found for the provided claim numbers and status"));
                response.setMessage("Failed");
                response.setIsError(true);
                response.setResponse(Collections.emptyList());
            }
        } catch (Exception e) {
            response.setErrors(Collections.singletonList("An error occurred: " + e.getMessage()));
            response.setMessage("Failed");
            response.setIsError(true);
            e.printStackTrace();
        }

        return response;
    }


	@Override
	public CommonResponse dealerView(VehicleInfoRequest request) {
		CommonResponse response = new CommonResponse(); 
        List<VehicleInfoResponse> vehList = new ArrayList<>();
        
        try {

            // Fetch the work orders based on the garage ID
            List<GarageWorkOrder> workOrders = garageWorkOrderRepository.findBySparepartsDealerId(request.getSparepartsDealerId());

            // Check if there are any work orders for this garage
            if (workOrders.isEmpty()) {
                response.setMessage("No data found");
                response.setIsError(false);
                return response;
            }
            // Claim Number List
            List<String> claimNumbers = workOrders.stream().map(GarageWorkOrder::getClaimNo)
                    .collect(Collectors.toList());
            
         // Collect quotation numbers and map claim to status in one step
            Map<String, String> quotationToStatusMap = workOrders.stream()
                .collect(Collectors.toMap(GarageWorkOrder::getQuotationNo, GarageWorkOrder::getStatus));
           

            // Fetch the list of vehicle info based on claim numbers and status
            List<InsuredVehicleInfo> vehicleInfoList = insuredVehicleInfoRepository.findByClaimNoInAndDealerId(claimNumbers,request.getSparepartsDealerId());
            
            // Check if any vehicles were found for the provided claim numbers and status
            if (!vehicleInfoList.isEmpty()) {
                for (InsuredVehicleInfo vehicle : vehicleInfoList) {
                    // Create a new VehicleInfoResponse object and populate it
                    VehicleInfoResponse veh = new VehicleInfoResponse();
                    
                    veh.setCompanyId(vehicle.getCompanyId() != null ? String.valueOf(vehicle.getCompanyId()) : null);
                    veh.setPolicyNo(vehicle.getPolicyNo());
                    veh.setClaimNo(vehicle.getClaimNo());
                    veh.setVehicleMake(vehicle.getVehicleMake());
                    veh.setVehicleModel(vehicle.getVehicleModel());
                    veh.setMakeYear(vehicle.getMakeYear() != null ? String.valueOf(vehicle.getMakeYear()) : null);
                    veh.setChassisNo(vehicle.getChassisNo());
                    veh.setInsuredName(vehicle.getInsuredName());
                    veh.setType(vehicle.getType());
                    veh.setVehicleRegNo(vehicle.getVehicleRegNo()); 
                    veh.setEntryDate(vehicle.getEntryDate());
                    veh.setQuoteStatus(vehicle.getStatus());
                    veh.setStatus(quotationToStatusMap.get(vehicle.getQuotationNo()));
                    veh.setQuotationNo(vehicle.getQuotationNo());
                    veh.setGarageLoginId(vehicle.getGarageId());
                    veh.setDealerLogin(request.getSparepartsDealerId());
                    // Add the populated response to the list
                    vehList.add(veh);
                }
                response.setErrors(Collections.emptyList());
                response.setMessage("Success");
                response.setResponse(vehList);
            } else {
                //response.setErrors(Collections.singletonList("No vehicles found for the provided claim numbers and status"));
                response.setMessage("Failed");
                response.setIsError(true);
                response.setResponse(Collections.emptyList());
            }
        } catch (Exception e) {
            response.setErrors(Collections.singletonList("An error occurred: " + e.getMessage()));
            response.setMessage("Failed");
            response.setIsError(true);
            e.printStackTrace();
        }

        return response;
	}
	
	@Override
	public CommonResponse surveyorViewV1(VehicleInfoRequest request) {
		CommonResponse response = new CommonResponse();
	    try {
	        
	    	List<SurveyorViewResponse> vehList = new ArrayList<>();
	    	
	    	List<DamageSectionDetails> details=new ArrayList<>();
	        
	        // Fetch damage section details based on Status
	    	if(request.getGarageId()!=null) {
	         details = damageRepository.findByStatusAndGarageLoginId("Dealer",request.getGarageId());
	    	}
	    	else if(request.getClaimNo()!=null) {
	          details = damageRepository.findByStatusAndClaimNo("Dealer",request.getClaimNo());
	    	}
	        if(details != null) {
	        	
	        for(DamageSectionDetails damage : details) {
	        	
	        	// Filter by claimNo if provided
	        	if (request.getClaimNo() != null && !request.getClaimNo().isBlank()) {
	        	    if (!request.getClaimNo().equalsIgnoreCase(damage.getClaimNo())) {
	        	        continue;
	        	    }
	        	}
	        	
	        	SurveyorViewResponse veh = new SurveyorViewResponse();
	        	
	        	veh.setDamageSno(String.valueOf(damage.getDamageSno()));
	        	veh.setDamageDictDesc(Optional.ofNullable(damage.getDamageDirection()).orElse(""));
	        	veh.setDamagePart(Optional.ofNullable(damage.getDamagePart()).orElse(""));
	        	veh.setRepairReplace(Optional.ofNullable(damage.getRepairReplace()).orElse(""));
	        	veh.setNoOfParts(Optional.ofNullable(damage.getNoOfParts()).map(String::valueOf).orElse(""));
	        	veh.setGaragePrice(Optional.ofNullable(damage.getGaragePrice()).map(String::valueOf).orElse(""));
	        	veh.setDealerPrice(Optional.ofNullable(damage.getDealerPrice()).map(String::valueOf).orElse(""));
	        	veh.setGarageLoginId(Optional.ofNullable(damage.getGarageLoginId()).orElse(""));
	        	veh.setDealerLoginId(Optional.ofNullable(damage.getDealerLoginId()).orElse(""));
	        	
	        	Optional<InsuredVehicleInfo> vehicleInfoList = insuredVehicleInfoRepository.findByClaimNoAndGarageId(damage.getClaimNo(),damage.getGarageLoginId());
	        	if(vehicleInfoList.isPresent()) {
	        		
	        		InsuredVehicleInfo vehicle = vehicleInfoList.get();
	        		
	        		  if (request.getVehicleRegNo() != null && !request.getVehicleRegNo().isEmpty()) {
	        		        if (!request.getVehicleRegNo().equalsIgnoreCase(vehicle.getVehicleRegNo())) {
	        		            continue; 
	        		        }
	        		    }
	        		
	        		veh.setCompanyId(vehicle.getCompanyId() != null ? String.valueOf(vehicle.getCompanyId()) : null);
                    veh.setPolicyNo(vehicle.getPolicyNo());
                    veh.setClaimNo(vehicle.getClaimNo());
                    veh.setVehicleMake(vehicle.getVehicleMake());
                    veh.setVehicleModel(vehicle.getVehicleModel());
                    veh.setMakeYear(vehicle.getMakeYear() != null ? String.valueOf(vehicle.getMakeYear()) : null);
                    veh.setChassisNo(vehicle.getChassisNo());
                    veh.setInsuredName(vehicle.getInsuredName());
                    veh.setType(vehicle.getType());
                    veh.setVehicleRegNo(vehicle.getVehicleRegNo()); 
                    veh.setEntryDate(vehicle.getEntryDate());
                    veh.setStatus(vehicle.getStatus());
                    veh.setQuoteStatus(vehicle.getStatus());
                    veh.setQuotationNo(damage.getQuotationNo());
	        	}
	        	 vehList.add(veh);
	        }
	        vehList = vehList.stream()
	        	    .filter(res -> "Replace".equalsIgnoreCase(res.getRepairReplace())) // Filter condition
	        	    .collect(Collectors.toList()); 
	        
                response.setErrors(Collections.emptyList());
                response.setMessage("Success");
                response.setResponse(vehList);
            } else {
                response.setErrors(Collections.singletonList("No vehicles found for the provided claim numbers and status"));
                response.setMessage("Failed");
                response.setIsError(true);
                response.setResponse(Collections.emptyList());
            }
	        
	    } catch (Exception e) {
	        // Handle exceptions
	    	String exceptionDetails = e.getClass().getSimpleName() + ": " + e.getMessage();
	        response.setResponse(exceptionDetails);
	        response.setMessage("Failed");
	        response.setResponse(null);
	    }
	    return response;
	}

	@Override
	public CommonResponse surveyorAsignedView(VehicleInfoRequest request) {
	    CommonResponse response = new CommonResponse();
	    try {
	        List<SurveyorViewResponse> vehList = new ArrayList<>();

	        String garageId = request.getGarageId();
	        String surveyorId = request.getSurveyorId();
	        String claimNo = request.getClaimNo();
	        String vehicleRegNo = request.getVehicleRegNo();

	        List<DamageSectionDetails> details = new ArrayList<>();

	        // Base filter: GarageId is mandatory
//	        if (garageId == null || garageId.isBlank()) {
//	            response.setIsError(true);
//	            response.setMessage("GarageId is required");
//	            response.setErrors(List.of("GarageId is missing"));
//	            return response;
//	        }

	           	 if(claimNo != null && surveyorId != null) {
		        	 details = damageRepository.findByClaimNoAndSurveyorId(claimNo,surveyorId);
		        }
	  
	         else if (!isNullOrEmpty(surveyorId) && !isNullOrEmpty(claimNo) && isNullOrEmpty(vehicleRegNo)) {
	            details = damageRepository.findByGarageLoginIdAndSurveyorIdAndClaimNo(garageId, surveyorId, claimNo);

	        } 
	        
	         else if (isNullOrEmpty(surveyorId) && isNullOrEmpty(claimNo) && isNullOrEmpty(vehicleRegNo)) {
	        	 details = damageRepository
	        		    .findByGarageLoginIdAndSurveyorIdAndGarageDealerIsNotNull(request.getGarageId(), request.getSurveyorId());
	        }
//	        else if (!isNullOrEmpty(surveyorId) && !isNullOrEmpty(claimNo) && !isNullOrEmpty(vehicleRegNo)) {
//	            details = damageRepository.findByGarageLoginIdAndSurveyorIdAndClaimNoAndVehicleRegNo(
//	                garageId, surveyorId, claimNo, vehicleRegNo);
//	        }

	        // Add directly assigned garage work orders
	        List<GarageWorkOrder> workList = garageWorkOrderRepository.findByGarageIdAndSparepartsDealerId(garageId, null);
	        if (workList != null) {
	            workList = workList.stream()
	                .filter(work -> "WA".equalsIgnoreCase(work.getQuoteStatus()))
	                .collect(Collectors.toList());

	            for (GarageWorkOrder work : workList) {
	                List<DamageSectionDetails> completedDetails =
	                    damageRepository.findByClaimNoAndQuotationNo(work.getClaimNo(), work.getQuotationNo());
	                details.addAll(completedDetails);
	            }
	        }

	        // Final vehicle details response
	        for (DamageSectionDetails damage : details) {

	            // Optional filtering based on claimNo
	            if (!isNullOrEmpty(claimNo) && !claimNo.equalsIgnoreCase(damage.getClaimNo())) {
	                continue;
	            }

	            // Optional filtering based on claimNo + vehicleRegNo
	            if (!isNullOrEmpty(claimNo) && !isNullOrEmpty(vehicleRegNo)) {
	                Optional<InsuredVehicleInfo> vehicleInfoFilter = insuredVehicleInfoRepository
	                    .findByClaimNoAndGarageIdAndSurveyorId(damage.getClaimNo(), damage.getGarageLoginId(), surveyorId);

	                if (vehicleInfoFilter.isPresent()) {
	                    String dbRegNo = vehicleInfoFilter.get().getVehicleRegNo();
	                    if (!vehicleRegNo.equalsIgnoreCase(dbRegNo)) {
	                        continue;
	                    }
	                } else {
	                    continue; // no matching vehicle info, skip
	                }
	            }

	            SurveyorViewResponse veh = new SurveyorViewResponse();

	            veh.setDamageSno(String.valueOf(damage.getDamageSno()));
	            veh.setDamageDictDesc(Optional.ofNullable(damage.getDamageDirection()).orElse(""));
	            veh.setDamagePart(Optional.ofNullable(damage.getDamagePart()).orElse(""));
	            veh.setRepairReplace(Optional.ofNullable(damage.getRepairReplace()).orElse(""));
	            veh.setNoOfParts(Optional.ofNullable(damage.getNoOfParts()).map(String::valueOf).orElse(""));
	            veh.setGaragePrice(Optional.ofNullable(damage.getGaragePrice()).map(String::valueOf).orElse(""));
	            veh.setDealerPrice(Optional.ofNullable(damage.getDealerPrice()).map(String::valueOf).orElse(""));
	            veh.setGarageLoginId(Optional.ofNullable(damage.getGarageLoginId()).orElse(""));
	            veh.setDealerLoginId(Optional.ofNullable(damage.getDealerLoginId()).orElse(""));
	            veh.setAssignedTo(damage.getGarageDealer());

	            Optional<InsuredVehicleInfo> vehicleInfoOpt =
	                insuredVehicleInfoRepository.findByClaimNoAndGarageIdAndSurveyorId(
	                    damage.getClaimNo(), damage.getGarageLoginId(), surveyorId);

	            vehicleInfoOpt.ifPresent(vehicle -> {
	                veh.setCompanyId(vehicle.getCompanyId() != null ? String.valueOf(vehicle.getCompanyId()) : null);
	                veh.setPolicyNo(vehicle.getPolicyNo());
	                veh.setClaimNo(vehicle.getClaimNo());
	                veh.setVehicleMake(vehicle.getVehicleMake());
	                veh.setVehicleModel(vehicle.getVehicleModel());
	                veh.setMakeYear(vehicle.getMakeYear() != null ? String.valueOf(vehicle.getMakeYear()) : null);
	                veh.setChassisNo(vehicle.getChassisNo());
	                veh.setInsuredName(vehicle.getInsuredName());
	                veh.setType(vehicle.getType());
	                veh.setVehicleRegNo(vehicle.getVehicleRegNo());
	                veh.setEntryDate(vehicle.getEntryDate());
	                veh.setStatus(vehicle.getStatus());
	                veh.setQuoteStatus(vehicle.getStatus());
	                veh.setQuotationNo(damage.getQuotationNo());
	            });

	            vehList.add(veh);
	        }

	        if (!vehList.isEmpty()) {
	            response.setErrors(Collections.emptyList());
	            response.setMessage("Success");
	            response.setResponse(vehList);
	        } 
//	        else {
//	            response.setErrors(List.of("No matching records found"));
//	            response.setMessage("Failed");
//	            response.setIsError(true);
//	            response.setResponse(Collections.emptyList());
//	        }
	    } catch (Exception e) {
	        response.setMessage("Failed");
	        response.setIsError(true);
	        response.setResponse(null);
	        response.setErrors(List.of("Exception: " + e.getMessage()));
	    }
	    return response;
	}

	private boolean isNullOrEmpty(String str) {
	    return str == null || str.trim().isEmpty();
	}


	@Override
	public CommonResponse dealerStatusSave(VehicleInfoRequest request) {
		CommonResponse response = new CommonResponse();
        
        try {

            Optional<GarageWorkOrder> workOrdersOptional = garageWorkOrderRepository.findByClaimNoAndGarageId(request.getClaimNo(),request.getGarageId());
            	
            if (!workOrdersOptional.isPresent()) {
                response.setErrors(Collections.singletonList("No work orders found for the given Claim No"));
                response.setMessage("Failed");
                response.setIsError(true);
                return response;
            }else if(StringUtils.isBlank(request.getQuoteStatus())) {
            	response.setErrors(Collections.singletonList("Status Cannot be empty"));
                response.setMessage("Failed");
                response.setIsError(true);
                return response;
            }
            
            GarageWorkOrder workOrders = workOrdersOptional.get();
            workOrders.setQuoteStatus(request.getQuoteStatus());
            garageWorkOrderRepository.save(workOrders);

            Optional<InsuredVehicleInfo> vehicleInfoOptional = insuredVehicleInfoRepository.findByClaimNoAndGarageId(request.getClaimNo(),request.getGarageId());
            
            if (!vehicleInfoOptional.isPresent()) {
                response.setErrors(Collections.singletonList("No Vehicle found for the given Claim No"));
                response.setMessage("Failed");
                response.setIsError(true);
                return response;
            }
            InsuredVehicleInfo vehicleInfo = vehicleInfoOptional.get();
            vehicleInfo.setStatus(request.getQuoteStatus());
            insuredVehicleInfoRepository.save(vehicleInfo);
            
                response.setErrors(Collections.emptyList());
                response.setMessage("Success");

        } catch (Exception e) {
            response.setErrors(Collections.singletonList("An error occurred: " + e.getMessage()));
            response.setMessage("Failed");
            response.setIsError(true);
            e.printStackTrace();
        }

        return response;
	}

	@Override
	public CommonResponse getExternalGarageListByGarageId(ExternalVehicleGarageViewRequest request) {
	    CommonResponse response = new CommonResponse();
	    List<VehicleInfoResponse> vehList = new ArrayList<>();
	    
	    try {
	        CommonResponse comRes = ExternalApiServiceImpl.getGarageClaimList(request);
	        if (comRes.getResponse() != null) {
	            List<GarageClaimListDataDto> externalData = (List<GarageClaimListDataDto>) comRes.getResponse();
	            for (GarageClaimListDataDto data : externalData) {
	                VehicleInfoResponse veh = new VehicleInfoResponse();
	                
	                // Mapping fields
	                veh.setCompanyId(request.getCompanyId()); 
	                veh.setPolicyNo(data.getPolicyNo());
	                veh.setClaimNo(data.getClaimNo());
	                veh.setVehicleMake(data.getMake());
	                veh.setVehicleModel(data.getModel());
	                veh.setMakeYear(data.getYear());
	                veh.setChassisNo(data.getChassisNo());
	                veh.setInsuredName(data.getInsuredName());
	                veh.setType(data.getBodyType());
	                veh.setVehicleRegNo(data.getVehRegNo());
	                veh.setEntryDate(new Date());
	                veh.setStatus("Y");
	                veh.setQuoteStatus("PFG");
	                veh.setQuotationNo(null);
	                veh.setDealerLogin(null);
	                veh.setGarageLoginId(data.getPartyId());
	                veh.setFnolSgsId(String.valueOf(data.getFnolSgsId())); // Convert int to String
	                veh.setLossLocation(data.getLossLocation());
	                
	                vehList.add(veh);
	            }
	        }
	        response.setResponse(vehList);
	        response.setErrors(Collections.emptyList());
	        response.setMessage("Success");
	        response.setIsError(false);

	    } catch (Exception e) {
	        response.setErrors(Collections.singletonList("An error occurred: " + e.getMessage()));
	        response.setMessage("Failed");
	        response.setIsError(true);
	        e.printStackTrace();
	    }

	    return response;
	}


	@Override
	public CommonResponse surveyorFilter(FilterGarageReq request) {
	    CommonResponse response = new CommonResponse();
	    List<VehicleInfoResponse> vehList = new ArrayList<>();

	    try {
	        List<InsuredVehicleInfo> vehicleInfo = new ArrayList<>();

	        List<String> allowedStatus=Arrays.asList("GPC", "DA", "CRFG", "WA", "CPTS", "DDE");
	        // 🚘 CASE 1: When Garage ID is given → Get list of CLAIM NOs
	        if (request.getGarageId() != null && !request.getGarageId().isEmpty() && request.getSurveyorId() !=null) {
	            vehicleInfo = insuredVehicleInfoRepository.findByGarageIdAndSurveyorId(request.getGarageId(),request.getSurveyorId());

	            // Map to ClaimNo only
	            List<String> claimNos = vehicleInfo.stream()
	            	.filter(k->allowedStatus.contains(k.getStatus()))
	                .map(InsuredVehicleInfo::getClaimNo)
//	                .distinct()
	                .collect(Collectors.toList());

	            response.setResponse(claimNos);
	            response.setMessage("Claim numbers fetched successfully");
	            response.setIsError(false);
	            return response;

	        } 
	        // 🔍 CASE 2: When ClaimNo is given → Get list of VEHICLE REG NOs
	        else if (request.getClaimNo() != null && !request.getClaimNo().isEmpty() && request.getSurveyorId()!=null) {
	            vehicleInfo = insuredVehicleInfoRepository.findByClaimNoAndSurveyorId(request.getClaimNo(),request.getSurveyorId());

	            // Map to VehicleRegNo
	            List<String> vehicleRegNos = vehicleInfo.stream()
	                .map(InsuredVehicleInfo::getVehicleRegNo)
//	                .distinct()
	                .collect(Collectors.toList());

	            response.setResponse(vehicleRegNos);
	            response.setMessage("Vehicle registration numbers fetched successfully");
	            response.setIsError(false);
	            return response;
	        }

	        // If nothing is provided
	        response.setIsError(true);
	        response.setMessage("Please provide either GarageId or ClaimNo");

	    } catch (Exception e) {
	        response.setIsError(true);
	        response.setMessage("Exception occurred: " + e.getMessage());
	    }

	    return response;
	}


	@Override
	public CommonResponse getallClaimNo(String SurveyorId) {
	    CommonResponse response = new CommonResponse();
	    try {
	        List<InsuredVehicleInfo> vehicleList = insuredVehicleInfoRepository.findBySurveyorId(SurveyorId);
	        
	        // Allowed statuses
	        List<String> allowedStatuses = Arrays.asList("GPC", "DA", "CRFG", "WA", "CPTS", "DDE");

	        if (vehicleList.isEmpty()) {
	            response.setIsError(true);
	            response.setMessage("No ClaimNos found");
	            response.setResponse(Collections.emptyList());
	        } else {
	            List<GetAllClaimNoResponse> claimNos = vehicleList.stream()
	            		   .filter(k -> allowedStatuses.contains(k.getStatus()))
	                .map(k -> new GetAllClaimNoResponse(k.getClaimNo()))
	                .collect(Collectors.toList());

	            response.setIsError(false);
	            response.setMessage("Fetch Successful");
	            response.setResponse(claimNos);
	        }
	    } catch (Exception e) {
	        response.setIsError(true);
	        response.setMessage("Exception occurred: " + e.getMessage());
	        response.setResponse(Collections.emptyList());
	    }

	    return response;
	}


	@Override
	public CommonResponse surveyorgetAll(VehicleInfoRequest request) {
		  CommonResponse response = new CommonResponse(); 
	        List<VehicleInfoResponse> vehList = new ArrayList<>();
	        try {
	        	  List<InsuredVehicleInfo> vehicleInfoList = insuredVehicleInfoRepository.findByClaimNo(request.getClaimNo());

	             // Check if there are any work orders for this garage
	             if (vehicleInfoList.isEmpty()) {
	                 response.setMessage("Failed");
	                 response.setIsError(true);
	                 return response;
	             }
	             
	             for(InsuredVehicleInfo vehicle:vehicleInfoList) {
	            	 VehicleInfoResponse veh=new VehicleInfoResponse();
	            	   veh.setCompanyId(vehicle.getCompanyId() != null ? String.valueOf(vehicle.getCompanyId()) : null);
	                    veh.setPolicyNo(vehicle.getPolicyNo());
	                    veh.setClaimNo(vehicle.getClaimNo());
	                    veh.setVehicleMake(vehicle.getVehicleMake());
	                    veh.setVehicleModel(vehicle.getVehicleModel());
	                    veh.setMakeYear(vehicle.getMakeYear() != null ? String.valueOf(vehicle.getMakeYear()) : null);
	                    veh.setChassisNo(vehicle.getChassisNo());
	                    veh.setInsuredName(vehicle.getInsuredName());
	                    veh.setType(vehicle.getType());
	                    veh.setVehicleRegNo(vehicle.getVehicleRegNo()); 
	                    veh.setEntryDate(vehicle.getEntryDate());
	                    veh.setStatus(vehicle.getStatus());
	                    veh.setQuoteStatus(vehicle.getStatus());
	                    veh.setQuotationNo(vehicle.getQuotationNo());
	                    
	                    veh.setDealerLogin(vehicle.getDealerId());
	                    veh.setGarageLoginId(vehicle.getGarageId());
	                    vehList.add(veh);
	                }
	                response.setErrors(Collections.emptyList());
	                response.setMessage("Success");
	                response.setResponse(vehList);
	                return response;
	       
	         } catch (Exception e) {
	             response.setErrors(Collections.singletonList("An error occurred: " + e.getMessage()));
	             response.setMessage("Failed");
	             response.setIsError(true);
	             e.printStackTrace();
	         }

	         return response;
	}



	




//	@Override
//	public CommonResponse surveyorFilter(VehicleInfoRequest request) {
//		CommonResponse response=new CommonResponse();
//		 List<VehicleInfoResponse> vehList = new ArrayList<>();
//		 try {
//			 
//			   List<InsuredVehicleInfo> vehicleInfoList = new ArrayList<>();
//			   
//			   if (request.getGarageId() != null && request.getClaimNo() != null && request.getVehicleRegNo() != null) {
//		            vehicleInfoList = insuredVehicleInfoRepository
//		                    .findByGarageIdAndClaimNoAndVehicleRegNo(
//		                            request.getGarageId(),
//		                            request.getClaimNo(),
//		                            request.getVehicleRegNo()
//		                    );
//		        }
//		        else if (request.getGarageId() != null && request.getClaimNo() != null) {
//		            vehicleInfoList = insuredVehicleInfoRepository
//		                    .findByGarageIdAndClaimNo(
//		                            request.getGarageId(),
//		                            request.getClaimNo()
//		                    );
//		        }
//		
//		if(!vehicleInfoList.isEmpty()) {
//			for (InsuredVehicleInfo vehicle:vehicleInfoList) {
//				 VehicleInfoResponse veh = new VehicleInfoResponse();
//                 
//                 veh.setCompanyId(vehicle.getCompanyId() != null ? String.valueOf(vehicle.getCompanyId()) : null);
//                 veh.setPolicyNo(vehicle.getPolicyNo());
//                 veh.setClaimNo(vehicle.getClaimNo());
//                 veh.setVehicleMake(vehicle.getVehicleMake());
//                 veh.setVehicleModel(vehicle.getVehicleModel());
//                 veh.setMakeYear(vehicle.getMakeYear() != null ? String.valueOf(vehicle.getMakeYear()) : null);
//                 veh.setChassisNo(vehicle.getChassisNo());
//                 veh.setInsuredName(vehicle.getInsuredName());
//                 veh.setType(vehicle.getType());
//                 veh.setVehicleRegNo(vehicle.getVehicleRegNo()); 
//                 veh.setEntryDate(vehicle.getEntryDate());
//                 //veh.setStatus(vehicle.getStatus());
//                 veh.setQuoteStatus(vehicle.getStatus());
//                 veh.setQuotationNo(vehicle.getQuotationNo());
//                 
//                 veh.setDealerLogin(vehicle.getDealerId());
//                
//                 vehList.add(veh);
//			}
//			 response.setErrors(Collections.emptyList());
//             response.setMessage("Success");
//             response.setResponse(vehList);
//         } else {
//            
//             response.setMessage("Failed");
//             response.setIsError(true);
//             response.setResponse(Collections.emptyList());
//         }
//		 }catch (Exception e) {
//         response.setErrors(Collections.singletonList("An error occurred: " + e.getMessage()));
//         response.setMessage("Failed");
//         response.setIsError(true);
//         e.printStackTrace();
//     }
//
//     return response;
//		}
//		
	


//	@Override
//	public CommonResponse getFilterGarage(FilterGarageReq request) {
//		CommonResponse response = new CommonResponse();
//
//		try {
//			List<InsuredVehicleInfo> vehicleInfo=null;
//			
//			if(request.getGarageId()!=null && !request.getGarageId().isEmpty()) {
//				vehicleInfo=insuredVehicleInfoRepository.findByClaimNo(request.getClaimNo());
//			}else if (request.getVehicleRegNo()!=null && !request.getVehicleRegNo().isEmpty()) {
//				vehicleInfo=insuredVehicleInfoRepository.findByVehicleRegNo(request.getVehicleRegNo());
//			} 
//
//
//			if (vehicleInfo != null) {
//	            GarageFilterRes res = new GarageFilterRes();
//	            res.setGarageId(vehicleInfo.getGarageId());
//
//	            response.setResponse(res);
//	            response.setMessage("Success");
//	            response.setErrors(Collections.emptyList());
//	            response.setIsError(false);
//	        } else {
//	            response.setMessage("Vehicle information not found for given Claim No and Vehicle Reg No.");
//	            response.setErrors(Collections.singletonList("No data found."));
//	            response.setIsError(true);
//	        }
//			
//		} catch (Exception e) {
//			response.setErrors(Collections.singletonList("An error occurred" + e.getMessage()));
//			response.setMessage("failed");
//			response.setIsError(true);
//			e.printStackTrace();
//		}
//
//		return response;
//	}


}
