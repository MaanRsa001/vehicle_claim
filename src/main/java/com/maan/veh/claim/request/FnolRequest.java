package com.maan.veh.claim.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FnolRequest {
	
	@JsonProperty("jwtToken")
    private String jwtToken;
	
    @JsonProperty("RequestMetaData")
    private RequestMetaData requestMetaData;

    @JsonProperty("CustomerId")
    private String customerId;

    @JsonProperty("PolicyNo")
    private String policyNo;

    @JsonProperty("FnolNo")
    private String fnolNo;

    @JsonProperty("LossDate")
    private String lossDate;

    @Data
    public static class RequestMetaData {

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
        private String requestGeneratedDateTime;

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
}
