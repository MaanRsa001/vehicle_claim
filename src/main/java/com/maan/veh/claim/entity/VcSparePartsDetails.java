package com.maan.veh.claim.entity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "vc_spare_parts_details")
public class VcSparePartsDetails {

    @Id
    @Column(name = "claim_number", nullable = false, length = 255)
    private String claimNumber;

    @Column(name = "replacement_cost", precision = 10, scale = 2)
    private BigDecimal replacementCost = BigDecimal.ZERO;

    @Column(name = "replacement_cost_deductible", precision = 10, scale = 2)
    private BigDecimal replacementCostDeductible = BigDecimal.ZERO;

    @Column(name = "spare_part_depreciation", precision = 10, scale = 2)
    private BigDecimal sparePartDepreciation = BigDecimal.ZERO;

    @Column(name = "discount_on_spare_parts", precision = 10, scale = 2)
    private BigDecimal discountOnSpareParts = BigDecimal.ZERO;

    @Column(name = "total_amount_replacement", precision = 10, scale = 2)
    private BigDecimal totalAmountReplacement = BigDecimal.ZERO;

    @Column(name = "repair_labour", precision = 10, scale = 2)
    private BigDecimal repairLabour = BigDecimal.ZERO;

    @Column(name = "repair_labour_deductible", precision = 10, scale = 2)
    private BigDecimal repairLabourDeductible = BigDecimal.ZERO;

    @Column(name = "repair_labour_discount_amount", precision = 10, scale = 2)
    private BigDecimal repairLabourDiscountAmount = BigDecimal.ZERO;

    @Column(name = "total_amount_repair_labour", precision = 10, scale = 2)
    private BigDecimal totalAmountRepairLabour = BigDecimal.ZERO;

    @Column(name = "net_amount", precision = 10, scale = 2)
    private BigDecimal netAmount = BigDecimal.ZERO;

    @Column(name = "unknown_accident_deduction", precision = 10, scale = 2)
    private BigDecimal unknownAccidentDeduction = BigDecimal.ZERO;

    @Column(name = "amount_to_be_recovered", precision = 10, scale = 2)
    private BigDecimal amountToBeRecovered;

    @Column(name = "total_after_deductions", precision = 10, scale = 2)
    private BigDecimal totalAfterDeductions = BigDecimal.ZERO;

    @Column(name = "vat_rate_Per", precision = 5, scale = 2)
    private BigDecimal vatRatePer;

    @Column(name = "vat_rate", precision = 5, scale = 2)
    private BigDecimal vatRate;

    @Column(name = "vat_amount", precision = 10, scale = 2)
    private BigDecimal vatAmount = BigDecimal.ZERO;

    @Column(name = "total_with_vat", precision = 10, scale = 2)
    private BigDecimal totalWithVAT = BigDecimal.ZERO;
    
    @Column(name = "salvage_deduction", precision = 10, scale = 2)
    private BigDecimal salvageDeduction = BigDecimal.ZERO;

}

