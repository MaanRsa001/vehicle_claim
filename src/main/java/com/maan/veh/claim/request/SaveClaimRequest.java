package com.maan.veh.claim.request;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SaveClaimRequest {
	
    @JsonProperty("RequestMetaData")
    private ClaimIntimationRequestMetaData requestMetaData;

    @JsonProperty("LanguageCode")
    private String languageCode;

    @JsonProperty("PolicyNo")
    private String policyNo;

    @JsonProperty("InsuredId")
    private String insuredId;

    @JsonProperty("LossDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date lossDate;

    @JsonProperty("IntimatedDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private Date intimatedDate;

    @JsonProperty("NatureOfLoss")
    private String natureOfLoss;
    
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

    @JsonProperty("Driver")
    private ClaimIntimationDriver driver;

    @JsonProperty("AttachmentDetails")
    private ClaimIntimationAttachmentDetails attachmentDetails;

    @JsonProperty("CreatedUser")
    private String createdUser;

    @JsonProperty("ClaimType")
    private String claimType;

    @JsonProperty("AccidentNumber")
    private String accidentNumber;

    @JsonProperty("IsThirdPartyInvolved")
    private String isThirdPartyInvolved;

    @JsonProperty("ThirdPartyInfo")
    private List<ClaimIntimationThirdPartyInfo> thirdPartyInfo;
 
}

