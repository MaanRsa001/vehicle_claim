package com.maan.veh.claim.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class InsuranceCompanyMasterResponse {

    @JsonProperty("CompanyId")
    private String companyId;

    @JsonProperty("AmendId")
    private String amendId;

    @JsonProperty("EffectiveDateStart")
    private String effectiveDateStart;

    @JsonProperty("EffectiveDateEnd")
    private String effectiveDateEnd;

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

    @JsonProperty("EntryDate")
    private String entryDate;

    @JsonProperty("Remarks")
    private String remarks;

    @JsonProperty("CoreAppCode")
    private String coreAppCode;

    @JsonProperty("CreatedBy")
    private String createdBy;

    @JsonProperty("UpdatedBy")
    private String updatedBy;

    @JsonProperty("UpdatedDate")
    private String updatedDate;

    @JsonProperty("CountryId")
    private String countryId;

    @JsonProperty("CityId")
    private String cityId;

    @JsonProperty("POBox")
    private String pOBox;
}
