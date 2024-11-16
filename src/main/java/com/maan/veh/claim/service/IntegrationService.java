package com.maan.veh.claim.service;

import java.util.Map;

import com.maan.veh.claim.request.IntegrationRequest;

public interface IntegrationService {

	Map<String, Object> sendDataToExternalApi(IntegrationRequest request);

	//Map<String, Object> getVehicleInfo(List<String> request);

}
