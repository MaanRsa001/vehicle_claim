package com.maan.veh.claim.qiic.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimDetailsViewRequest {
	
	@JsonProperty("CompanyId")
    private String companyId;
	
	@JsonProperty("GarageId")
    private String garageId;
	
	@JsonProperty("ClaimNo")
    private String claimNo;
}
