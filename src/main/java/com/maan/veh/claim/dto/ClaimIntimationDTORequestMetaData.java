package com.maan.veh.claim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimIntimationDTORequestMetaData {
    @JsonProperty("consumerTrackingID")
    private String consumerTrackingID;

    @JsonProperty("currentBranch")
    private String currentBranch;

    @JsonProperty("ipAddress")
    private String ipAddress;

    @JsonProperty("originBranch")
    private String originBranch;

    @JsonProperty("requestData")
    private String requestData;

    @JsonProperty("requestGeneratedDateTime")
    private String requestGeneratedDateTime;

    @JsonProperty("requestId")
    private String requestId;

    @JsonProperty("requestOrigin")
    private String requestOrigin;

    @JsonProperty("requestReference")
    private String requestReference;

    @JsonProperty("requestedService")
    private String requestedService;

    @JsonProperty("responseData")
    private String responseData;

    @JsonProperty("sourceCode")
    private String sourceCode;

    @JsonProperty("userName")
    private String userName;
}
