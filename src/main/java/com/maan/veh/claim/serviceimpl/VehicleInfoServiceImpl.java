package com.maan.veh.claim.serviceimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.veh.claim.dto.GarageClaimListDataDto;
import com.maan.veh.claim.entity.DamageSectionDetails;
import com.maan.veh.claim.entity.GarageWorkOrder;
import com.maan.veh.claim.entity.InsuredVehicleInfo;
import com.maan.veh.claim.repository.DamageSectionDetailsRepository;
import com.maan.veh.claim.repository.GarageWorkOrderRepository;
import com.maan.veh.claim.repository.InsuredVehicleInfoRepository;
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

    @Override
    public CommonResponse getVehicleInfoByCompanyId(VehicleGarageViewRequest request) {
    	CommonResponse response = new CommonResponse(); 	
    	List<VehicleInfoResponse> vehList = new ArrayList<>();
        
        try {
        	
            // Fetch the list of vehicle info based on company ID
            //List<InsuredVehicleInfo> vehicleInfoList = insuredVehicleInfoRepository.findByCompanyIdAndStatusIn(Integer.valueOf(request.getCompanyId()),status);
        	List<InsuredVehicleInfo> vehicleInfoList = insuredVehicleInfoRepository.findByCompanyIdAndGarageId(Integer.valueOf(request.getCompanyId()),request.getGarageId());
        	
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

            // Fetch the work orders based on the garage ID
            List<GarageWorkOrder> workOrders = garageWorkOrderRepository.findByGarageId(request.getGarageId());

            // Check if there are any work orders for this garage
            if (workOrders.isEmpty()) {
                //response.setErrors(Collections.singletonList("No work orders found for the given garage ID"));
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
            //List<InsuredVehicleInfo> vehicleInfoList = insuredVehicleInfoRepository.findByClaimNoInAndGarageId(claimWithReplacement,request.getGarageId());
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
            //List<InsuredVehicleInfo> vehicleInfoList = insuredVehicleInfoRepository.findByClaimNoIn(claimNumbers);
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

//	@Override
//	public CommonResponse surveyorViewV1(VehicleInfoRequest request) {
//		CommonResponse response = new CommonResponse();
//	    try {
//	        
//	    	List<VehicleInfoResponse> vehList = new ArrayList<>();
//	        
//	        // Fetch damage section details based on Status
//	        List<DamageSectionDetails> details = damageRepository.findByStatusAndGarageLoginId("Dealer",request.getGarageId());
//	        
//	        List<String> claimNoList = details.stream()
//                    .map(DamageSectionDetails::getClaimNo) 
//                    .collect(Collectors.toList());
//	        
//	        Map<String, String> claimToQuotationMap = details.stream()
//	                .collect(Collectors.toMap(
//	                        DamageSectionDetails::getClaimNo,  // Key: claimNo
//	                        DamageSectionDetails::getQuotationNo // Value: quotationNo
//	                ));
//	        
//	        List<InsuredVehicleInfo> vehicleInfoList = insuredVehicleInfoRepository.findByClaimNoIn(claimNoList);
//	        
//	        
//	        if (!vehicleInfoList.isEmpty()) {
//                for (InsuredVehicleInfo vehicle : vehicleInfoList) {
//                    // Create a new VehicleInfoResponse object and populate it
//                    VehicleInfoResponse veh = new VehicleInfoResponse();
//                    
//                    veh.setCompanyId(vehicle.getCompanyId() != null ? String.valueOf(vehicle.getCompanyId()) : null);
//                    veh.setPolicyNo(vehicle.getPolicyNo());
//                    veh.setClaimNo(vehicle.getClaimNo());
//                    veh.setVehicleMake(vehicle.getVehicleMake());
//                    veh.setVehicleModel(vehicle.getVehicleModel());
//                    veh.setMakeYear(vehicle.getMakeYear() != null ? String.valueOf(vehicle.getMakeYear()) : null);
//                    veh.setChassisNo(vehicle.getChassisNo());
//                    veh.setInsuredName(vehicle.getInsuredName());
//                    veh.setType(vehicle.getType());
//                    veh.setVehicleRegNo(vehicle.getVehicleRegNo()); 
//                    veh.setEntryDate(vehicle.getEntryDate());
//                    //veh.setStatus(vehicle.getStatus());
//                    veh.setQuoteStatus(vehicle.getStatus());
//                    veh.setQuotationNo(claimToQuotationMap.get(vehicle.getClaimNo()));
//                    
//                    // Add the populated response to the list
//                    vehList.add(veh);
//                }
//                response.setErrors(Collections.emptyList());
//                response.setMessage("Success");
//                response.setResponse(vehList);
//            } else {
//                response.setErrors(Collections.singletonList("No vehicles found for the provided claim numbers and status"));
//                response.setMessage("Failed");
//                response.setIsError(true);
//                response.setResponse(Collections.emptyList());
//            }
//	        
//	    } catch (Exception e) {
//	        // Handle exceptions
//	    	String exceptionDetails = e.getClass().getSimpleName() + ": " + e.getMessage();
//	        response.setResponse(exceptionDetails);
//	        response.setMessage("Failed");
//	        response.setResponse(null);
//	    }
//	    return response;
//	}
	
	@Override
	public CommonResponse surveyorViewV1(VehicleInfoRequest request) {
		CommonResponse response = new CommonResponse();
	    try {
	        
	    	List<SurveyorViewResponse> vehList = new ArrayList<>();
	        
	        // Fetch damage section details based on Status
	        List<DamageSectionDetails> details = damageRepository.findByStatusAndGarageLoginId("Dealer",request.getGarageId());
	        if(details != null) {
	        	
	        for(DamageSectionDetails damage : details) {
	        	
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

	        // Fetch damage section details based on Status
	        List<DamageSectionDetails> details = damageRepository.findByGarageLoginIdAndSurveyorIdAndGarageDealerIsNotNull(request.getGarageId(),request.getSurveyorId());
	        
	        // Also collecting the workorder which is asigned directly to garage without going to dealer
	    	List<GarageWorkOrder> workList = garageWorkOrderRepository.findByGarageIdAndSparepartsDealerId(request.getGarageId(),null);
	    	if(workList != null) {
	    		workList = workList.stream().filter(work -> "WA".equalsIgnoreCase(work.getQuoteStatus())).collect(Collectors.toList());
	    	}
	    	
	        for(GarageWorkOrder work : workList) {
	    		List<DamageSectionDetails> completedDetails = damageRepository.findByClaimNoAndQuotationNo(work.getClaimNo(), work.getQuotationNo());
	    		details.addAll(completedDetails);
	    	}
	        
	        if(details != null) {
	        	
	        for(DamageSectionDetails damage : details) {
	        	
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
	        	
	        	Optional<InsuredVehicleInfo> vehicleInfoList = insuredVehicleInfoRepository.findByClaimNoAndGarageIdAndSurveyorId(damage.getClaimNo(),damage.getGarageLoginId(),request.getSurveyorId());
	        	if(vehicleInfoList.isPresent()) {
	        		
	        		InsuredVehicleInfo vehicle = vehicleInfoList.get();
	        		
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
//	        vehList = vehList.stream()
//	        	    .filter(res -> "Replace".equalsIgnoreCase(res.getRepairReplace())) // Filter condition
//	        	    .collect(Collectors.toList()); 
	        
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


}
