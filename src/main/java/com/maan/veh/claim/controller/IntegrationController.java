package com.maan.veh.claim.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.veh.claim.request.IntegrationRequest;
import com.maan.veh.claim.service.IntegrationService;

@RestController
@RequestMapping("/api/integration")
public class IntegrationController {
	
	@Autowired
	private IntegrationService service;
	
	@PostMapping("/pushClaimInfo")
    public ResponseEntity<Map<String, Object>> pushClaimInfo(@RequestBody IntegrationRequest request) {

        Map<String, Object> response = service.sendDataToExternalApi(request);
       
        return ResponseEntity.ok(response);
    }
	
	@PostMapping("/getClaimInfo")
    public ResponseEntity<Map<String, Object>> getClaimInfo(@RequestBody IntegrationRequest request) {

        Map<String, Object> response = service.getVehicleInfo(request.getClaimNo());
       
        return ResponseEntity.ok(response);
    }
}
