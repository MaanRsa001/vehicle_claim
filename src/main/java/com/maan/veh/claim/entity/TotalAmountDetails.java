package com.maan.veh.claim.entity;

import java.math.BigDecimal;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "total_amount_details")
@IdClass(TotalAmountDetailsId.class)
public class TotalAmountDetails {

    @Id
    @Column(name = "claim_no", length = 50)
    private String claimNo;

    @Column(name = "net_amount", precision = 10, scale = 2)
    private BigDecimal netAmount;

    @Column(name = "totamt_aft_deduction", precision = 10, scale = 2)
    private BigDecimal totamtAftDeduction;

    @Column(name = "vat_rate_percent", precision = 5, scale = 2)
    private BigDecimal vatRatePercent;

    @Column(name = "vat_rate", precision = 10, scale = 2)
    private BigDecimal vatRate;

    @Column(name = "vat_amount", precision = 10, scale = 2)
    private BigDecimal vatAmount;

    @Column(name = "totamt_with_vat", precision = 10, scale = 2)
    private BigDecimal totamtWithVat;

    @Column(name = "entry_date")
    @Temporal(TemporalType.DATE)
    private Date entryDate;

    @Column(name = "created_by", length = 50)
    private String createdBy;

    @Column(name = "status", length = 20)
    private String status;

    @Column(name = "tot_replace_cost", precision = 10, scale = 2)
    private BigDecimal totReplaceCost;

    @Column(name = "tot_labour_cost", precision = 10, scale = 2)
    private BigDecimal totLabourCost;
}
