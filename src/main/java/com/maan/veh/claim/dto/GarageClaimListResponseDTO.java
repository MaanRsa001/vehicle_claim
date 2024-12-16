package com.maan.veh.claim.dto;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class GarageClaimListResponseDTO {
    @JsonProperty("hasError")
    private boolean hasError;

    @JsonProperty("status")
    private int status;

    @JsonProperty("dataset")
    private List<GarageClaimListDataDto> dataset;
    
    @JsonProperty("message")
    private String message;

}

