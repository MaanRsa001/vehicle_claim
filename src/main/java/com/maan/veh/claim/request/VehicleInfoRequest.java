package com.maan.veh.claim.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class VehicleInfoRequest {

    @JsonProperty("CompanyId")
    private String companyId;
    
    @JsonProperty("ClaimNo")
    private String claimNo;
    
    @JsonProperty("GarageId")
    private String garageId;
    
    @JsonProperty("SparepartsDealerId")
    private String sparepartsDealerId;
}
