package com.maan.veh.claim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor  // Generates a no-args constructor
@AllArgsConstructor // Generates a constructor with all fields
public class InsuredVehicleInfoDTO {

	 @JsonProperty("partyId")
	 private String partyId;
	
	 @JsonProperty("categoryId")
	 private String categoryId;
	   
	 @JsonProperty("prodId")
	 private String prodId;
	 

}
