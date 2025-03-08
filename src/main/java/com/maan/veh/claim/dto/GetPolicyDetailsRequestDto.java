package com.maan.veh.claim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class GetPolicyDetailsRequestDto {

    @JsonProperty("requestMetaData")
    private RequestMetaDataDTO requestMetaData;

    @JsonProperty("policyNo")
    private String policyNo;

    @JsonProperty("productId")
    private String productId;

    @JsonProperty("transactionType")
    private String transactionType;

    @JsonProperty("cobCode")
    private String cobCode;

    @Data
    public static class RequestMetaDataDTO {

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
        private String requestGeneratedDateTime; // Keeping it as a String

        @JsonProperty("consumerTrackingID")
        private String consumerTrackingID;
    }
}
