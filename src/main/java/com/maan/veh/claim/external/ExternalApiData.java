package com.maan.veh.claim.external;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ExternalApiData {

    @JsonProperty("hasError")
    private boolean hasError;

    @JsonProperty("statusCode")
    private int statusCode;

    @JsonProperty("errorDetailsList")
    private List<ErrorDetail> errorDetailsList;

    @JsonProperty("fnolNo") 
    private String fnolNo;

    @JsonProperty("claimStatusCode") 
    private String claimStatusCode;

    @JsonProperty("claimType") 
    private String claimType;

    @JsonProperty("claimPartyId") 
    private String claimPartyId;

    @JsonProperty("claimPartyName") 
    private String claimPartyName;
    
    @JsonProperty("fnolSgsId") 
    private String fnolSgsId;
    

}

