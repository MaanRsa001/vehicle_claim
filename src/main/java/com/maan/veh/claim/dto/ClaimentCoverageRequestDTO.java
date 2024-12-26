package com.maan.veh.claim.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;


@Data
public class ClaimentCoverageRequestDTO {
	
	@JsonProperty("requestMetaData")
    private RequestMetaData requestMetaData;

    @JsonProperty("policyNo")
    private String policyNo;

    @JsonProperty("riskId")
    private String riskId;

    @JsonProperty("sgsId")
    private String sgsId;

    @JsonProperty("clcpClfSgsId")
    private String clcpClfSgsId;

    @Data
    public static class RequestMetaData {

        @JsonProperty("requestOrigin")
        private String requestOrigin;

        @JsonProperty("currentBranch")
        private String currentBranch;

        @JsonProperty("originBranch")
        private String originBranch;

        @JsonProperty("userName")
        private String userName;

        @JsonProperty("ipAddress")
        private String ipAddress;

        @JsonProperty("requestGeneratedDateTime")
        private LocalDateTime requestGeneratedDateTime;

        @JsonProperty("consumerTrackingID")
        private String consumerTrackingID;
    }
}
