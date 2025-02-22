package com.maan.veh.claim.qiic.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GarageSettlementResponseDto {
	
	@JsonProperty("hasError")
    private boolean hasError;

    @JsonProperty("status")
    private int status;
    
    @JsonProperty("dataset")
    private List<GarageSettlementListResponseDto> data;

    
    @JsonProperty("message")
    private String message;
}
