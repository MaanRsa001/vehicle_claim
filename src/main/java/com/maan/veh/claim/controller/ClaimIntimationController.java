package com.maan.veh.claim.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.veh.claim.dto.SaveSparePartsDTO;
import com.maan.veh.claim.qiic.dto.DownloadDocumentRequest;
import com.maan.veh.claim.qiic.request.GarageSettlementListRequest;
import com.maan.veh.claim.qiic.request.GetGarageWorkOrderRequest;
import com.maan.veh.claim.qiic.request.LpoApprovalSyncRequest;
import com.maan.veh.claim.request.CheckClaimStatusRequest;
import com.maan.veh.claim.request.ClaimListRequest;
import com.maan.veh.claim.request.ClaimTransactionRequest;
import com.maan.veh.claim.request.ClaimentCoverageRequest;
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
        CommonResponse res = externalApiService.getClaimByPolicy(request);
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
    
    @PostMapping("/saveSpareParts")
    public ResponseEntity<CommonResponse> saveSpareParts(@RequestBody SaveSparePartsDTO requestPayload) {
        	CommonResponse res = externalApiService.saveSpareParts(requestPayload);
            return ResponseEntity.ok(res);
    }
    
    @PostMapping("/garage/saveSpareParts")
    public ResponseEntity<CommonResponse> saveGarageSpareParts(@RequestBody SaveSparePartsDTO requestPayload) {
        	CommonResponse res = externalApiService.saveSpareParts(requestPayload);
            return ResponseEntity.ok(res);
    }
    
    @PostMapping("/getSpareParts")
    public ResponseEntity<CommonResponse> getSavedSpareParts(@RequestBody SaveSparePartsDTO requestPayload) {
        	CommonResponse res = externalApiService.getSavedSpareParts(requestPayload);
            return ResponseEntity.ok(res);
    }
    
    @PostMapping("/garage/getSpareParts")
    public ResponseEntity<CommonResponse> getSavedGarageSpareParts(@RequestBody SaveSparePartsDTO requestPayload) {
        	CommonResponse res = externalApiService.getSavedGarageSpareParts(requestPayload);
            return ResponseEntity.ok(res);
    }
    
    @PostMapping("/checkClaimStatus")
    public ResponseEntity<CommonResponse> checkClaimStatus(@RequestBody CheckClaimStatusRequest requestPayload) {
        	CommonResponse res = externalApiService.checkClaimStatus(requestPayload);
            return ResponseEntity.ok(res);
    }
    
    @PostMapping("/listClaimantCoverages")
    public ResponseEntity<CommonResponse> listClaimantCoverages(@RequestBody ClaimentCoverageRequest requestPayload) {
        	CommonResponse res = externalApiService.listClaimantCoverages(requestPayload);
            return ResponseEntity.ok(res);
    }
    
    @PostMapping("/garage/lposync")
    public ResponseEntity<CommonResponse> lpoSyncView(@RequestBody LpoApprovalSyncRequest requestPayload) {
        	CommonResponse res = externalApiService.lpoSyncView(requestPayload);
            return ResponseEntity.ok(res);
    }
    
    @PostMapping("/garage/getWorkOrder")
    public ResponseEntity<CommonResponse> getGarageWorkOrder(@RequestBody GetGarageWorkOrderRequest requestPayload) {
        	CommonResponse res = externalApiService.getGarageWorkOrder(requestPayload);
            return ResponseEntity.ok(res);
    }
    
    @PostMapping("/garage/settlement")
    public ResponseEntity<CommonResponse> getGarageSettlement(@RequestBody GarageSettlementListRequest requestPayload) {
        	CommonResponse res = externalApiService.getGarageSettlement(requestPayload);
            return ResponseEntity.ok(res);
    }
    
    @PostMapping("/garage/completeWorkOrder")
    public ResponseEntity<CommonResponse> completeWorkOrder(@RequestBody GetGarageWorkOrderRequest requestPayload) {
        	CommonResponse res = externalApiService.completeWorkOrder(requestPayload);
            return ResponseEntity.ok(res);
    }
    
    @PostMapping("/garage/uploadDoc")
    public ResponseEntity<CommonResponse> uplodDocument(@RequestBody GetGarageWorkOrderRequest requestPayload) {
        	CommonResponse res = externalApiService.uplodDocument(requestPayload);
            return ResponseEntity.ok(res);
    }
    
    @PostMapping("/garage/getUploadFileList")
    public ResponseEntity<CommonResponse> getUploadFileList(@RequestBody DownloadDocumentRequest requestPayload) {
        	CommonResponse res = externalApiService.getUploadFileList(requestPayload);
            return ResponseEntity.ok(res);
    }
    
    @PostMapping("/garage/DownloadDoc")
    public ResponseEntity<CommonResponse> downloadDoc(@RequestBody DownloadDocumentRequest requestPayload) {
        	CommonResponse res = externalApiService.downloadDoc(requestPayload);
            return ResponseEntity.ok(res);
    }
    
    @PostMapping("/getPolicyDetails")
    public ResponseEntity<CommonResponse> getPolicyDetails(@RequestBody GetClaimRequest request) {
        CommonResponse res = externalApiService.getPolicyDetails(request);
        return ResponseEntity.ok(res);
    }
}
