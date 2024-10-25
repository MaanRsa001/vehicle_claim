package com.maan.veh.claim.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimData {

    @JsonProperty("HasError")
    private String hasError;

    @JsonProperty("StatusCode")
    private String statusCode;

    @JsonProperty("PolicyNo")
    private String policyNo;

    @JsonProperty("ProductName")
    private String productName;

    @JsonProperty("ClaimNo")
    private String claimNo;

    @JsonProperty("ClaimIntimationDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date claimIntimationDate;

    @JsonProperty("ClaimLossDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date claimLossDate;

    @JsonProperty("LossDescription")
    private String lossDescription;

    @JsonProperty("LossLocation")
    private String lossLocation;

    @JsonProperty("ProductId")
    private String productId;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("LobName")
    private String lobName;

    @JsonProperty("LobCode")
    private String lobCode;

    @JsonProperty("SgsId")
    private String sgsId;
}
