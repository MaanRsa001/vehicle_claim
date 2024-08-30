package com.maan.veh.claim.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VehicleInfoRequest {

    @JsonProperty("CompanyId")
    private String companyId;
}
