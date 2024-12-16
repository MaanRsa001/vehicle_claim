package com.maan.veh.claim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GarageClaimListDataDto {
    @JsonProperty("partyId")
    private String partyId;

    @JsonProperty("fnolSgsId")
    private int fnolSgsId;

    @JsonProperty("policyNo")
    private String policyNo;

    @JsonProperty("customerId")
    private String customerId;

    @JsonProperty("insuredId")
    private String insuredId;

    @JsonProperty("make")
    private String make;

    @JsonProperty("makeKey")
    private String makeKey;

    @JsonProperty("model")
    private String model;

    @JsonProperty("modelKey")
    private String modelKey;

    @JsonProperty("year")
    private String year;

    @JsonProperty("chassisNo")
    private String chassisNo;

    @JsonProperty("vehRegNo")
    private String vehRegNo;

    @JsonProperty("bodyType")
    private String bodyType;

    @JsonProperty("insuredName")
    private String insuredName;

    @JsonProperty("claimNo")
    private String claimNo;

    @JsonProperty("prodId")
    private String prodId;

    @JsonProperty("riskId")
    private String riskId;

    @JsonProperty("fnolNo")
    private String fnolNo;

    @JsonProperty("lossLocation")
    private String lossLocation;

    @JsonProperty("claimStatus")
    private String claimStatus;
}
