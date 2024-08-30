package com.maan.veh.claim.serviceimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.veh.claim.entity.InsuredVehicleInfo;
import com.maan.veh.claim.repository.InsuredVehicleInfoRepository;
import com.maan.veh.claim.request.VehicleInfoRequest;
import com.maan.veh.claim.response.VehicleInfoResponse;
import com.maan.veh.claim.service.VehicleInfoService;

@Service
public class VehicleInfoServiceImpl implements VehicleInfoService {

    @Autowired
    private InsuredVehicleInfoRepository insuredVehicleInfoRepository;

    @Override
    public List<VehicleInfoResponse> getVehicleInfoByCompanyId(VehicleInfoRequest request) {
        List<VehicleInfoResponse> responseList = new ArrayList<>();
        
        try {
            // Fetch the list of vehicle info based on company ID
            List<InsuredVehicleInfo> vehicleInfoList = insuredVehicleInfoRepository.findByCompanyId(Integer.valueOf(request.getCompanyId()));

            // Convert each entity to a response object and add to the response list
            for (InsuredVehicleInfo vehicle : vehicleInfoList) {
                // Create a new VehicleInfoResponse object
                VehicleInfoResponse response = new VehicleInfoResponse();
                
                // Set each property one by one
                response.setCompanyId(vehicle.getCompanyId() != null ? String.valueOf(vehicle.getCompanyId()) : null);
                response.setPolicyNo(vehicle.getPolicyNo());
                response.setClaimNo(vehicle.getClaimNo());
                response.setVehicleMake(vehicle.getVehicleMake());
                response.setVehicleModel(vehicle.getVehicleModel());
                response.setMakeYear(vehicle.getMakeYear() != null ? String.valueOf(vehicle.getMakeYear()) : null);
                response.setChassisNo(vehicle.getChassisNo());
                response.setInsuredName(vehicle.getInsuredName());
                response.setType(vehicle.getType());
                response.setVehicleRegNo(vehicle.getVehicleRegNo()); 
                response.setEntryDate(vehicle.getEntryDate());
                response.setStatus(vehicle.getStatus());
                
                // Add the populated response to the list
                responseList.add(response);
            }
        } catch (Exception e) {
            // Print stack trace for debugging (consider logging in production code)
            e.printStackTrace();
            // Return an empty list in case of an error
            return Collections.emptyList();
        }

        return responseList;
    }
}
