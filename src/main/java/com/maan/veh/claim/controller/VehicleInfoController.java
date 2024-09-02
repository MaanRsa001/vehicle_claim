package com.maan.veh.claim.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.veh.claim.request.VehicleInfoRequest;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.VehicleInfoResponse;
import com.maan.veh.claim.service.VehicleInfoService;

@RestController
@RequestMapping("/api/vehicleinfo")
public class VehicleInfoController {

    @Autowired
    private VehicleInfoService vehicleInfoService;

    @PostMapping("/getAll")
    public ResponseEntity<CommonResponse> getVehicleInfoByCompanyId(@RequestBody VehicleInfoRequest request) {
    	CommonResponse response = vehicleInfoService.getVehicleInfoByCompanyId(request);
        return ResponseEntity.ok(response);
    }
}