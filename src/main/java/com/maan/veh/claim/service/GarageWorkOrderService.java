package com.maan.veh.claim.service;

import java.util.List;

import com.maan.veh.claim.response.GarageWorkOrderResponse;

public interface GarageWorkOrderService {
    List<GarageWorkOrderResponse> getGarageWorkOrders(String claimNo, String createdBy);
}
