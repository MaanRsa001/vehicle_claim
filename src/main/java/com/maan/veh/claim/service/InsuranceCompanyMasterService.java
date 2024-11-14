package com.maan.veh.claim.service;

import com.maan.veh.claim.request.InsuranceCompanyMasterRequest;
import com.maan.veh.claim.response.CommonResponse;

public interface InsuranceCompanyMasterService {

    CommonResponse createCompany(InsuranceCompanyMasterRequest request);

    CommonResponse getCompany(String companyId);

    CommonResponse updateCompany(String companyId, InsuranceCompanyMasterRequest request);

    CommonResponse deleteCompany(String companyId);
}
