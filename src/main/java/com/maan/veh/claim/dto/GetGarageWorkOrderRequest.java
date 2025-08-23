package com.maan.veh.claim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GetGarageWorkOrderRequest {

	@JsonProperty("PartyId")
	 private String partyId;
	
	 @JsonProperty("CategoryId")
	 private String categoryId;
	   
	 @JsonProperty("ProdId")
	 private String prodId;
	 
	 @JsonProperty("Companyid")
	 private int companyid;
	 
	 @JsonProperty("Garageid")
	 private String  garageid;
	 
	 @JsonProperty("ClaimNo")
	 private String  claimNo;
	 
	 @JsonProperty("QuoteStatus")
	 private String  quoteStatus;
	 
	 @JsonProperty("AmndVersionId")
	 private String AmndVersionId;
}
