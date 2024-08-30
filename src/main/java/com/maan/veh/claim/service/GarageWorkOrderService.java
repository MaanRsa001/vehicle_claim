package com.maan.veh.claim.service;

import java.util.List;

import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.GarageWorkOrderResponse;
import com.maan.veh.claim.response.GarageWorkOrderSaveReq;

public interface GarageWorkOrderService {
	
    List<GarageWorkOrderResponse> getGarageWorkOrders(String claimNo, String createdBy);

	CommonResponse saveWorkOrder(GarageWorkOrderSaveReq claim);
}
