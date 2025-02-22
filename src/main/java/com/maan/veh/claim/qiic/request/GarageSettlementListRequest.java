package com.maan.veh.claim.qiic.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GarageSettlementListRequest {
	
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
}
