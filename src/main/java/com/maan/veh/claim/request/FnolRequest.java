package com.maan.veh.claim.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class FnolRequest {
	
	@JsonProperty("jwtToken")
    private String jwtToken;
	
    @JsonProperty("RequestMetaData")
    private FnolRequestMetaData requestMetaData;

    @JsonProperty("CustomerId")
    private String customerId;

    @JsonProperty("PolicyNo")
    private String policyNo;

    @JsonProperty("FnolNo")
    private String fnolNo;

    @JsonProperty("LossDate")
    private String lossDate;


}
