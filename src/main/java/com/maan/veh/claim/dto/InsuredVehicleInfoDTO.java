package com.maan.veh.claim.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class InsuredVehicleInfoDTO {

	 @JsonProperty("partyId")
	 private String partyId;
	
	 @JsonProperty("categoryId")
	 private String categoryId;
	   
	 @JsonProperty("prodId")
	 private String prodId;
	 

}
