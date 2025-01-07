package com.maan.veh.claim.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "vc_claim_status")
@IdClass(VcClaimStatusId.class)
public class VcClaimStatus {

    @Id
    @Column(name = "claim_no", length = 50, nullable = false)
    private String claimNo;

    @Id
    @Column(name = "claim_notification_no", length = 50, nullable = false)
    private String claimNotificationNo;

    @Id
    @Column(name = "policy_number", length = 50, nullable = false)
    private String policyNumber;

    @Id
    @Column(name = "claim_status", length = 50, nullable = false)
    private String claimStatus;

    @Column(name = "updated_at")
    private Date updatedAt;

    @Column(name = "created_at")
    private Date createdAt;

    @Column(name = "loss_remarks", length = 255)
    private String lossRemarks;

    @Column(name = "fnol_no", length = 50)
    private String fnolNo;

    @Column(name = "claim_type", length = 10)
    private String claimType;

    @Column(name = "loss_date")
    private Date lossDate;

    @Column(name = "nature_of_loss", length = 50)
    private String natureOfLoss;

    @Column(name = "product_id", length = 50)
    private String productId;

    @Column(name = "driver_name", length = 100)
    private String driverName;

    @Column(name = "accident_number", length = 50)
    private String accidentNumber;

    @Column(name = "policy_inception_date")
    private Date policyInceptionDate;

    @Column(name = "fault_percentage", precision = 5, scale = 2)
    private BigDecimal faultPercentage;

    @Column(name = "accident_location", length = 255)
    private String accidentLocation;

    @Column(name = "intimation_date")
    private Date intimationDate;

    @Column(name = "policy_expiry_date")
    private Date policyExpiryDate;

    @Column(name = "case_number", length = 50)
    private String caseNumber;

    @Column(name = "office_code", length = 50)
    private String officeCode;

    @Column(name = "claimant_type", length = 50)
    private String claimantType;

    @Column(name = "garage_code", length = 255)
    private String garageCode;

    @Column(name = "total_loss_yn", length = 1)
    private String totalLossYN;

    @Column(name = "cause_of_loss", length = 255)
    private String causeOfLoss;

    @Column(name = "lob_name", length = 50)
    private String lobName;

    @Column(name = "insured_name", length = 100)
    private String insuredName;
    
    @Column(name = "surveyor_id", length = 100)
    private String surveyorId;
    
    @Column(name = "garage_id", length = 100)
    private String garageId;
    
    @Column(name = "dealer_id", length = 100)
    private String dealerId;
}
