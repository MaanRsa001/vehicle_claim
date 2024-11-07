package com.maan.veh.claim.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SaveSparePartsRequestMetaData {
	
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
    private String requestGeneratedDateTime;

    @JsonProperty("consumerTrackingID")
    private String consumerTrackingID;
}
