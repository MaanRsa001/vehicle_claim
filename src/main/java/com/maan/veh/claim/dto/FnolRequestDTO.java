package com.maan.veh.claim.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FnolRequestDTO {
	
    @JsonProperty("RequestMetaData")
    private FnolRequestDTOMetaData requestMetaData;

    @JsonProperty("CustomerId")
    private String customerId;
    @JsonProperty("PolicyNo")
    private String policyNo;

    @JsonProperty("FnolNo")
    private String fnolNo;

    @JsonProperty("LossDate")
    private String lossDate;

    
}

