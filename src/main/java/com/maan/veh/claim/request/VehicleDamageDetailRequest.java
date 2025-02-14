package com.maan.veh.claim.request;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class VehicleDamageDetailRequest {
	
    @JsonProperty("damageDirection")
    private String damageDirection;

    @JsonProperty("partyType")
    private String partyType;

    @JsonProperty("replaceOrRepair")
    private String replaceOrRepair;

    @JsonProperty("noUnits")
    private String noUnits;

    @JsonProperty("unitPrice")
    private String unitPrice;
    
    @JsonProperty("deductibleAmount")
    private String deductibleAmount;
    
    @JsonProperty("deductiblePer")
    private String deductiblePer;
    
    @JsonProperty("asPerInvoice")
    private String asPerInvoice;

    @JsonProperty("replacementCharge")
    private String replacementCharge;
    
    @JsonProperty("beforeDeduction")
    private String beforeDeduction;

    @JsonProperty("total")
    private String total;
}
