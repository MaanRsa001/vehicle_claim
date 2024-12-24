package com.maan.veh.claim.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimsDetailsResponseDto {

    @JsonProperty("hasError")
    private boolean hasError;

    @JsonProperty("status")
    private int status;

    @JsonProperty("data")
    private ClaimsData data;

    @JsonProperty("message")
    private String message;

    @Data
    public static class ClaimsData {

        @JsonProperty("hasError")
        private boolean hasError;

        @JsonProperty("statusCode")
        private int statusCode;

        @JsonProperty("errorDetailsList")
        private List<String> errorDetailsList; // Assuming errorDetailsList is a list of strings, adjust as needed

        @JsonProperty("claimsDetailsList")
        private List<ClaimDetails> claimsDetailsList;
    }

    @Data
    public static class ClaimDetails {

        @JsonProperty("accidentNumber")
        private String accidentNumber;

        @JsonProperty("policyInceptionDate")
        private String policyInceptionDate;

        @JsonProperty("policyExpiryDate")
        private String policyExpiryDate;

        @JsonProperty("accidentLocation")
        private String accidentLocation;

        @JsonProperty("claimNotificationNo")
        private String claimNotificationNo;

        @JsonProperty("faultPercentage")
        private String faultPercentage;

        @JsonProperty("intimationDate")
        private String intimationDate;

        @JsonProperty("claimType")
        private String claimType;

        @JsonProperty("lossDate")
        private String lossDate;

        @JsonProperty("productId")
        private String productId;

        @JsonProperty("natureOfLoss")
        private String natureOfLoss;

        @JsonProperty("claimNo")
        private String claimNo;

        @JsonProperty("fnolNo")
        private String fnolNo;

        @JsonProperty("driverName")
        private String driverName;

        @JsonProperty("lossRemarks")
        private String lossRemarks;

        @JsonProperty("policyNumber")
        private String policyNumber;

        @JsonProperty("totalLossYN")
        private String totalLossYN;

        @JsonProperty("claimantType")
        private String claimantType;

        @JsonProperty("insuredName")
        private String insuredName;

        @JsonProperty("caseNumber")
        private String caseNumber;

        @JsonProperty("claimStatus")
        private String claimStatus;

        @JsonProperty("causeOfLoss")
        private String causeOfLoss;

        @JsonProperty("officeCode")
        private String officeCode;

        @JsonProperty("lobName")
        private String lobName;

        @JsonProperty("garageCode")
        private String garageCode;
    }
}
