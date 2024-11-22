package com.maan.veh.claim.controller;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

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
	
	@GetMapping(value = "/garageLoginId")
	public ResponseEntity<CommonRes> getGarageLoginId() {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getGarageLoginId();
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
	
	@GetMapping(value = "/dealerLoginId")
	public ResponseEntity<CommonRes> getDealerLoginId() {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getDealerLoginId();
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
	
	@GetMapping(value = "/status")
	public ResponseEntity<CommonRes> getStatus() {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getStatus();
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
	
	@GetMapping(value = "/repairtype")
	public ResponseEntity<CommonRes> getRepairType() {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getRepairType();
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
	
	@GetMapping(value = "/documentType")
	public ResponseEntity<CommonRes> getDocumentType() {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.geDocumentType();
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
	
	@GetMapping(value = "/branch/{companyId}")
	public ResponseEntity<CommonRes> getBranch(@PathVariable String companyId) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getBranch(companyId);
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
	
	@GetMapping(value = "/company")
	public ResponseEntity<CommonRes> getCompany() {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getCompany();
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
	
	@GetMapping(value = "/country/{companyId}")
	public ResponseEntity<CommonRes> getCountry(@PathVariable String companyId) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getCountry(companyId);
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
	
	@GetMapping(value = "/city/{companyId}")
	public ResponseEntity<CommonRes> getCity(@PathVariable String companyId) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getCity(companyId);
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
	
	@GetMapping(value = "/error")
    public void getError() {
        // Throw a random 401 Unauthorized exception
        throwRandomHttpException();
    }

    private void throwRandomHttpException() {
        // List of possible exceptions with different HTTP statuses
        HttpStatus[] httpStatuses = {
            HttpStatus.UNAUTHORIZED, // 401
            HttpStatus.BAD_REQUEST, // 400
            HttpStatus.FORBIDDEN,   // 403
            HttpStatus.INTERNAL_SERVER_ERROR // 500
        };

        // Randomly pick an HTTP status
        Random random = new Random();
        HttpStatus randomStatus = httpStatuses[random.nextInt(httpStatuses.length)];

        // Throw the exception with the selected status
        throw new ResponseStatusException(randomStatus, "Random HTTP error occurred: " + randomStatus.getReasonPhrase());
    }

}
