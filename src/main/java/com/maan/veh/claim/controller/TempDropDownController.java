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
import com.maan.veh.claim.service.ClaimDropDownService;

@RestController
@RequestMapping("/claim/dropdown")
public class TempDropDownController {
	
	@Autowired
	private ClaimDropDownService dropDownService;
	
	 @GetMapping(value = "/claimType")
	    public ResponseEntity<CommonRes> getClaimType() {
	        return getDropdownResponse(List.of(
	            new DropDownRes("1", "Accident"),
	            new DropDownRes("2", "Theft"),
	            new DropDownRes("3", "Fire"),
	            new DropDownRes("4", "Natural Disaster")
	        ));
	    }

	    @GetMapping(value = "/claimCategory")
	    public ResponseEntity<CommonRes> getClaimCategory() {
	        return getDropdownResponse(List.of(
	            new DropDownRes("1", "Minor"),
	            new DropDownRes("2", "Major"),
	            new DropDownRes("3", "Total Loss")
	        ));
	    }

	    @GetMapping(value = "/partyType")
	    public ResponseEntity<CommonRes> getPartyType() {
	        return getDropdownResponse(List.of(
	            new DropDownRes("1", "First Party"),
	            new DropDownRes("2", "Third Party")
	        ));
	    }

	    @GetMapping(value = "/atFault")
	    public ResponseEntity<CommonRes> getAtFault() {
	        return getDropdownResponse(List.of(
	            new DropDownRes("Y", "Yes"),
	            new DropDownRes("N", "No")
	        ));
	    }

	    @GetMapping(value = "/code")
	    public ResponseEntity<CommonRes> getCode() {
	        return getDropdownResponse(List.of(
	            new DropDownRes("1", "Code1"),
	            new DropDownRes("2", "Code2"),
	            new DropDownRes("3", "Code3")
	        ));
	    }

	    @GetMapping(value = "/nationality")
	    public ResponseEntity<CommonRes> getNationality() {
	        return getDropdownResponse(List.of(
	            new DropDownRes("1", "India"),
	            new DropDownRes("2", "USA"),
	            new DropDownRes("3", "UK")
	        ));
	    }

	    @GetMapping(value = "/vehicleInsurer")
	    public ResponseEntity<CommonRes> getVehicleInsurer() {
	        return getDropdownResponse(List.of(
	            new DropDownRes("1", "Allianz"),
	            new DropDownRes("2", "AXA"),
	            new DropDownRes("3", "Zurich")
	        ));
	    }

	    @GetMapping(value = "/vehicleMake")
	    public ResponseEntity<CommonRes> getVehicleMake() {
	        return getDropdownResponse(List.of(
	            new DropDownRes("1", "Toyota"),
	            new DropDownRes("2", "Honda"),
	            new DropDownRes("3", "Ford")
	        ));
	    }

	    @GetMapping(value = "/vehicleModel")
	    public ResponseEntity<CommonRes> getVehicleModel() {
	        return getDropdownResponse(List.of(
	            new DropDownRes("1", "Camry"),
	            new DropDownRes("2", "Civic"),
	            new DropDownRes("3", "Mustang")
	        ));
	    }

	    @GetMapping(value = "/thirdPartyType")
	    public ResponseEntity<CommonRes> getThirdPartyType() {
	        return getDropdownResponse(List.of(
	            new DropDownRes("1", "Pedestrian"),
	            new DropDownRes("2", "Vehicle"),
	            new DropDownRes("3", "Property")
	        ));
	    }

	    @GetMapping(value = "/documentType")
	    public ResponseEntity<CommonRes> getDocumentType() {
	        return getDropdownResponse(List.of(
	            new DropDownRes("1", "Invoice"),
	            new DropDownRes("2", "Receipt"),
	            new DropDownRes("3", "Report")
	        ));
	    }

	    // Common method for all dropdown responses
	    private ResponseEntity<CommonRes> getDropdownResponse(List<DropDownRes> resList) {
	        CommonRes data = new CommonRes();
	        data.setCommonResponse(resList);
	        data.setIsError(false);
	        data.setErrorMessage(null);
	        data.setMessage("Success");
	        data.setErroCode(0);

	        return new ResponseEntity<>(data, HttpStatus.OK);
	    }
	    
	    @GetMapping(value = "/policeStation")
	    public ResponseEntity<CommonRes> getPoliceStation() {
	    	CommonRes data = new CommonRes();

			List<DropDownRes> res = dropDownService.getPoliceStation();
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
	    
	    @GetMapping(value = "/losslocation")
	    public ResponseEntity<CommonRes> getLossLocation() {
	    	CommonRes data = new CommonRes();

			List<DropDownRes> res = dropDownService.getLossLocation();
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
	    
	    @GetMapping(value = "/natureofloss")
	    public ResponseEntity<CommonRes> getNatureOfLoss() {
	    	CommonRes data = new CommonRes();

			List<DropDownRes> res = dropDownService.getNatureOfLoss();
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
