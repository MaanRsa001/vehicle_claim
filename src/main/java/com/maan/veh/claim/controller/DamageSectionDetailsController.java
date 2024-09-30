package com.maan.veh.claim.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.veh.claim.request.DamageSectionDetailsRequest;
import com.maan.veh.claim.request.DamageSectionDetailsSaveReq;
import com.maan.veh.claim.request.DealerSectionDetailsSaveReq;
import com.maan.veh.claim.request.GarageSectionDetailsSaveReq;
import com.maan.veh.claim.response.CommonResponse;
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
    
    @PostMapping("/save")
    public ResponseEntity<CommonResponse> saveDamageSectionDetails(@RequestBody List<DamageSectionDetailsSaveReq> req) {
        CommonResponse response = service.saveDamageSectionDetails(req);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/garagesave")
    public ResponseEntity<CommonResponse> saveGarageDamageSectionDetails(@RequestBody List<GarageSectionDetailsSaveReq> req) {
        CommonResponse response = service.saveGarageDamageSectionDetails(req);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/garageview")
    public ResponseEntity<CommonResponse> viewGarageDamageSectionDetails(@RequestBody GarageSectionDetailsSaveReq req) {
        CommonResponse response = service.viewGarageDamageSectionDetails(req);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/garagetotalview")
    public ResponseEntity<CommonResponse> viewGarageTotalDamageSectionDetails(@RequestBody GarageSectionDetailsSaveReq req) {
        CommonResponse response = service.viewGarageTotalDamageSectionDetails(req);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/dealersave")
    public ResponseEntity<CommonResponse> saveDealerDamageSectionDetails(@RequestBody List<DealerSectionDetailsSaveReq> req) {
        CommonResponse response = service.saveDealerDamageSectionDetails(req);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    
    @PostMapping("/dealerview")
    public ResponseEntity<CommonResponse> viewDealerDamageSectionDetails(@RequestBody GarageSectionDetailsSaveReq req) {
        CommonResponse response = service.viewDealerDamageSectionDetails(req);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
