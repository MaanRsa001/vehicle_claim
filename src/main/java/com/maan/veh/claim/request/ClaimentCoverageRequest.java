package com.maan.veh.claim.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimentCoverageRequest {
	
	@JsonProperty("PolicyNo")
    private String PolicyNo;

    @JsonProperty("RiskId")
    private String RiskId;

    @JsonProperty("SgsId")
    private String SgsId;

    @JsonProperty("ClcpClfSgsId")
    private String ClcpClfSgsId;
}
