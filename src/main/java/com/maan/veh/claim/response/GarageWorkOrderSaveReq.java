package com.maan.veh.claim.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GarageWorkOrderSaveReq {
	
	 	@JsonProperty("ClaimNo")
	    private String claimNo;

	 	@JsonProperty("WorkOrderNo")
	    private String workOrderNo;
	 	
	 	@JsonProperty("WorkOrderType")
	    private String workOrderType;
	 	
	 	 @JsonProperty("WorkOrderTypeDesc")
	     private String workOrderTypeDesc;

	 	@JsonProperty("WorkOrderDate")
	    private String workOrderDate;

	 	@JsonProperty("SettlementType")
	    private String settlementType;
	 	
	 	@JsonProperty("SettlementTypeDesc")
	    private String settlementTypeDesc;

	 	@JsonProperty("SettlementTo")
	    private String settlementTo;
	 	
	 	@JsonProperty("SettlementToDesc")
	    private String settlementToDesc;

	 	@JsonProperty("GarageName")
	    private String garageName;

	 	@JsonProperty("GarageId")
	    private String garageId;

	 	@JsonProperty("Location")
	    private String location;
	 	
	 	@JsonProperty("RepairType")
	    private String repairType;
	 	
	 	@JsonProperty("QuotationNo")
	    private String quotationNo;
	 	
	 	@JsonProperty("DeliveryDate")
	    private String deliveryDate;
	 	
	 	@JsonProperty("JointOrderYn")
	    private String jointOrderYn;
	 	
	 	@JsonProperty("SubrogationYn")
	    private String subrogationYn;
	 	
	 	@JsonProperty("TotalLoss")
	    private String totalLoss;
	 	
	 	@JsonProperty("LossType")
	    private String lossType;
	 	
	 	@JsonProperty("Remarks")
	    private String remarks;

	 	@JsonProperty("CreatedBy")
	    private String createdBy;

	 	@JsonProperty("UpdatedBy")
	    private String updatedBy;
	 	
	 	@JsonProperty("SparepartsDealerId")
	    private String sparepartsDealerId;
	 	
	 	@JsonProperty("QuoteStatus")
	    private String quoteStatus;

}
