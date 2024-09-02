package com.maan.veh.claim.controller;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.veh.claim.request.GarageWorkOrderRequest;
import com.maan.veh.claim.response.CommonResponse;
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
        return new ResponseEntity<>(savedClaim, HttpStatus.OK);
    }

    @PostMapping("/getbyclaimno")
    public ResponseEntity<CommonResponse> getGarageWorkOrdersByClaimNo(
            @RequestBody GarageWorkOrderRequest request) {
    	CommonResponse response = service.getGarageWorkOrdersByClaimNo(request);
    	 return new ResponseEntity<>(response, HttpStatus.OK);

    }
    
    @PostMapping("/getAll")
    public ResponseEntity<CommonResponse> getGarageWorkOrders(
            @RequestBody GarageWorkOrderRequest request) {
    	CommonResponse response = service.getGarageWorkOrders(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
 
}
