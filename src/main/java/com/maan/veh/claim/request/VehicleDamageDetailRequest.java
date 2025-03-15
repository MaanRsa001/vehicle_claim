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

    @JsonProperty("replacementCharge")
    private String replacementCharge;

    @JsonProperty("total")
    private String total;
    
    @JsonProperty("damageTyp")
    private String damageTyp;
    
    @JsonProperty("deprect")
    private String deprect;
    
    @JsonProperty("deprectTyp")
    private String deprectTyp;
    
    @JsonProperty("discount")
    private String discount;
    
    @JsonProperty("discountAmt")
    private String discountAmt ;
    
    @JsonProperty("originalDisc")
    private String originalDisc;
    
    @JsonProperty("partAccident")
    private String partAccident;
    
    @JsonProperty("reffStatus")
    private String reffStatus;
    
    @JsonProperty("remarks")
    private String remarks;
    
    @JsonProperty("repairLabour")
    private String repairLabour;
    
    @JsonProperty("repairLabourDeduct")
    private String repairLabourDeduct;
    
    @JsonProperty("repairLabourDisc")
    private String repairLabourDisc;
    
    @JsonProperty("repairLabourDiscAmt")
    private String repairLabourDiscAmt;
    
    @JsonProperty("replaceCostDed")
    private String replaceCostDed;
    
    @JsonProperty("sparePartTyp")
    private String sparePartTyp;
    
    @JsonProperty("totalAmtRepLab")
    private String totalAmtRepLab;
    
}
