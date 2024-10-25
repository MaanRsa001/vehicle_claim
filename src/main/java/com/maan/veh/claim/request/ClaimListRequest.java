package com.maan.veh.claim.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimListRequest {

    @JsonProperty("LobCode")
    private String lobCode;

    @JsonProperty("ProdCode")
    private String prodCode;

    @JsonProperty("PolicyNumber")
    private String policyNumber;

    @JsonProperty("ClaimNotificationNumber")
    private String claimNotificationNumber;

    @JsonProperty("CreatedBy")
    private String createdBy;

    @JsonProperty("ClaimNotificationFromDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date claimNotificationFromDate;

    @JsonProperty("ClaimNotificationToDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date claimNotificationToDate;

    @JsonProperty("ClaimLossFromDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date claimLossFromDate;

    @JsonProperty("ClaimLossToDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date claimLossToDate;

    @JsonProperty("RequestMetaData")
    private ClaimIntimationRequestMetaData requestMetaData;

}

