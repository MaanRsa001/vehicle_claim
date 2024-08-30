package com.maan.veh.claim.request;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GarageWorkOrderRequest {

    @JsonProperty("ClaimNo")
    private String claimNo;

    @JsonProperty("CreatedBy")
    private String createdBy;

}

