package com.maan.veh.claim.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ExternalVehicleGarageViewRequest {
	
	@JsonProperty("CompanyId")
    private String companyId;
	
	@JsonProperty("GarageId")
    private String garageId;
	
	@JsonProperty("PartyId")
    private String partyId;
	
	@JsonProperty("CategoryId")
    private String categoryId;
	
	@JsonProperty("ProdId")
    private String prodId;
}
