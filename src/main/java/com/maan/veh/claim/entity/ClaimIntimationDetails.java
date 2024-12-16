package com.maan.veh.claim.entity;
import java.util.Date;
import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "claim_intimation_details")
@IdClass(ClaimIntimationDetailsId.class)
public class ClaimIntimationDetails {

    @Id
    @Column(name = "policy_no")
    private String policyNo;
    
    @Column(name = "fnol_no")
    private String fnolNo;

    @Column(name = "request_origin")
    private String requestOrigin;

    @Column(name = "current_branch")
    private String currentBranch;

    @Column(name = "origin_branch")
    private String originBranch;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "ip_address")
    private String ipAddress;

    @Column(name = "request_generated_date_time")
    private Date requestGeneratedDateTime;

    @Column(name = "consumer_tracking_id")
    private String consumerTrackingId;

    @Column(name = "language_code")
    private String languageCode;

    @Column(name = "insured_id")
    private String insuredId;

    @Column(name = "loss_date")
    private Date lossDate;

    @Column(name = "intimated_date")
    private Date intimatedDate;

    @Column(name = "loss_location")
    private String lossLocation;

    @Column(name = "nature_of_loss")
    private String natureOfLoss;

    @Column(name = "police_station")
    private String policeStation;

    @Id	
    @Column(name = "police_report_no")
    private String policeReportNo;

    @Column(name = "loss_description", length = 250)
    private String lossDescription;

    @Column(name = "at_fault")
    private String atFault;
    
    @Column(name = "claim_status_code")
    private String claimStatusCode;
    
    @Column(name = "fnol_sgs_id")
    private String fnolSgsId;
    
    @Column(name = "claim_type")
    private String claimType;
    
    @Column(name = "claim_party_id")
    private String claimPartyId;
}

