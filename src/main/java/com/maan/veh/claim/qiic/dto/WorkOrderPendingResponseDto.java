package com.maan.veh.claim.qiic.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class WorkOrderPendingResponseDto {
	
	@JsonProperty("hasError")
    private boolean hasError;

    @JsonProperty("status")
    private int status;
    
    @JsonProperty("dataset")
    private List<WorkOrderDetailResponseDto> data;

    
    @JsonProperty("message")
    private String message;
}
