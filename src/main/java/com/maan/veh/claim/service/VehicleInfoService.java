package com.maan.veh.claim.service;

import java.util.List;

import com.maan.veh.claim.request.VehicleInfoRequest;
import com.maan.veh.claim.response.VehicleInfoResponse;

public interface VehicleInfoService {
    List<VehicleInfoResponse> getVehicleInfoByCompanyId(VehicleInfoRequest request);
}
