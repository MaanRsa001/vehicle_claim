package com.maan.veh.claim.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class CheckClaimStatusResponse {
	
	@JsonProperty("AccidentNumber")
    private String accidentNumber;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonProperty("PolicyInceptionDate")
    private Date policyInceptionDate;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonProperty("PolicyExpiryDate")
    private Date policyExpiryDate;

    @JsonProperty("AccidentLocation")
    private String accidentLocation;

    @JsonProperty("ClaimNotificationNo")
    private String claimNotificationNo;

    @JsonProperty("FaultPercentage")
    private String faultPercentage;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonProperty("IntimationDate")
    private Date intimationDate;

    @JsonProperty("ClaimType")
    private String claimType;
    
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    @JsonProperty("LossDate")
    private Date lossDate;

    @JsonProperty("ProductId")
    private String productId;

    @JsonProperty("NatureOfLoss")
    private String natureOfLoss;

    @JsonProperty("ClaimNo")
    private String claimNo;

    @JsonProperty("FnolNo")
    private String fnolNo;

    @JsonProperty("DriverName")
    private String driverName;

    @JsonProperty("LossRemarks")
    private String lossRemarks;

    @JsonProperty("PolicyNumber")
    private String policyNumber;

    @JsonProperty("TotalLossYN")
    private String totalLossYN;

    @JsonProperty("ClaimantType")
    private String claimantType;

    @JsonProperty("InsuredName")
    private String insuredName;

    @JsonProperty("CaseNumber")
    private String caseNumber;

    @JsonProperty("ClaimStatus")
    private String claimStatus;

    @JsonProperty("CauseOfLoss")
    private String causeOfLoss;

    @JsonProperty("OfficeCode")
    private String officeCode;

    @JsonProperty("LobName")
    private String lobName;

    @JsonProperty("GarageCode")
    private String garageCode;
}
