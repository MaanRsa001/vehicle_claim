package com.maan.veh.claim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimsDetailsRequestDto {

	 @JsonProperty("customerId")
	 private String customerId;

	 @JsonProperty("brokerId")
	 private String brokerId;

	 @JsonProperty("claimNo")
	 private String claimNo;

	 @JsonProperty("fnolNo")
	 private String fnolNo;
}
