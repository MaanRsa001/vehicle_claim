package com.maan.veh.claim.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
	
@Data
public class GarageSectionDetailsSaveReq {
	
	@JsonProperty("ClaimNo")
    private String claimNo;
    
    @JsonProperty("QuotationNo")
    private String quotationNo;

    @JsonProperty("DamageSno")
    private String damageSno;

    @JsonProperty("DamageDirection")
    private String damageDirection;

    @JsonProperty("DamagePart")
    private String damagePart;

    @JsonProperty("RepairReplace")
    private String repairReplace;

    @JsonProperty("NoOfUnits")
    private String noOfUnits;

    @JsonProperty("UnitPrice")
    private String unitPrice;
    
    @JsonProperty("ReplacementCharge")
    private String replacementCharge;

    @JsonProperty("GarageLoginId")
    private String garageLoginId;

    @JsonProperty("TotalPrice")
    private String totalPrice;

}
