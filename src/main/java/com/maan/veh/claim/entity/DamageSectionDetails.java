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
import jakarta.validation.constraints.NotNull;
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
	    @NotNull
	    @Column(name = "claim_no", length = 50)
	    private String claimNo;

	    @Id
	    @NotNull
	    @Column(name = "damage_sno")
	    private Integer damageSno;
	    
	    @Column(name = "quotation_no")
	    private String quotationNo;

	    @Column(name = "damage_direction", length = 100)
	    private String damageDirection;

	    @Column(name = "damage_part", length = 100)
	    private String damagePart;

	    @Column(name = "repair_replace", length = 50)
	    private String repairReplace;

	    @Column(name = "no_of_parts")
	    private Integer noOfParts;

	    @Column(name = "garage_price", precision = 10, scale = 2)
	    private BigDecimal garagePrice;

	    @Column(name = "dealer_price", precision = 10, scale = 2)
	    private BigDecimal dealerPrice;

	    @Column(name = "garage_login_id", length = 50)
	    private String garageLoginId;

	    @Column(name = "dealer_login_id", length = 50)
	    private String dealerLoginId;

	    @Column(name = "surveyor_id")
	    private String surveyorId;

	    @Column(name = "replace_cost", precision = 10, scale = 2)
	    private BigDecimal replaceCost;

	    @Column(name = "replace_cost_deduct", precision = 10, scale = 2)
	    private BigDecimal replaceCostDeduct;

	    @Column(name = "sparepart_deprection", precision = 10, scale = 2)
	    private BigDecimal sparepartDeprection;

	    @Column(name = "discount_sparepart", precision = 10, scale = 2)
	    private BigDecimal discountSparepart;

	    @Column(name = "totamt_replace", precision = 10, scale = 2)
	    private BigDecimal totamtReplace;

	    @Column(name = "labour_cost", precision = 10, scale = 2)
	    private BigDecimal labourCost;

	    @Column(name = "labour_cost_deduct", precision = 10, scale = 2)
	    private BigDecimal labourCostDeduct;

	    @Column(name = "labour_disc", precision = 10, scale = 2)
	    private BigDecimal labourDisc;

	    @Column(name = "totamt_of_labour", precision = 10, scale = 2)
	    private BigDecimal totamtOfLabour;

	    @Column(name = "tot_price", precision = 10, scale = 2)
	    private BigDecimal totPrice;

	    @Column(name = "entry_date")
	    @Temporal(TemporalType.DATE)
	    private Date entryDate;

	    @Column(name = "status", length = 20)
	    private String status;

}

