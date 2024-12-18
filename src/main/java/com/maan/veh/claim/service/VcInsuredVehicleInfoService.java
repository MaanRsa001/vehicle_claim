package com.maan.veh.claim.service;

import org.springframework.web.bind.annotation.RequestBody;

import com.maan.veh.claim.dto.InsuredVehicleInfoDTO;
import com.maan.veh.claim.dto.InsuredVehicleMasterDTO;
import com.maan.veh.claim.request.VcInsuredVehicleInfoRequest;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.InsuredVehicleRes;

public interface VcInsuredVehicleInfoService {
	
	CommonResponse saveInsuredVehicle(@RequestBody InsuredVehicleMasterDTO req);

}
