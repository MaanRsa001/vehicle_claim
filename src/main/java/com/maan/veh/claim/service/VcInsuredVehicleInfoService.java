package com.maan.veh.claim.service;

import org.springframework.web.bind.annotation.RequestBody;

import com.maan.veh.claim.dto.InsuredVehicleMasterDTO;
import com.maan.veh.claim.qiic.request.ClaimDetailsViewRequest;
import com.maan.veh.claim.response.CommonResponse;

public interface VcInsuredVehicleInfoService {
	
	CommonResponse saveInsuredVehicle(@RequestBody InsuredVehicleMasterDTO req);

	CommonResponse getVehicleInfo(ClaimDetailsViewRequest request);

}
