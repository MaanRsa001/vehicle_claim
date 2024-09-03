package com.maan.veh.claim.service;

import com.maan.veh.claim.request.TotalAmountDetailsRequest;
import com.maan.veh.claim.request.TotalAmountDetailsSaveRequest;
import com.maan.veh.claim.response.CommonResponse;

public interface TotalAmountDetailsService {
	
	CommonResponse getTotalAmountDetailsByClaimNo(TotalAmountDetailsRequest req);

	CommonResponse saveTotalAmountDetails(TotalAmountDetailsSaveRequest req);

	CommonResponse getTotalAmountDetailsByCreatedBy(TotalAmountDetailsRequest request);
	
}
