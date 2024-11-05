package com.maan.veh.claim.response;

import java.text.SimpleDateFormat;
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

    @JsonProperty("hasError")
    private boolean hasError;

    @JsonProperty("statusCode")
    private int statusCode;

    @JsonProperty("policyNo")
    private String policyNo;

    @JsonProperty("productName")
    private String productName;

    @JsonProperty("claimNo")
    private String claimNo;

    @JsonProperty("claimIntimationDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date claimIntimationDate;

    @JsonProperty("claimLossDate")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss.SSS")
    private Date claimLossDate;

    @JsonProperty("lossDescription")
    private String lossDescription;

    @JsonProperty("lossLocation")
    private String lossLocation;

    @JsonProperty("productId")
    private String productId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("lobName")
    private String lobName;

    @JsonProperty("lobCode")
    private String lobCode;

    @JsonProperty("sgsId")
    private String sgsId;

    // Custom getter to return claimIntimationDate in "dd/MM/yyyy" format
    public String getClaimIntimationDate() {
        return formatDate(claimIntimationDate);
    }

    // Custom getter to return claimLossDate in "dd/MM/yyyy" format
    public String getClaimLossDate() {
        return formatDate(claimLossDate);
    }

    // Helper method to format Date
    private String formatDate(Date date) {
        if (date != null) {
            return new SimpleDateFormat("dd/MM/yyyy").format(date);
        }
        return null;
    }
}
