package com.maan.veh.claim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class ClaimentCoverageRequestDTO {

    @JsonProperty("policyNo")
    private String policyNo;

    @JsonProperty("riskId")
    private String riskId;

    @JsonProperty("sgsId")
    private String sgsId;

    @JsonProperty("clcpClfSgsId")
    private String clcpClfSgsId;

}
