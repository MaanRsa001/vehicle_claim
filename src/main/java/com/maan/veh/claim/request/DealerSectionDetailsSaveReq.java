package com.maan.veh.claim.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DealerSectionDetailsSaveReq {
	
	@JsonProperty("ClaimNo")
    private String claimNo;
    
    @JsonProperty("QuotationNo")
    private String quotationNo;

    @JsonProperty("DamageSno")
    private String damageSno;

    @JsonProperty("UnitPrice")
    private String unitPrice;

    @JsonProperty("DealerLoginId")
    private String dealerLoginId;

}
