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
    private int noUnits;

    @JsonProperty("unitPrice")
    private BigDecimal unitPrice;
    
    @JsonProperty("deductibleAmount")
    private BigDecimal deductibleAmount;
    
    @JsonProperty("deductiblePer")
    private BigDecimal deductiblePer;
    
    @JsonProperty("asPerInvoice")
    private String asPerInvoice;

    @JsonProperty("replacementCharge")
    private BigDecimal replacementCharge;
    
    @JsonProperty("beforeDeduction")
    private BigDecimal beforeDeduction;

    @JsonProperty("total")
    private BigDecimal total;
}
