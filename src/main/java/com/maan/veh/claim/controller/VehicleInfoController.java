package com.maan.veh.claim.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.veh.claim.dto.FilterGarageReq;
import com.maan.veh.claim.request.ExternalVehicleGarageViewRequest;
import com.maan.veh.claim.request.VehicleGarageViewRequest;
import com.maan.veh.claim.request.VehicleInfoRequest;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.service.VehicleInfoService;

import io.swagger.v3.oas.annotations.Operation;

@RestController
@RequestMapping("/vehicle")
public class VehicleInfoController {

	@Autowired
	private VehicleInfoService vehicleInfoService;

	@Operation(summary = "Retrieve Vehicle Info by Company ID", description = "This API is used to retrieve vehicle information for a specific company. "
			+ "The request should contain the company ID, and the response will include all relevant vehicle details.")
	@PostMapping("/garage/view")
	public ResponseEntity<CommonResponse> getVehicleInfoByCompanyId(@RequestBody VehicleGarageViewRequest request) {
		CommonResponse response = vehicleInfoService.getVehicleInfoByCompanyId(request);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Reject a Claim", description = "This API allows the rejection of a vehicle insurance claim. "
			+ "You must provide the claim information in the request. The system will update the claim status to 'Rejected'.")
	@PostMapping("/garage/rejectClaim")
	public ResponseEntity<CommonResponse> rejectClaim(@RequestBody VehicleInfoRequest request) {
		CommonResponse response = vehicleInfoService.rejectClaim(request);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Surveyor's View of Claims", description = "This API provides the surveyor with a view of claims related to vehicles. "
			+ "The surveyor will be able to see claim details, status, and other related information.")
	@PostMapping("/surveyor/view/pending")
	public ResponseEntity<CommonResponse> surveyorViewPending(@RequestBody VehicleInfoRequest request) {
		CommonResponse response = vehicleInfoService.surveyorView(request);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Dealer's View of Claims", description = "This API provides the dealer with a view of claims related to vehicles. "
			+ "The dealer will be able to see the details of each claim, including vehicle information and claim status.")
	@PostMapping("/dealer/view")
	public ResponseEntity<CommonResponse> dealerView(@RequestBody VehicleInfoRequest request) {
		CommonResponse response = vehicleInfoService.dealerView(request);
		return ResponseEntity.ok(response);
	}

	@Operation(summary = "Surveyor's View after Dealer entered the amount", description = "This API provides the surveyor with a view of claims related to vehicles. "
			+ "The surveyor will be able to see the details of each claim, including vehicle information and claim status.")
	@PostMapping("/surveyor/view/completed")
	public ResponseEntity<CommonResponse> surveyorViewV1(@RequestBody VehicleInfoRequest request) {
		CommonResponse response = vehicleInfoService.surveyorViewV1(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/surveyor/asigned/completed")
	public ResponseEntity<CommonResponse> surveyorAsignedView(@RequestBody VehicleInfoRequest request) {
		CommonResponse response = vehicleInfoService.surveyorAsignedView(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/surveyor/view")
	public ResponseEntity<CommonResponse> surveyorView(@RequestBody VehicleInfoRequest request) {
		CommonResponse response = vehicleInfoService.surveyorView(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/dealer/status/save")
	public ResponseEntity<CommonResponse> dealerStatusSave(@RequestBody VehicleInfoRequest request) {
		CommonResponse response = vehicleInfoService.dealerStatusSave(request);
		return ResponseEntity.ok(response);
	}

	@PostMapping("/external/garage/view")
	public ResponseEntity<CommonResponse> getExternalGarageList(@RequestBody ExternalVehicleGarageViewRequest request) {
		CommonResponse response = vehicleInfoService.getExternalGarageListByGarageId(request);
		return ResponseEntity.ok(response);
	}

//    @PostMapping("/filtergarage")
//    public ResponseEntity<CommonResponse> getFilterGarage(@RequestBody FilterGarageReq request){
//    	CommonResponse response=vehicleInfoService.getFilterGarage(request);
//    	return ResponseEntity.ok(response);
//    }

//    @PostMapping("/surveyor/filter")
//    public ResponseEntity<CommonResponse> surveyorFilter(@RequestBody VehicleInfoRequest request) {
//    	CommonResponse response = vehicleInfoService.surveyorFilter(request);
//        return ResponseEntity.ok(response);
//    }

	@PostMapping("/surveyor/filter")
	public ResponseEntity<CommonResponse> surveyorFilter(@RequestBody FilterGarageReq request) {
		CommonResponse response = vehicleInfoService.surveyorFilter(request);
		return ResponseEntity.ok(response);
	}
	
	@GetMapping("/getall/claimno/{SurveyorId}")
	public ResponseEntity<CommonResponse> getallClaimNo(@PathVariable String SurveyorId){
		CommonResponse response = vehicleInfoService.getallClaimNo(SurveyorId);
		return ResponseEntity.ok(response);
	}
	
	@PostMapping("/surveyor/getall")
	public ResponseEntity<CommonResponse> surveyorgetAll(@RequestBody VehicleInfoRequest request) {
		CommonResponse response = vehicleInfoService.surveyorgetAll(request);
		return ResponseEntity.ok(response);
	}
	
	
}
