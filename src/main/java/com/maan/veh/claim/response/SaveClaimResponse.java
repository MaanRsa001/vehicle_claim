package com.maan.veh.claim.response;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class SaveClaimResponse {

    @JsonProperty("ErrorDetailsList")
    private List<ErrorDetail> errorDetailsList;

    @JsonProperty("HasError")
    private String hasError;

    @JsonProperty("StatusCode")
    private String statusCode;

    @JsonProperty("FnolNo")
    private String fnolNo;

    @JsonProperty("PolicyReferenceNo")
    private String policyReferenceNo;

    @JsonProperty("PolicyICReferenceNo")
    private String policyICReferenceNo;

    @JsonProperty("ClaimRequestReference")
    private String claimRequestReference;

    @JsonProperty("ClaimStatusCode")
    private String claimStatusCode;

    @JsonProperty("FnolSgsId")
    private String fnolSgsId;

    @JsonProperty("ClaimType")
    private String claimType;

    @JsonProperty("ClaimPartyId")
    private String claimPartyId;

    @JsonProperty("ClaimPartyName")
    private String claimPartyName;

    @Data
    public static class ErrorDetail {
        @JsonProperty("ErrorCode")
        private String errorCode;

        @JsonProperty("ErrorDescription")
        private String errorDescription;

        @JsonProperty("ErrorField")
        private String errorField;

        @JsonProperty("ErrorSection")
        private String errorSection;
    }
}

