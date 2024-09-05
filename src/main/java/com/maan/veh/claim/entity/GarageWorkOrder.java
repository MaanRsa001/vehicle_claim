package com.maan.veh.claim.entity;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
	
@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vc_garage_work_order")
@IdClass(GarageWorkOrderId.class)
public class GarageWorkOrder {

    @Id
    @Column(name = "claim_no")
    private String claimNo;

    @Column(name = "work_order_no")
    private String workOrderNo;

    @Column(name = "work_order_type")
    private String workOrderType;

    @Column(name = "work_order_date")
    private Date workOrderDate;

    @Column(name = "settlement_type")
    private String settlementType;

    @Column(name = "settlement_to")
    private String settlementTo;

    @Column(name = "garage_name")
    private String garageName;

    @Column(name = "garage_id")
    private String garageId;

    @Column(name = "location")
    private String location;

    @Column(name = "repair_type")
    private String repairType;

    @Column(name = "quotation_no")
    private String quotationNo;

    @Column(name = "delivery_date")
    private Date deliveryDate;

    @Column(name = "joint_order_yn")
    private String jointOrderYn;

    @Column(name = "subrogation_yn")
    private String subrogationYn;

    @Column(name = "total_loss")
    private BigDecimal totalLoss;

    @Column(name = "loss_type")
    private String lossType;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "created_by")
    private String createdBy;

    @Column(name = "created_date")
    private Date createdDate;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "updated_date")
    private Date updatedDate;

    @Column(name = "entry_date")
    private Date entryDate;

    @Column(name = "status")
    private String status;

    @Column(name = "spareparts_dealer_id")
    private String sparepartsDealerId;

    // Getters and Setters
}

