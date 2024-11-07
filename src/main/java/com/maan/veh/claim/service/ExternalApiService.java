package com.maan.veh.claim.service;

import com.maan.veh.claim.dto.SaveSparePartsDTO;
import com.maan.veh.claim.request.ClaimListRequest;
import com.maan.veh.claim.request.ClaimTransactionRequest;
import com.maan.veh.claim.request.FnolRequest;
import com.maan.veh.claim.request.LoginRequest;
import com.maan.veh.claim.request.SaveClaimRequest;
import com.maan.veh.claim.response.CommonResponse;

public interface ExternalApiService {

	CommonResponse createFnol(SaveClaimRequest requestPayload);

	CommonResponse findFNOL(FnolRequest request);

	CommonResponse getFnolStatus(ClaimTransactionRequest request);
	
	CommonResponse authenticateUser(LoginRequest request);

	CommonResponse getClaimByPolicy(String policyNo);

	CommonResponse getAllClaims();

	CommonResponse getClaimListing(ClaimListRequest requestPayload);

	CommonResponse saveSpareParts(SaveSparePartsDTO requestPayload);

}
