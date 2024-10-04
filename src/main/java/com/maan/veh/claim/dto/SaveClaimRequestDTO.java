package com.maan.veh.claim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class SaveClaimRequestDTO {
	
    @JsonProperty("requestMetaData")
    private ClaimIntimationDTORequestMetaData requestMetaData;

    @JsonProperty("languageCode")
    private String languageCode;

    @JsonProperty("policyNo")
    private String policyNo;

    @JsonProperty("insuredId")
    private String insuredId;

    @JsonProperty("lossDate")
    private String lossDate;

    @JsonProperty("intimatedDate")
    private String intimatedDate;

    @JsonProperty("lossLocation")
    private String lossLocation;

    @JsonProperty("policeStation")
    private String policeStation;

    @JsonProperty("policeReportNo")
    private String policeReportNo;

    @JsonProperty("lossDescription")
    private String lossDescription;

    @JsonProperty("atFault")
    private String atFault;

    @JsonProperty("policyPeriod")
    private String policyPeriod;

    @JsonProperty("contactPersonPhoneNo")
    private String contactPersonPhoneNo;

    @JsonProperty("contactPersonPhoneCode")
    private String contactPersonPhoneCode;

    @JsonProperty("policyReferenceNo")
    private String policyReferenceNo;

    @JsonProperty("policyICReferenceNo")
    private String policyICReferenceNo;

    @JsonProperty("claimRequestReference")
    private String claimRequestReference;

    @JsonProperty("claimCategory")
    private String claimCategory;

    @JsonProperty("driver")
    private ClaimIntimationDTODriver driver;

    @JsonProperty("attachmentDetails")
    private ClaimIntimationDTOAttachmentDetails attachmentDetails;

    @JsonProperty("createdUser")
    private String createdUser;

    @JsonProperty("claimType")
    private String claimType;

    @JsonProperty("accidentNumber")
    private String accidentNumber;

    @JsonProperty("isThirdPartyInvolved")
    private String isThirdPartyInvolved;

    @JsonProperty("thirdPartyInfo")
    private List<ClaimIntimationDTOThirdPartyInfo> thirdPartyInfo;

      
}


