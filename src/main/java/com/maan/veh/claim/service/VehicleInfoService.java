package com.maan.veh.claim.service;

import com.maan.veh.claim.request.VehicleGarageViewRequest;
import com.maan.veh.claim.request.VehicleInfoRequest;
import com.maan.veh.claim.response.CommonResponse;

public interface VehicleInfoService {
	CommonResponse getVehicleInfoByCompanyId(VehicleGarageViewRequest request);

	CommonResponse rejectClaim(VehicleInfoRequest request);

	CommonResponse surveyorView(VehicleInfoRequest request);

	CommonResponse dealerView(VehicleInfoRequest request);

	CommonResponse surveyorViewV1(VehicleInfoRequest request);

	CommonResponse surveyorAsignedView(VehicleInfoRequest request);

	CommonResponse dealerStatusSave(VehicleInfoRequest request);
}
