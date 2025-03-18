package com.maan.veh.claim.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.veh.claim.dto.InsuredVehicleMasterDTO;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.service.VcInsuredVehicleInfoService;

@RestController
@RequestMapping("/insuredvehicleinfo")
public class VcInsuredVehicleInfoController {
	@Autowired
    private VcInsuredVehicleInfoService service;
	

	
	  @PostMapping("/save")
	    public ResponseEntity<CommonResponse> saveInsuredVehicleInfo(@RequestBody InsuredVehicleMasterDTO request) {

		  CommonResponse response =service.saveInsuredVehicle(request);
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }
	  
	  @PostMapping("surveyor/save")
	    public ResponseEntity<CommonResponse> assignSurveyorInsuredVehicleInfo(@RequestBody InsuredVehicleMasterDTO request) {

		  CommonResponse response =service.assignSurveyorInsuredVehicleInfo(request);
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }
	  
	  @PostMapping("dealer/save")
	    public ResponseEntity<CommonResponse> assignDealerInsuredVehicleInfo(@RequestBody InsuredVehicleMasterDTO request) {

		  CommonResponse response =service.assignDealerInsuredVehicleInfo(request);
	        return new ResponseEntity<>(response, HttpStatus.OK);
	    }

}
