package com.maan.veh.claim.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimentCoverageRequest {
	
	@JsonProperty("ClaimNo")
    private String claimNo;
	
	@JsonProperty("GarageId")
    private String garageId;
}
