package com.maan.veh.claim.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.veh.claim.response.CommonRes;
import com.maan.veh.claim.response.DropDownRes;
import com.maan.veh.claim.service.DropDownService;

@RestController
@RequestMapping("/dropdown")

public class DropDownController {

	@Autowired
	private DropDownService dropDownService;

	@GetMapping("/getdamagedirection")
	public ResponseEntity<CommonRes> getDamageDirection() {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getDamageDirection();
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

	@GetMapping("/getdamagedropdown")
	public ResponseEntity<CommonRes> getDamageDropdown() {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getDamageDropdown();
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

	@GetMapping("/getworkordertype")
	public ResponseEntity<CommonRes> getWorkOrderType() {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getWorkOrderType();
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

	@GetMapping("/getsettlementtype")
	public ResponseEntity<CommonRes> getSettlementType() {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getSettlementType();
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

	@GetMapping("/getlosstype")
	public ResponseEntity<CommonRes> getLossType() {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getLossType();
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

	@GetMapping("/getdamagetype")
	public ResponseEntity<CommonRes> getDamageType() {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getDamageType();
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

	@GetMapping(value = "/claimlosstype")
	public ResponseEntity<CommonRes> getLosstype() {

		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getLosstype();
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
	
	@GetMapping(value = "/vehiclebodyparts")
	public ResponseEntity<CommonRes> getbodyPart() {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getbodyPart();
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
	
	@GetMapping(value = "/vatpercentage")
	public ResponseEntity<CommonRes> getVatPercentage() {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getVatPercentage();
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
	
	@GetMapping(value = "/accountforsettlement")
	public ResponseEntity<CommonRes> getAccountForSettlement() {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getAccountForSettlement();
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
	
	@GetMapping(value = "/repairreplace")
	public ResponseEntity<CommonRes> getRepairReplace() {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getRepairReplace();
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
