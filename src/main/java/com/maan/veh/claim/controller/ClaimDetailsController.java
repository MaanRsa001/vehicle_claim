package com.maan.veh.claim.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.veh.claim.service.ClaimDetailsService;

@RestController
@RequestMapping("/api/claim")
public class ClaimDetailsController {

    @Autowired
    private ClaimDetailsService claimDetailsService;


}

