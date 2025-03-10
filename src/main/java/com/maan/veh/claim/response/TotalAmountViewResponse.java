package com.maan.veh.claim.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TotalAmountViewResponse {
    
    @JsonProperty("ClaimNo")
    private String claimNo;

    @JsonProperty("QuotationNo")
    private String quotationNo;
    
    @JsonProperty("GarageId")
    private String garageId;

    // Spare Parts (Replacement) Details
    @JsonProperty("SparePartsCost")
    private String sparePartsCost;

    @JsonProperty("SparePartsDepreciation")
    private String sparePartsDepreciation;

    @JsonProperty("SparePartsDiscount")
    private String sparePartsDiscount;

    @JsonProperty("SparePartsDeductible")
    private String sparePartsDeductible;

    @JsonProperty("TotalAmountSpareParts")
    private String totalAmountSpareParts;

    // Repair Labour Details
    @JsonProperty("RepairLabourCost")
    private String repairLabourCost;

    @JsonProperty("RepairLabourDiscount")
    private String repairLabourDiscount;

    @JsonProperty("RepairLabourDeductible")
    private String repairLabourDeductible;

    @JsonProperty("TotalAmountRepairLabour")
    private String totalAmountRepairLabour;

    // Other Amounts
    @JsonProperty("NetAmount")
    private String netAmount;

    @JsonProperty("UnknownAccidentDeduction")
    private String unknownAccidentDeduction;

    @JsonProperty("AmountToBeRecovered")
    private String amountToBeRecovered;

    @JsonProperty("TotalAfterDeduction")
    private String totalAfterDeduction;

    // VAT Details
    @JsonProperty("VATRate")
    private String vatRate;

    @JsonProperty("VATAmount")
    private String vatAmount;

    @JsonProperty("TotalAmountWithVAT")
    private String totalAmountWithVAT;
}
