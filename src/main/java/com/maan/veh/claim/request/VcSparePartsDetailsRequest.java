package com.maan.veh.claim.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VcSparePartsDetailsRequest {

    @JsonProperty("ClaimNo")
    private String claimNo;

    @JsonProperty("QuotationNo")
    private String quotationNo;

    @JsonProperty("GarageId")
    private String garageId;

    @JsonProperty("SparePartType")
    private String sparePartType;

    @JsonProperty("SparePartTypeDesc")
    private String sparePartTypeDesc;

    @JsonProperty("OriginalDiscount")
    private String originalDiscount;

    @JsonProperty("DiscountPercentage")
    private String discountPercentage;

    @JsonProperty("DiscountAmount")
    private String discountAmount;

    @JsonProperty("ReplacementCostDeductible")
    private String replacementCostDeductible;

    @JsonProperty("DamageType")
    private String damageType;

    @JsonProperty("DepreciationType")
    private String depreciationType;

    @JsonProperty("DepreciationTypeDesc")
    private String depreciationTypeDesc;

    @JsonProperty("Depreciation")
    private String depreciation;

    @JsonProperty("ReferralStatus")
    private String referralStatus;

    @JsonProperty("RepairLabour")
    private String repairLabour;

    @JsonProperty("RepairLabourDiscount")
    private String repairLabourDiscount;

    @JsonProperty("RepairLabourDiscountAmount")
    private String repairLabourDiscountAmount;

    @JsonProperty("RepairLabourDeductible")
    private String repairLabourDeductible;

    @JsonProperty("TotalAmountRepairLabour")
    private String totalAmountRepairLabour;

    @JsonProperty("Remarks")
    private String remarks;

    @JsonProperty("DamageDirection")
    private String damageDirection;

    @JsonProperty("DamageDirectionDesc")
    private String damageDirectionDesc;

    @JsonProperty("PartType")
    private String partType;

    @JsonProperty("PartTypeDesc")
    private String partTypeDesc;

    @JsonProperty("ReplaceRepair")
    private String replaceRepair;

    @JsonProperty("NoOfUnits")
    private String noOfUnits;

    @JsonProperty("SparePartsCost")
    private String sparePartsCost;

    @JsonProperty("LabourCharge")
    private String labourCharge;

    @JsonProperty("TotalCost")
    private String totalCost;

    @JsonProperty("DamageSno")
    private String damageSno;
}
