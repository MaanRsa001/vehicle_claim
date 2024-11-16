package com.maan.veh.claim.response;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class GarageWorkOrderResponse {

    @JsonProperty("ClaimNo")
    private String claimNo;

    @JsonProperty("WorkOrderNo")
    private String workOrderNo;

    @JsonProperty("WorkOrderType")
    private String workOrderType;
    
    @JsonProperty("WorkOrderTypeDesc")
    private String workOrderTypeDesc;
    
    @JsonFormat(pattern="dd/MM/yyyy")
    @JsonProperty("WorkOrderDate")
    private Date workOrderDate;

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
    
    @JsonFormat(pattern="dd/MM/yyyy")
    @JsonProperty("DeliveryDate")
    private Date deliveryDate;

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
    
    @JsonFormat(pattern="dd/MM/yyyy")
    @JsonProperty("CreatedDate")
    private Date createdDate;

    @JsonProperty("UpdatedBy")
    private String updatedBy;
    
    @JsonFormat(pattern="dd/MM/yyyy")
    @JsonProperty("UpdatedDate")
    private Date updatedDate;
    
    @JsonFormat(pattern="dd/MM/yyyy")
    @JsonProperty("EntryDate")
    private Date entryDate;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("SparepartsDealerId")
    private String sparepartsDealerId;
    
    @JsonProperty("QuoteStatus")
    private String quoteStatus;
    
    @JsonProperty("DealerYN")
    private String dealerYn;
}

