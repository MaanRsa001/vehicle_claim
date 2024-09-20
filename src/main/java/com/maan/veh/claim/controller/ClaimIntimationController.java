package com.maan.veh.claim.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.veh.claim.request.ClaimTransactionRequest;
import com.maan.veh.claim.request.FnolRequest;
import com.maan.veh.claim.request.LoginRequest;
import com.maan.veh.claim.request.SaveClaimRequest;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.service.ExternalApiService;

@RestController
@RequestMapping("/fnol")
public class ClaimIntimationController {

    @Autowired
    private ExternalApiService externalApiService;

    @PostMapping("/saveClaimIntimation")
    public ResponseEntity<CommonResponse> saveClaimIntimation(@RequestBody SaveClaimRequest requestPayload) {
        	CommonResponse res = externalApiService.saveClaimIntimation(requestPayload);
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
}
