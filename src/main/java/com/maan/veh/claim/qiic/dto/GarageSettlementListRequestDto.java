package com.maan.veh.claim.qiic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  // Generates a no-args constructor
@AllArgsConstructor // Generates a constructor with all fields
public class GarageSettlementListRequestDto {
	
	@JsonProperty("partyId")
	private String partyId;
	
	@JsonProperty("claimNo")
	private String claimNo;
}
