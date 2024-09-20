package com.maan.veh.claim.request;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.Date;

@Data
public class ClaimDetailsSaveRequest {

    @JsonProperty("ClaimReferenceNo")
    private String claimReferenceNo;

    @JsonProperty("ClaimNo")
    private String claimNo;

    @JsonProperty("PolicyNo")
    private String policyNo;

    @JsonProperty("InsuredId")
    private String insuredId;
    
    @JsonFormat(pattern="dd/MM/yyyy")
    @JsonProperty("LossDate")
    private Date lossDate;
    
    @JsonFormat(pattern="dd/MM/yyyy")
    @JsonProperty("IntimatedDate")
    private Date intimatedDate;

    @JsonProperty("LossLocation")
    private String lossLocation;

    @JsonProperty("PoliceStation")
    private String policeStation;

    @JsonProperty("PoliceReportNo")
    private String policeReportNo;

    @JsonProperty("LossDescription")
    private String lossDescription;

    @JsonProperty("AtFault")
    private String atFault;

    @JsonProperty("PolicyPeriod")
    private String policyPeriod;

    @JsonProperty("ContactPersonPhoneNo")
    private String contactPersonPhoneNo;

    @JsonProperty("ContactPersonPhoneCode")
    private String contactPersonPhoneCode;

    @JsonProperty("PolicyReferenceNo")
    private String policyReferenceNo;

    @JsonProperty("PolicyICReferenceNo")
    private String policyICReferenceNo;

    @JsonProperty("ClaimRequestReference")
    private String claimRequestReference;

    @JsonProperty("ClaimCategory")
    private String claimCategory;

    @JsonProperty("CreatedUser")
    private String createdUser;

    @JsonProperty("ClaimType")
    private Integer claimType;

    @JsonProperty("AccidentNumber")
    private String accidentNumber;

    @JsonProperty("IsThirdPartyInvolved")
    private String isThirdPartyInvolved;

    // Driver Details
    @JsonProperty("DriverEmiratesId")
    private String driverEmiratesId;

    @JsonProperty("DriverLicenseNumber")
    private String driverLicenseNumber;
    
    @JsonFormat(pattern="dd/MM/yyyy")
    @JsonProperty("DriverDob")
    private Date driverDob;
}

