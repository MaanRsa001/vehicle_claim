package com.maan.veh.claim.service;

import com.maan.veh.claim.request.GarageWorkOrderRequest;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.GarageWorkOrderSaveReq;

public interface GarageWorkOrderService {

	CommonResponse getGarageWorkOrders(GarageWorkOrderRequest request);

	CommonResponse saveWorkOrder(GarageWorkOrderSaveReq claim);

	CommonResponse getGarageWorkOrdersByClaimNo(GarageWorkOrderRequest request);

	CommonResponse getAllGarageWorkOrders(GarageWorkOrderRequest request);


}
