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

    @JsonProperty("RepairReplace")
    private String repairReplace;

    @JsonProperty("ReplaceCostDeductPercentage")
    private String replaceCostDeductPercentage;

    @JsonProperty("SparepartDeprectionPercentage")
    private String sparepartDeprectionPercentage;

    @JsonProperty("DiscountSparepartPercentage")
    private String discountSparepartPercentage;

    @JsonProperty("LabourCostDeductPercentage")
    private String labourCostDeductPercentage;

    @JsonProperty("LabourDiscPercentage")
    private String labourDiscPercentage;

    @JsonProperty("GarageDealer")
    private String garageDealer;

    @JsonProperty("SurveyorId")
    private String surveyorId;
}
