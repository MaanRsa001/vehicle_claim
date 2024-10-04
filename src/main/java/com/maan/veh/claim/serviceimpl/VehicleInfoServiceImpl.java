package com.maan.veh.claim.serviceimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.veh.claim.entity.GarageWorkOrder;
import com.maan.veh.claim.entity.InsuredVehicleInfo;
import com.maan.veh.claim.repository.GarageWorkOrderRepository;
import com.maan.veh.claim.repository.InsuredVehicleInfoRepository;
import com.maan.veh.claim.request.VehicleInfoRequest;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.VehicleInfoResponse;
import com.maan.veh.claim.service.VehicleInfoService;

@Service
public class VehicleInfoServiceImpl implements VehicleInfoService {

    @Autowired
    private InsuredVehicleInfoRepository insuredVehicleInfoRepository;
    
    @Autowired
    private GarageWorkOrderRepository garageWorkOrderRepository;

    @Override
    public CommonResponse getVehicleInfoByCompanyId(VehicleInfoRequest request) {
    	CommonResponse response = new CommonResponse(); 	
    	List<VehicleInfoResponse> vehList = new ArrayList<>();
        
        try {
        	List<String> status = new ArrayList<>();
        	status.add("P");
        	status.add("Y");
        	status.add("I");
        	status.add("C");
        	status.add("R");
        	
            // Fetch the list of vehicle info based on company ID
            List<InsuredVehicleInfo> vehicleInfoList = insuredVehicleInfoRepository.findByCompanyIdAndStatusIn(Integer.valueOf(request.getCompanyId()),status);

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
	                if("Y".equalsIgnoreCase(vehicle.getStatus())) {
	                	//veh.setStatus("P");
	                	veh.setQuoteStatus("P");
	                }else {
	                	//veh.setStatus(vehicle.getStatus());
	                	veh.setQuoteStatus(vehicle.getStatus());
	                }
	                //setting quote number
	                Optional<GarageWorkOrder> workorder = garageWorkOrderRepository.findByClaimNo(vehicle.getClaimNo());
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
            Optional<InsuredVehicleInfo> optionalVehicleInfo = insuredVehicleInfoRepository.findByClaimNo(request.getClaimNo());
            
            // Check if the vehicle info is present
            if (optionalVehicleInfo.isPresent()) {
                InsuredVehicleInfo insuredVehicleInfo = optionalVehicleInfo.get();
                
                // Update the status of the vehicle info to "Rejected"
                insuredVehicleInfo.setStatus("R");
                
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
            // Define the status to filter by
            List<String> status = Arrays.asList("C");

            // Fetch the work orders based on the garage ID
            List<GarageWorkOrder> workOrders = garageWorkOrderRepository.findByGarageId(request.getGarageId());

            // Check if there are any work orders for this garage
            if (workOrders.isEmpty()) {
                response.setErrors(Collections.singletonList("No work orders found for the given garage ID"));
                response.setMessage("Failed");
                response.setIsError(true);
                return response;
            }

            // Collect claim numbers and map claim to quotation numbers in one step
            Map<String, String> claimToQuotationMap = workOrders.stream()
                .collect(Collectors.toMap(GarageWorkOrder::getClaimNo, GarageWorkOrder::getQuotationNo));

            List<String> claimNumbers = new ArrayList<>(claimToQuotationMap.keySet());

            // Fetch the list of vehicle info based on claim numbers and status
            List<InsuredVehicleInfo> vehicleInfoList = insuredVehicleInfoRepository.findByClaimNoInAndStatusIn(claimNumbers, status);

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
                    
                    // Add the populated response to the list
                    vehList.add(veh);
                }
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
            // Define the status to filter by
            List<String> status = Arrays.asList("C");

            // Fetch the work orders based on the garage ID
            List<GarageWorkOrder> workOrders = garageWorkOrderRepository.findBySparepartsDealerId(request.getSparepartsDealerId());

            // Check if there are any work orders for this garage
            if (workOrders.isEmpty()) {
                response.setErrors(Collections.singletonList("No work orders found for the given garage ID"));
                response.setMessage("Failed");
                response.setIsError(true);
                return response;
            }

            // Collect claim numbers and map claim to quotation numbers in one step
            Map<String, String> claimToQuotationMap = workOrders.stream()
                .collect(Collectors.toMap(GarageWorkOrder::getClaimNo, GarageWorkOrder::getQuotationNo));
            
         // Collect claim numbers and map claim to status in one step
            Map<String, String> claimToStatusMap = workOrders.stream()
                .collect(Collectors.toMap(GarageWorkOrder::getClaimNo, GarageWorkOrder::getStatus));

            List<String> claimNumbers = new ArrayList<>(claimToQuotationMap.keySet());

            // Fetch the list of vehicle info based on claim numbers and status
            List<InsuredVehicleInfo> vehicleInfoList = insuredVehicleInfoRepository.findByClaimNoInAndStatusIn(claimNumbers, status);

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
                    veh.setStatus(claimToStatusMap.get(vehicle.getClaimNo()));
                    veh.setQuotationNo(claimToQuotationMap.get(vehicle.getClaimNo()));
                    
                    // Add the populated response to the list
                    vehList.add(veh);
                }
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
            response.setErrors(Collections.singletonList("An error occurred: " + e.getMessage()));
            response.setMessage("Failed");
            response.setIsError(true);
            e.printStackTrace();
        }

        return response;
	}

}
