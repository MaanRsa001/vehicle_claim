package com.maan.veh.claim.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.veh.claim.request.VehicleInfoRequest;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.service.VehicleInfoService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/api/vehicleinfo")
public class VehicleInfoController {

    @Autowired
    private VehicleInfoService vehicleInfoService;

    @Operation(
        summary = "Retrieve Vehicle Info by Company ID",
        description = "This API is used to retrieve vehicle information for a specific company. "
                    + "The request should contain the company ID, and the response will include all relevant vehicle details."
    )
    @PostMapping("/getAll")
    public ResponseEntity<CommonResponse> getVehicleInfoByCompanyId(@RequestBody VehicleInfoRequest request) {
    	CommonResponse response = vehicleInfoService.getVehicleInfoByCompanyId(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Reject a Claim",
        description = "This API allows the rejection of a vehicle insurance claim. "
                    + "You must provide the claim information in the request. The system will update the claim status to 'Rejected'."
    )
    @PostMapping("/rejectClaim")
    public ResponseEntity<CommonResponse> rejectClaim(@RequestBody VehicleInfoRequest request) {
    	CommonResponse response = vehicleInfoService.rejectClaim(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Surveyor's View of Claims",
        description = "This API provides the surveyor with a view of claims related to vehicles. "
                    + "The surveyor will be able to see claim details, status, and other related information."
    )
    @PostMapping("/surveyorView")
    public ResponseEntity<CommonResponse> surveyorView(@RequestBody VehicleInfoRequest request) {
    	CommonResponse response = vehicleInfoService.surveyorView(request);
        return ResponseEntity.ok(response);
    }

    @Operation(
        summary = "Dealer's View of Claims",
        description = "This API provides the dealer with a view of claims related to vehicles. "
                    + "The dealer will be able to see the details of each claim, including vehicle information and claim status."
    )
    @PostMapping("/dealerView")
    public ResponseEntity<CommonResponse> dealerView(@RequestBody VehicleInfoRequest request) {
    	CommonResponse response = vehicleInfoService.dealerView(request);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
            summary = "Surveyor's View after Dealer entered the amount",
            description = "This API provides the surveyor with a view of claims related to vehicles. "
                        + "The surveyor will be able to see the details of each claim, including vehicle information and claim status."
        )
    @PostMapping("/surveyorViewV1")
    public ResponseEntity<CommonResponse> surveyorViewV1(@RequestBody VehicleInfoRequest request) {
    	CommonResponse response = vehicleInfoService.surveyorViewV1(request);
        return ResponseEntity.ok(response);
    }
}
