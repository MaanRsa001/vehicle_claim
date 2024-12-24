package com.maan.veh.claim.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CheckClaimStatusRequest {
	
	 @JsonProperty("CustomerId")
	 private String customerId;

	 @JsonProperty("BrokerId")
	 private String brokerId;

	 @JsonProperty("ClaimNo")
	 private String claimNo;

	 @JsonProperty("FnolNo")
	 private String fnolNo;
}
