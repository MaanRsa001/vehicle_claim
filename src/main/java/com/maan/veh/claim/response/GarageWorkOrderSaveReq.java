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

	 	@JsonProperty("WorkOrderDate")
	    private String workOrderDate;

	 	@JsonProperty("SettlementType")
	    private String settlementType;

	 	@JsonProperty("SettlementTo")
	    private String settlementTo;

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

	 	@JsonProperty("CreatedDate")
	    private String createdDate;

	 	@JsonProperty("UpdatedBy")
	    private String updatedBy;
	 	
	 	@JsonProperty("UpdatedDate")
	    private String updatedDate;

	 	@JsonProperty("SparepartsDealerId")
	    private String sparepartsDealerId;


}
