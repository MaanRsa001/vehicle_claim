package com.maan.veh.claim.service;

import java.util.List;

import com.maan.veh.claim.request.DamageSectionDetailsRequest;
import com.maan.veh.claim.request.DamageSectionDetailsSaveReq;
import com.maan.veh.claim.request.DealerSectionDetailsSaveReq;
import com.maan.veh.claim.request.GarageSectionDetailsSaveReq;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.DamageSectionDetailsResponse;

public interface DamageSectionDetailsService {
    List<DamageSectionDetailsResponse> getDamageDetailsByClaimNo(DamageSectionDetailsRequest request);

	CommonResponse saveDamageSectionDetails(List<DamageSectionDetailsSaveReq> req);

	CommonResponse saveGarageDamageSectionDetails(List<GarageSectionDetailsSaveReq> req);

	CommonResponse saveDealerDamageSectionDetails(List<DealerSectionDetailsSaveReq> req);
}
