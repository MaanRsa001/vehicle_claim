package com.maan.veh.claim.service;

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

public interface ExternalApiService {

	CommonResponse createFnol(SaveClaimRequest requestPayload);

	CommonResponse findFNOL(FnolRequest request);

	CommonResponse getFnolStatus(ClaimTransactionRequest request);
	
	CommonResponse authenticateUser(LoginRequest request);

	CommonResponse getClaimByPolicy(GetClaimRequest request);

	CommonResponse getAllClaims();

	CommonResponse getClaimListing(ClaimListRequest requestPayload);

	CommonResponse saveSpareParts(SaveSparePartsDTO requestPayload);

	CommonResponse getSavedSpareParts(SaveSparePartsDTO requestPayload);

	CommonResponse checkClaimStatus(CheckClaimStatusRequest requestPayload);

	CommonResponse listClaimantCoverages(ClaimentCoverageRequest requestPayload);
	
	CommonResponse getSavedGarageSpareParts(SaveSparePartsDTO requestPayload);

	CommonResponse lpoSyncView(LpoApprovalSyncRequest requestPayload);

	CommonResponse getGarageWorkOrder(GetGarageWorkOrderRequest requestPayload);

	CommonResponse getGarageSettlement(GarageSettlementListRequest requestPayload);

	CommonResponse completeWorkOrder(GetGarageWorkOrderRequest requestPayload);

	CommonResponse uplodDocument(GetGarageWorkOrderRequest requestPayload);

	CommonResponse getUploadFileList(DownloadDocumentRequest requestPayload);

	CommonResponse downloadDoc(DownloadDocumentRequest requestPayload);

	CommonResponse getUploadImpFileList(DownloadDocumentRequest requestPayload);

}
