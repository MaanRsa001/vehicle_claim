package com.maan.veh.claim.master;

import com.maan.veh.claim.request.InsuranceCompanyMasterRequest;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.InsuranceCompanyMasterResponse;
import com.maan.veh.claim.service.InsuranceCompanyMasterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/insurance-company")
public class InsuranceCompanyMasterController {

    @Autowired
    private InsuranceCompanyMasterService insuranceCompanyMasterService;

    @PostMapping("/save")
    public CommonResponse createCompany(@RequestBody InsuranceCompanyMasterRequest request) {
        return insuranceCompanyMasterService.createCompany(request);
    }

    @GetMapping("/{companyId}")
    public CommonResponse getCompany(@PathVariable String companyId) {
        return insuranceCompanyMasterService.getCompany(companyId);
    }

    @PutMapping("/{companyId}")
    public CommonResponse updateCompany(@PathVariable String companyId, @RequestBody InsuranceCompanyMasterRequest request) {
        return insuranceCompanyMasterService.updateCompany(companyId, request);
    }

    @DeleteMapping("/{companyId}")
    public CommonResponse deleteCompany(@PathVariable String companyId) {
        return insuranceCompanyMasterService.deleteCompany(companyId);
    }
}
