package com.maan.veh.claim.entity;
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
@Table(name = "damage_section_details")
@IdClass(DamageSectionDetailsId.class)
public class DamageSectionDetails {

    @Id
    @Column(name = "claim_no")
    private String claimNo;

    @Id
    @Column(name = "damage_sno")
    private int damageSno;

    @Column(name = "damage_direction")
    private String damageDictDesc;

    @Column(name = "damage_part")
    private String damagePart;

    @Column(name = "repair_replace")
    private String repairReplace;

    @Column(name = "no_of_parts")
    private Integer noOfParts;

    @Column(name = "garage_price")
    private Double garagePrice;

    @Column(name = "dealer_price")
    private Double dealerPrice;

    @Column(name = "garage_loginId")
    private String garageLoginId;

    @Column(name = "dealer_loginId")
    private String dealerLoginId;

    @Column(name = "surveyor_id")
    private Integer surveyorId;

    @Column(name = "replace_cost")
    private Double replaceCost;

    @Column(name = "replace_cost_deduct")
    private Double replaceCostDeduct;

    @Column(name = "sparepart_deprection")
    private Double sparepartDeprection;

    @Column(name = "discount_sparepart")
    private Double discountSparepart;

    @Column(name = "totamt_replace")
    private Double totamtReplace;

    @Column(name = "labour_cost")
    private Double labourCost;

    @Column(name = "labour_cost_deduct")
    private Double labourCostDeduct;

    @Column(name = "labour_disc")
    private Double labourDisc;

    @Column(name = "totamt_of_labour")
    private Double totamtOfLabour;

    @Column(name = "tot_price")
    private Double totPrice;

    @Column(name = "entry_date")
    private Date entryDate;

    @Column(name = "status")
    private String status;
}

