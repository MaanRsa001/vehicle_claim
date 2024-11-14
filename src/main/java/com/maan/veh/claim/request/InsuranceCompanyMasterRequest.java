package com.maan.veh.claim.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InsuranceCompanyMasterRequest {

    @JsonProperty("CompanyId")
    private String companyId;

    @JsonProperty("EffectiveDateStart")
    private String effectiveDateStart;

    @JsonProperty("CompanyName")
    private String companyName;

    @JsonProperty("CompanyAddress")
    private String companyAddress;

    @JsonProperty("CompanyEmail")
    private String companyEmail;

    @JsonProperty("CompanyPhone")
    private String companyPhone;

    @JsonProperty("CompanyWebsite")
    private String companyWebsite;

    @JsonProperty("CompanyLogo")
    private String companyLogo;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Remarks")
    private String remarks;

    @JsonProperty("CoreAppCode")
    private String coreAppCode;

    @JsonProperty("CreatedBy")
    private String createdBy;

    @JsonProperty("CountryId")
    private String countryId;

    @JsonProperty("CityId")
    private String cityId;

    @JsonProperty("POBox")
    private String pOBox;
}
