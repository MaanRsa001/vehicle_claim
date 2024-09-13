package com.maan.veh.claim.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class DamageSectionDetailsSaveReq {

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

    @JsonProperty("NoOfParts")
    private String noOfParts;

    @JsonProperty("GaragePrice")
    private String garagePrice;

    @JsonProperty("DealerPrice")
    private String dealerPrice;

    @JsonProperty("GarageLoginId")
    private String garageLoginId;

    @JsonProperty("DealerLoginId")
    private String dealerLoginId;

    @JsonProperty("SurveyorId")
    private String surveyorId;

    @JsonProperty("ReplaceCost")
    private String replaceCost;

    @JsonProperty("ReplaceCostDeduct")
    private String replaceCostDeduct;

    @JsonProperty("SparepartDeprection")
    private String sparepartDeprection;

    @JsonProperty("DiscountSparepart")
    private String discountSparepart;

    @JsonProperty("TotamtReplace")
    private String totamtReplace;

    @JsonProperty("LabourCost")
    private String labourCost;

    @JsonProperty("LabourCostDeduct")
    private String labourCostDeduct;

    @JsonProperty("LabourDisc")
    private String labourDisc;

    @JsonProperty("TotamtOfLabour")
    private String totamtOfLabour;

    @JsonProperty("TotPrice")
    private String totPrice;

    @JsonProperty("Status")
    private String status;

}