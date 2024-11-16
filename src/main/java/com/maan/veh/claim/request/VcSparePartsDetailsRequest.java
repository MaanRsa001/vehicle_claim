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

    @JsonProperty("ReplacementCost")
    private String replacementCost;

    @JsonProperty("ReplacementCostDeductible")
    private String replacementCostDeductible;

    @JsonProperty("SparePartDepreciation")
    private String sparePartDepreciation;

    @JsonProperty("DiscountOnSpareParts")
    private String discountOnSpareParts;

    @JsonProperty("TotalAmountReplacement")
    private String totalAmountReplacement;

    @JsonProperty("RepairLabour")
    private String repairLabour;

    @JsonProperty("RepairLabourDeductible")
    private String repairLabourDeductible;

    @JsonProperty("RepairLabourDiscountAmount")
    private String repairLabourDiscountAmount;

    @JsonProperty("TotalAmountRepairLabour")
    private String totalAmountRepairLabour;

    @JsonProperty("NetAmount")
    private String netAmount;

    @JsonProperty("UnknownAccidentDeduction")
    private String unknownAccidentDeduction;

    @JsonProperty("AmountToBeRecovered")
    private String amountToBeRecovered;

    @JsonProperty("TotalAfterDeductions")
    private String totalAfterDeductions;

    @JsonProperty("VatRatePer")
    private String vatRatePer;

    @JsonProperty("VatRate")
    private String vatRate;

    @JsonProperty("VatAmount")
    private String vatAmount;

    @JsonProperty("TotalWithVAT")
    private String totalWithVAT;
    
    @JsonProperty("SalvageDeduction")
    private String salvageDeduction;

}

