package com.maan.veh.claim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GarageClaimListDto {
	
	@JsonProperty("partyId")
    private String partyId;
	
	@JsonProperty("categoryId")
    private String categoryId;
	
	@JsonProperty("prodId")
    private String prodId;
}
