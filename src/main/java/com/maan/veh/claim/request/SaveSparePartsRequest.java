package com.maan.veh.claim.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class SaveSparePartsRequest {

	@JsonProperty("requestMetaData")
	private SaveSparePartsRequestMetaData requestMetaData;
	
	@JsonProperty("claimNo")
	private String claimNo;

	@JsonProperty("workOrderType")
	private String workOrderType;

	@JsonProperty("workOrderNo")
	private String workOrderNo;

	@JsonProperty("workOrderDate")
	private String workOrderDate;

	@JsonProperty("accForSettlementType")
	private String accForSettlementType;

	@JsonProperty("accForSettlement")
	private String accForSettlement;

	@JsonProperty("sparePartsDealer")
	private String sparePartsDealer;

	@JsonProperty("garageCode")
	private String garageCode;
	
	@JsonProperty("lpoId")
	private String lpoId;

	@JsonProperty("garageQuotationNo")
	private String garageQuotationNo;

	@JsonProperty("deliveryDate")
	private String deliveryDate;

	@JsonProperty("deliveredTo")
	private String deliveredTo;
	
	@JsonProperty("deliveredId")
	private String deliveredId;


	@JsonProperty("subrogation")
	private String subrogation;

	@JsonProperty("jointOrder")
	private String jointOrder;

	@JsonProperty("totalLoss")
	private String totalLoss;

	@JsonProperty("totalLossType")
	private String totalLossType;

	@JsonProperty("remarks")
	private String remarks;

	@JsonProperty("replacementCost")
	private String replacementCost;

	@JsonProperty("replacementCostDeductible")
	private String replacementCostDeductible;

	@JsonProperty("sparePartDepreciation")
	private String sparePartDepreciation;

	@JsonProperty("discountonSpareParts")
	private String discountonSpareParts;

	@JsonProperty("totalAmountReplacement")
	private String totalAmountReplacement;

	@JsonProperty("repairLabour")
	private String repairLabour;

	@JsonProperty("repairLabourDeductible")
	private String repairLabourDeductible;

	@JsonProperty("repairLabourDiscountAmount")
	private String repairLabourDiscountAmount;

	@JsonProperty("totalAmountRepairLabour")
	private String totalAmountRepairLabour;

	@JsonProperty("netAmount")
	private String netAmount;

	@JsonProperty("unkownAccidentDeduction")
	private String unkownAccidentDeduction;

	@JsonProperty("amounttobeRecovered")
	private String amounttobeRecovered;

	@JsonProperty("totalafterDeductions")
	private String totalafterDeductions;

	@JsonProperty("vatRatePer")
	private String vatRatePer;

	@JsonProperty("vatRate")
	private String vatRate;

	@JsonProperty("vatAmount")
	private String vatAmount;

	@JsonProperty("totalWithVAT")
	private String totalWithVAT;

	@JsonProperty("vehicleDamageDetails")
	private List<VehicleDamageDetailRequest> vehicleDamageDetails;
}
