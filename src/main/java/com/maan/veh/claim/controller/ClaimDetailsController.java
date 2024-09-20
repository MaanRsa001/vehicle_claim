package com.maan.veh.claim.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.veh.claim.request.ClaimDetailsSaveRequest;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.service.ClaimDetailsService;

@RestController
@RequestMapping("/api/claim")
public class ClaimDetailsController {

    @Autowired
    private ClaimDetailsService claimDetailsService;

    @PostMapping("/save")
    public CommonResponse saveClaimDetails(@RequestBody ClaimDetailsSaveRequest request) {
        return claimDetailsService.saveClaimDetails(request);
    }
}

