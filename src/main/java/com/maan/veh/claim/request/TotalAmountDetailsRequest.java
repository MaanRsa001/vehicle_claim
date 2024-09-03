package com.maan.veh.claim.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TotalAmountDetailsRequest {

    @JsonProperty("ClaimNo")
    private String claimNo;
    
    @JsonProperty("CreatedBy")
    private String createdBy;
}
