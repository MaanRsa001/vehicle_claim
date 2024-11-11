package com.maan.veh.claim.file;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetDocListReq {
	
	@JsonProperty("ClaimNo")
	private String claimNo;
	
	@JsonProperty("DocumentRef")
	private String documentRef;
	
}
