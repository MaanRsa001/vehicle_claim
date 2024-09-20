package com.maan.veh.claim.request;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class SaveClaimRequest {
	
	@JsonProperty("jwtToken")
    private String jwtToken;
	
    @JsonProperty("RequestMetaData")
    private RequestMetaData requestMetaData;

    @JsonProperty("LanguageCode")
    private String languageCode;

    @JsonProperty("PolicyNo")
    private String policyNo;

    @JsonProperty("InsuredId")
    private String insuredId;

    @JsonProperty("LossDate")
    private String lossDate;

    @JsonProperty("IntimatedDate")
    private String intimatedDate;

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
    private Driver driver;

    @JsonProperty("AttachmentDetails")
    private AttachmentDetails attachmentDetails;

    @JsonProperty("CreatedUser")
    private String createdUser;

    @JsonProperty("ClaimType")
    private String claimType;

    @JsonProperty("AccidentNumber")
    private String accidentNumber;

    @JsonProperty("IsThirdPartyInvolved")
    private String isThirdPartyInvolved;

    @JsonProperty("ThirdPartyInfo")
    private List<ThirdPartyInfo> thirdPartyInfo;

    @Data
    public static class RequestMetaData {
        @JsonProperty("ConsumerTrackingID")
        private String consumerTrackingID;

        @JsonProperty("CurrentBranch")
        private String currentBranch;

        @JsonProperty("IpAddress")
        private String ipAddress;

        @JsonProperty("OriginBranch")
        private String originBranch;

        @JsonProperty("RequestData")
        private String requestData;

        @JsonProperty("RequestGeneratedDateTime")
        private String requestGeneratedDateTime;

        @JsonProperty("RequestId")
        private String requestId;

        @JsonProperty("RequestOrigin")
        private String requestOrigin;

        @JsonProperty("RequestReference")
        private String requestReference;

        @JsonProperty("RequestedService")
        private String requestedService;

        @JsonProperty("ResponseData")
        private String responseData;

        @JsonProperty("SourceCode")
        private String sourceCode;

        @JsonProperty("UserName")
        private String userName;
    }

    @Data
    public static class Driver {
        @JsonProperty("EmiratesId")
        private String emiratesId;

        @JsonProperty("LicenseNumber")
        private String licenseNumber;

        @JsonProperty("Dob")
        private String dob; // Use proper date format or type as required
    }

    @Data
    public static class AttachmentDetails {
        @JsonProperty("DocumentDetails")
        private List<DocumentDetails> documentDetails;

        @Data
        public static class DocumentDetails {
            @JsonProperty("DocumentData")
            private String documentData;

            @JsonProperty("DocumentFormat")
            private String documentFormat;

            @JsonProperty("DocumentId")
            private String documentId;

            @JsonProperty("DocumentName")
            private String documentName;

            @JsonProperty("DocumentRefNo")
            private String documentRefNo;

            @JsonProperty("DocumentType")
            private String documentType;

            @JsonProperty("DocumentURL")
            private String documentURL;
        }
    }

    @Data
    public static class ThirdPartyInfo {
        @JsonProperty("TPDriverLiability")
        private String tpDriverLiability;

        @JsonProperty("TPDriverLicenceNo")
        private String tpDriverLicenceNo;

        @JsonProperty("TPDriverName")
        private String tpDriverName;

        @JsonProperty("TPDriverNationalityCode")
        private String tpDriverNationalityCode;

        @JsonProperty("TPDriverTrafficNo")
        private String tpDriverTrafficNo;

        @JsonProperty("TPMobileNumber")
        private String tpMobileNumber;

        @JsonProperty("TPVehicleCurrentInsurer")
        private String tpVehicleCurrentInsurer;

        @JsonProperty("TPVehicleMake")
        private String tpVehicleMake;

        @JsonProperty("TPVehicleMakeCode")
        private String tpVehicleMakeCode;

        @JsonProperty("TPVehicleModel")
        private String tpVehicleModel;

        @JsonProperty("TPVehicleModelCode")
        private String tpVehicleModelCode;

        @JsonProperty("TPVehiclePlateCode")
        private String tpVehiclePlateCode;

        @JsonProperty("TPVehiclePlateNo")
        private String tpVehiclePlateNo;

        @JsonProperty("TPVehiclePlateTypeCode")
        private String tpVehiclePlateTypeCode;

        @JsonProperty("ThirdPartyReference")
        private String thirdPartyReference;

        @JsonProperty("ThirdPartyType")
        private String thirdPartyType;
    }
}

