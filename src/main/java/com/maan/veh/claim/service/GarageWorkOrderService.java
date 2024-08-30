package com.maan.veh.claim.service;

import java.util.List;

import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.GarageWorkOrderResponse;
import com.maan.veh.claim.response.GarageWorkOrderSaveReq;

public interface GarageWorkOrderService {

	CommonResponse saveWorkOrder(GarageWorkOrderSaveReq claim);

    List<GarageWorkOrderResponse> getGarageWorkOrdersByClaimNo(String claimNo, String createdBy);

	List<GarageWorkOrderResponse> getAllGarageWorkOrders(String createdBy);

}
