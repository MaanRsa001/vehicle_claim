package com.maan.veh.claim.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class VcinsuredVehicleResponseQIIC {

	@JsonProperty("hasError")
    private boolean hasError;

    @JsonProperty("status")
    private int status;
    
    @JsonProperty("dataset")
    private List<VcInuredVehicleApiResponseQIIC> data;

    
    @JsonProperty("message")
    private String message;
}
