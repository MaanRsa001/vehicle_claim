package com.maan.veh.claim.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.veh.claim.request.ClaimListRequest;
import com.maan.veh.claim.request.ClaimTransactionRequest;
import com.maan.veh.claim.request.FnolRequest;
import com.maan.veh.claim.request.GetClaimRequest;
import com.maan.veh.claim.request.LoginRequest;
import com.maan.veh.claim.request.SaveClaimRequest;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.service.ExternalApiService;

@RestController
@RequestMapping("/fnol")
public class ClaimIntimationController {

    @Autowired
    private ExternalApiService externalApiService;

    @PostMapping("/createfnol")
    public ResponseEntity<CommonResponse> createFnol(@RequestBody SaveClaimRequest requestPayload) {
        	CommonResponse res = externalApiService.createFnol(requestPayload);
            return ResponseEntity.ok(res);
    }
    
    @PostMapping("/findfnol")
    public CommonResponse findFNOL(@RequestBody FnolRequest request) {
        return externalApiService.findFNOL(request);
    }
    
    @PostMapping("/getfnolstatus")
    public CommonResponse getFnolStatus(@RequestBody ClaimTransactionRequest request) {
        return externalApiService.getFnolStatus(request);
    }
    
    @PostMapping("/authenticate")
    public ResponseEntity<CommonResponse> authenticateUser(@RequestBody LoginRequest request) {
        CommonResponse response = externalApiService.authenticateUser(request);
        return ResponseEntity.ok(response);
    }
    
 // New API to get a single claim by policy number
    @PostMapping("/getClaimByPolicy")
    public ResponseEntity<CommonResponse> getClaimByPolicy(@RequestBody GetClaimRequest request) {
        CommonResponse res = externalApiService.getClaimByPolicy(request.getPolicyNo());
        return ResponseEntity.ok(res);
    }

    // New API to get all claims
    @PostMapping("/getAllClaims")
    public ResponseEntity<CommonResponse> getAllClaims(@RequestBody GetClaimRequest request) {
        CommonResponse res = externalApiService.getAllClaims();
        return ResponseEntity.ok(res);
    }
    
    @PostMapping("/claimListing")
    public ResponseEntity<CommonResponse> getClaimListing(@RequestBody ClaimListRequest requestPayload) {
        	CommonResponse res = externalApiService.getClaimListing(requestPayload);
            return ResponseEntity.ok(res);
    }
}
