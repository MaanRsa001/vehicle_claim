package com.maan.veh.claim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimIntimationDTODriver {
    @JsonProperty("emiratesId")
    private String emiratesId;

    @JsonProperty("licenseNumber")
    private String licenseNumber;

    @JsonProperty("dob")
    private String dob; // Use proper date format or type as required
}
