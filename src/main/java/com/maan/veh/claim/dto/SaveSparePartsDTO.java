package com.maan.veh.claim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SaveSparePartsDTO {
	
	@JsonProperty("ClaimNo")
    private String claimNo;
	
	@JsonProperty("WorkOrderNo")
    private String workOrderNo;
}
