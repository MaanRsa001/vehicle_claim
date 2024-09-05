package com.maan.veh.claim.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class IntegrationRequest {

	@JsonProperty("ClaimNo")
    private List<String> claimNo;

}
