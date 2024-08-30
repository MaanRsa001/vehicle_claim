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
    @Column(name = "claim_no", length = 50)
    private String claimNo;

    @Id
    @Column(name = "damage_sno")
    private int damageSno;

    @Column(name = "damage_direction", length = 100)
    private String damageDictDesc;

    @Column(name = "damage_part", length = 100)
    private String damagePart;

    @Column(name = "repair_replace", length = 50)
    private String repairReplace;

    @Column(name = "no_of_parts")
    private Integer noOfParts;

    @Column(name = "garage_price", precision = 10, scale = 2)
    private Double garagePrice;

    @Column(name = "dealer_price", precision = 10, scale = 2)
    private Double dealerPrice;

    @Column(name = "garage_loginId", length = 50)
    private String garageLoginId;

    @Column(name = "dealer_loginId", length = 50)
    private String dealerLoginId;

    @Column(name = "surveyor_id")
    private Integer surveyorId;

    @Column(name = "replace_cost", precision = 10, scale = 2)
    private Double replaceCost;

    @Column(name = "replace_cost_deduct", precision = 10, scale = 2)
    private Double replaceCostDeduct;

    @Column(name = "sparepart_deprection", precision = 10, scale = 2)
    private Double sparepartDeprection;

    @Column(name = "discount_sparepart", precision = 10, scale = 2)
    private Double discountSparepart;

    @Column(name = "totamt_replace", precision = 10, scale = 2)
    private Double totamtReplace;

    @Column(name = "labour_cost", precision = 10, scale = 2)
    private Double labourCost;

    @Column(name = "labour_cost_deduct", precision = 10, scale = 2)
    private Double labourCostDeduct;

    @Column(name = "labour_disc", precision = 10, scale = 2)
    private Double labourDisc;

    @Column(name = "totamt_of_labour", precision = 10, scale = 2)
    private Double totamtOfLabour;

    @Column(name = "tot_price", precision = 10, scale = 2)
    private Double totPrice;

    @Column(name = "entry_date")
    private Date entryDate;

    @Column(name = "status", length = 20)
    private String status;
}

