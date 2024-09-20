package com.maan.veh.claim.service;

import com.maan.veh.claim.request.ClaimDetailsSaveRequest;
import com.maan.veh.claim.response.CommonResponse;

public interface ClaimDetailsService {

	CommonResponse saveClaimDetails(ClaimDetailsSaveRequest request);

}
