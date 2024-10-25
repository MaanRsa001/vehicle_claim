package com.maan.veh.claim.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.veh.claim.dto.ClaimIntimationDTORequestMetaData;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimListRequestDTO {

    @JsonProperty("lobCode")
    private String lobCode;

    @JsonProperty("prodCode")
    private String prodCode;

    @JsonProperty("policyNumber")
    private String policyNumber;

    @JsonProperty("claimNotificationNumber")
    private String claimNotificationNumber;

    @JsonProperty("createdBy")
    private String createdBy;

    @JsonProperty("claimNotificationFromDate")
    private String claimNotificationFromDate;

    @JsonProperty("claimNotificationToDate")
    private String claimNotificationToDate;

    @JsonProperty("claimLossFromDate")
    private String claimLossFromDate;

    @JsonProperty("claimLossToDate")
    private String claimLossToDate;

    @JsonProperty("requestMetaData")
    private ClaimIntimationDTORequestMetaData requestMetaData;

}

