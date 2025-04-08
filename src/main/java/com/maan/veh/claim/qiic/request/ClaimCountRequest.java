package com.maan.veh.claim.qiic.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimCountRequest {
	
	@JsonProperty("LoginId")
    private String loginId;

    @JsonProperty("CompanyId")
    private String companyId;

    @JsonProperty("UserType")
    private String userType;

}
