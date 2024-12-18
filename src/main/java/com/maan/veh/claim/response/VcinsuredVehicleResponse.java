package com.maan.veh.claim.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.veh.claim.external.ExternalApiData;
import com.maan.veh.claim.external.ExternalApiResponse;
import com.maan.veh.claim.external.VcInuredVehicleApiReponse;

import lombok.Data;

@Data
public class VcinsuredVehicleResponse {
	  @JsonProperty("hasError")
	    private boolean hasError;

	    @JsonProperty("status")
	    private int status;
	    
	    @JsonProperty("dataset")
	    private List<VcInuredVehicleApiReponse> data;

	    
	    @JsonProperty("message")
	    private String message;

}
