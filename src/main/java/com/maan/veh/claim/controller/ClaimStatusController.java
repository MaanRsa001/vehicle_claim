package com.maan.veh.claim.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.veh.claim.response.CommonRes;
import com.maan.veh.claim.response.DropDownRes;
import com.maan.veh.claim.service.ClaimStatusService;

@RestController
@RequestMapping("/claim")
public class ClaimStatusController {
	
	@Autowired
	private ClaimStatusService service;

	@GetMapping("/garage/status/{currentStatus}")
	public ResponseEntity<CommonRes> getGarageStatus(@PathVariable String currentStatus) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = service.getGarageStatus(currentStatus);
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(null);
		data.setMessage("Success");

		if (res != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}
	
	@GetMapping("/surveyor/status/{currentStatus}")
	public ResponseEntity<CommonRes> getSurveyorStatus(@PathVariable String currentStatus) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = service.getSurveyorStatus(currentStatus);
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(null);
		data.setMessage("Success");

		if (res != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}
	
	@GetMapping("/dealer/status/{currentStatus}")
	public ResponseEntity<CommonRes> getDealerStatus(@PathVariable String currentStatus) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = service.getDealerStatus(currentStatus);
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(null);
		data.setMessage("Success");

		if (res != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}
	
	@GetMapping("/grid/status/{usertype}/{companyId}")
	public ResponseEntity<CommonRes> getGridStatus(@PathVariable String usertype,@PathVariable String companyId) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = service.getGridStatus(usertype,companyId,"1");
		data.setCommonResponse(res);
		data.setIsError(false);
		data.setErrorMessage(null);
		data.setMessage("Success");

		if (res != null) {
			return new ResponseEntity<CommonRes>(data, HttpStatus.CREATED);
		} else {
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}
}
