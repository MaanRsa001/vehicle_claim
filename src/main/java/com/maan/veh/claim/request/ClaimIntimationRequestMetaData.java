package com.maan.veh.claim.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimIntimationRequestMetaData {
    @JsonProperty("ConsumerTrackingID")
    private String consumerTrackingID;

    @JsonProperty("CurrentBranch")
    private String currentBranch;

    @JsonProperty("IpAddress")
    private String ipAddress;

    @JsonProperty("OriginBranch")
    private String originBranch;

    @JsonProperty("RequestData")
    private String requestData;

    @JsonProperty("RequestGeneratedDateTime")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date requestGeneratedDateTime;

    @JsonProperty("RequestId")
    private String requestId;

    @JsonProperty("RequestOrigin")
    private String requestOrigin;

    @JsonProperty("RequestReference")
    private String requestReference;

    @JsonProperty("RequestedService")
    private String requestedService;

    @JsonProperty("ResponseData")
    private String responseData;

    @JsonProperty("SourceCode")
    private String sourceCode;

    @JsonProperty("UserName")
    private String userName;
}
