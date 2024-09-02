package com.maan.veh.claim.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.veh.claim.request.DamageSectionDetailsRequest;
import com.maan.veh.claim.response.DamageSectionDetailsResponse;
import com.maan.veh.claim.service.DamageSectionDetailsService;

@RestController
@RequestMapping("/api/damagedetails")
public class DamageSectionDetailsController {

    @Autowired
    private DamageSectionDetailsService service;

    @PostMapping("/byclaimno")
    public ResponseEntity<List<DamageSectionDetailsResponse>> getDamageDetailsByClaimNo(
            @RequestBody DamageSectionDetailsRequest request) {
        List<DamageSectionDetailsResponse> response = service.getDamageDetailsByClaimNo(request);
        return ResponseEntity.ok(response);
    }
    
    
}
