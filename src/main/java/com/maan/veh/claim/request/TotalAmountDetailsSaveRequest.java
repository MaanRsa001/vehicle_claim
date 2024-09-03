package com.maan.veh.claim.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class TotalAmountDetailsSaveRequest {

    @JsonProperty("ClaimNo")
    private String claimNo;

    @JsonProperty("NetAmount")
    private String netAmount;

    @JsonProperty("TotamtAftDeduction")
    private String totamtAftDeduction;

    @JsonProperty("VatRatePercent")
    private String vatRatePercent;

    @JsonProperty("VatRate")
    private String vatRate;

    @JsonProperty("VatAmount")
    private String vatAmount;

    @JsonProperty("TotamtWithVat")
    private String totamtWithVat;

    @JsonProperty("CreatedBy")
    private String createdBy;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("TotReplaceCost")
    private String totReplaceCost;

    @JsonProperty("TotLabourCost")
    private String totLabourCost;
}
