package com.maan.veh.claim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FilterGarageReq {

	
	@JsonProperty("GarageId")
	private String garageId;
	
	@JsonProperty("ClaimNo")
	private String claimNo;
	
	@JsonProperty("SurveyorId")
	private String surveyorId;
	
	
}
