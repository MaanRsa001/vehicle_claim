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

    @PostMapping("/getbyclaimno")
    public ResponseEntity<List<GarageWorkOrderResponse>> getGarageWorkOrdersByClaimNo(
            @RequestBody GarageWorkOrderRequest request) {
        List<GarageWorkOrderResponse> response = service.getGarageWorkOrdersByClaimNo(
                request.getClaimNo(), request.getCreatedBy());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/getall")
    public ResponseEntity<List<GarageWorkOrderResponse>> getAllGarageWorkOrders(
            @RequestBody GarageWorkOrderRequest request) {
        List<GarageWorkOrderResponse> response = service.getAllGarageWorkOrders(request.getCreatedBy());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
