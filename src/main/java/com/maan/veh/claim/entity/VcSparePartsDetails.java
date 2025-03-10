package com.maan.veh.claim.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "vc_spare_parts_details")
@IdClass(VcSparePartsDetailsId.class)
public class VcSparePartsDetails {

    @Id
    @Column(name = "claim_no", nullable = false, length = 100)
    private String claimNumber;

    @Id
    @Column(name = "quotation_no", nullable = false, length = 100)
    private String quotationNo;

    @Id
    @Column(name = "damage_sno", nullable = false, length = 50)
    private String damageSno;
    
    @Id
    @Column(name = "garage_id", nullable = false, length = 100) // Newly added primary key field
    private String garageId;

    @Column(name = "spare_part_type", length = 100)
    private String sparePartType;

    @Column(name = "spare_part_type_desc", length = 100)
    private String sparePartTypeDesc;

    @Column(name = "original_discount")
    private BigDecimal originalDiscount;

    @Column(name = "discount_percentage")
    private BigDecimal discountPercentage;

    @Column(name = "discount_amount")
    private BigDecimal discountAmount;

    @Column(name = "replacement_cost_deductible")
    private BigDecimal replacementCostDeductible;

    @Column(name = "damage_type", length = 100)
    private String damageType;

    @Column(name = "depreciation_type", length = 50)
    private String depreciationType;

    @Column(name = "depreciation_type_desc", length = 100)
    private String depreciationTypeDesc;

    @Column(name = "depreciation")
    private BigDecimal depreciation;

    @Column(name = "referral_status", length = 100)
    private String referralStatus;

    @Column(name = "REPAIR_LABOUR")
    private BigDecimal repairLabour;

    @Column(name = "repair_labour_discount")
    private BigDecimal repairLabourDiscount;

    @Column(name = "repair_labour_discount_amount")
    private BigDecimal repairLabourDiscountAmount;

    @Column(name = "repair_labour_deductible")
    private BigDecimal repairLabourDeductible;

    @Column(name = "total_amount_repair_labour")
    private BigDecimal totalAmountRepairLabour;

    @Column(name = "remarks", length = 500)
    private String remarks;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "damage_direction", length = 50)
    private String damageDirection;

    @Column(name = "damage_direction_desc", length = 50)
    private String damageDirectionDesc;

    @Column(name = "part_type", length = 100)
    private String partType;

    @Column(name = "part_type_desc", length = 100)
    private String partTypeDesc;

    @Column(name = "replace_repair", length = 50)
    private String replaceRepair;

    @Column(name = "no_of_units")
    private Integer noOfUnits;

    @Column(name = "spare_parts_cost")
    private BigDecimal sparePartsCost;

    @Column(name = "labour_charge")
    private BigDecimal labourCharge;

    @Column(name = "total_cost")
    private BigDecimal totalCost;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
