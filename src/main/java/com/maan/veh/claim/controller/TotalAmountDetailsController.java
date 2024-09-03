package com.maan.veh.claim.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.veh.claim.request.TotalAmountDetailsRequest;
import com.maan.veh.claim.request.TotalAmountDetailsSaveRequest;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.service.TotalAmountDetailsService;

@RestController
@RequestMapping("/totalamount")
public class TotalAmountDetailsController {
	
	@Autowired
    private TotalAmountDetailsService service;
	
	@PostMapping("/getbyclaimno")
    public ResponseEntity<CommonResponse> getTotalAmountDetails(@RequestBody TotalAmountDetailsRequest request) {
		CommonResponse response = service.getTotalAmountDetailsByClaimNo(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
	}

	@PostMapping("/save")
	public ResponseEntity<CommonResponse> saveTotalAmountDetails(@RequestBody TotalAmountDetailsSaveRequest req) {
		CommonResponse response = service.saveTotalAmountDetails(req);
		return new ResponseEntity<>(response, HttpStatus.OK);
	}
	
	@PostMapping("/getall")
    public ResponseEntity<CommonResponse> getTotalAmountDetailsByCreatedBy(@RequestBody TotalAmountDetailsRequest request) {
        CommonResponse response = service.getTotalAmountDetailsByCreatedBy(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
