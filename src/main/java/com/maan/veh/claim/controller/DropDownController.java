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

	@GetMapping("/getdamagedirection/{companyId}")
	public ResponseEntity<CommonRes> getDamageDirection(@PathVariable String companyId) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getDamageDirection(companyId);
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

	@GetMapping("/getdamagedropdown/{companyId}")
	public ResponseEntity<CommonRes> getDamageDropdown(@PathVariable String companyId) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getDamageDropdown(companyId);
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

	@GetMapping("/getworkordertype/{companyId}")
	public ResponseEntity<CommonRes> getWorkOrderType(@PathVariable String companyId) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getWorkOrderType(companyId);
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

	@GetMapping("/getsettlementtype/{companyId}")
	public ResponseEntity<CommonRes> getSettlementType(@PathVariable String companyId) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getSettlementType(companyId);
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

	@GetMapping("/getlosstype/{companyId}")
	public ResponseEntity<CommonRes> getLossType(@PathVariable String companyId) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getLossType(companyId);
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
	
	@GetMapping("/lossLocation/{companyId}")
	public ResponseEntity<CommonRes> getLossLocation(@PathVariable String companyId) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getLossLocation(companyId);
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

	@GetMapping("/getdamagetype/{companyId}")
	public ResponseEntity<CommonRes> getDamageType(@PathVariable String companyId) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getDamageType(companyId);
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

	@GetMapping(value = "/claimlosstype/{companyId}")
	public ResponseEntity<CommonRes> getLosstype(@PathVariable String companyId) {

		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getLosstype(companyId);
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
	
	@GetMapping(value = "/vehiclebodyparts/{companyId}")
	public ResponseEntity<CommonRes> getbodyPart(@PathVariable String companyId) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getbodyPart(companyId);
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
	
	@GetMapping(value = "/vatpercentage/{companyId}")
	public ResponseEntity<CommonRes> getVatPercentage(@PathVariable String companyId) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getVatPercentage(companyId);
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
	
	@GetMapping(value = "/accountforsettlement/{companyId}")
	public ResponseEntity<CommonRes> getAccountForSettlement(@PathVariable String companyId) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getAccountForSettlement(companyId);
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
	
	@GetMapping(value = "/repairreplace/{companyId}")
	public ResponseEntity<CommonRes> getRepairReplace(@PathVariable String companyId) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getRepairReplace(companyId);
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
	
	@GetMapping(value = "/garageLoginId/{companyId}")
	public ResponseEntity<CommonRes> getGarageLoginId(@PathVariable String companyId) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getGarageLoginId(companyId);
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
	
	@GetMapping(value = "/dealerLoginId/{companyId}")
	public ResponseEntity<CommonRes> getDealerLoginId(@PathVariable String companyId) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getDealerLoginId(companyId);
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
	
	@GetMapping(value = "/status/{companyId}")
	public ResponseEntity<CommonRes> getStatus(@PathVariable String companyId) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getStatus(companyId);
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
	
	@GetMapping(value = "/repairtype/{companyId}")
	public ResponseEntity<CommonRes> getRepairType(@PathVariable String companyId) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getRepairType(companyId);
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
	
	@GetMapping(value = "/documentType/{companyId}")
	public ResponseEntity<CommonRes> getDocumentType(@PathVariable String companyId) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.geDocumentType(companyId);
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
	
	@GetMapping(value = "/company/{companyId}")
	public ResponseEntity<CommonRes> getCompany(@PathVariable String companyId) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getCompany(companyId);
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
    
    @GetMapping(value = "/usertype/{companyId}")
	public ResponseEntity<CommonRes> getUserType(@PathVariable String companyId) {
		CommonRes data = new CommonRes();

		List<DropDownRes> res = dropDownService.getUserType(companyId);
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
