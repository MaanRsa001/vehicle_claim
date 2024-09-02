package com.maan.veh.claim.service;

import com.maan.veh.claim.request.VehicleInfoRequest;
import com.maan.veh.claim.response.CommonResponse;

public interface VehicleInfoService {
	CommonResponse getVehicleInfoByCompanyId(VehicleInfoRequest request);
}
