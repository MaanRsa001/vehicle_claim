package com.maan.veh.claim.controller;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.veh.claim.request.GarageWorkOrderRequest;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.GarageWorkOrderResponse;
import com.maan.veh.claim.response.GarageWorkOrderSaveReq;
import com.maan.veh.claim.service.GarageWorkOrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/garage")
public class GarageWorkOrderController {

    @Autowired
    private GarageWorkOrderService service;

    
    @PostMapping("/save")
    public ResponseEntity<CommonResponse> saveWorkOrder(@RequestBody GarageWorkOrderSaveReq claim) {
    	CommonResponse savedClaim = service.saveWorkOrder(claim);
        return new ResponseEntity<>(savedClaim, HttpStatus.CREATED);
    }
    
    @PostMapping("/get")
    public ResponseEntity<List<GarageWorkOrderResponse>> getGarageWorkOrders(
            @RequestBody GarageWorkOrderRequest request) {
        List<GarageWorkOrderResponse> response = service.getGarageWorkOrders(
                request.getClaimNo(), request.getCreatedBy());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
