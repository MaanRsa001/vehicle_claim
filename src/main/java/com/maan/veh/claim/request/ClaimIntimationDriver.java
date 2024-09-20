package com.maan.veh.claim.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimIntimationDriver {
    @JsonProperty("EmiratesId")
    private String emiratesId;

    @JsonProperty("LicenseNumber")
    private String licenseNumber;

    @JsonProperty("Dob")
    private String dob; // Use proper date format or type as required
}
