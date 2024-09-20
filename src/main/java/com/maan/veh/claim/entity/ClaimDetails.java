package com.maan.veh.claim.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "claim_details")
@IdClass(ClaimDetailsId.class)
@Data
@NoArgsConstructor
public class ClaimDetails {
	
	@Column(name = "claim_reference_no")
    private String claimReferenceNo;
	
    @Id
    @Column(name = "claim_no")
    private String claimNo;

    @Id
    @Column(name = "policy_no")
    private String policyNo;

    @Column(name = "insured_id")
    private String insuredId;

    @Column(name = "loss_date")
    @Temporal(TemporalType.DATE)
    private Date lossDate;

    @Column(name = "intimated_date")
    @Temporal(TemporalType.DATE)
    private Date intimatedDate;

    @Column(name = "loss_location")
    private String lossLocation;

    @Column(name = "police_station")
    private String policeStation;

    @Column(name = "police_report_no")
    private String policeReportNo;

    @Column(name = "loss_description")
    private String lossDescription;

    @Column(name = "at_fault")
    private String atFault;

    @Column(name = "policy_period")
    private String policyPeriod;

    @Column(name = "contact_person_phone_no")
    private String contactPersonPhoneNo;

    @Column(name = "contact_person_phone_code")
    private String contactPersonPhoneCode;

    @Column(name = "policy_reference_no")
    private String policyReferenceNo;

    @Column(name = "policy_ic_reference_no")
    private String policyICReferenceNo;

    @Column(name = "claim_request_reference")
    private String claimRequestReference;

    @Column(name = "claim_category")
    private String claimCategory;

    @Column(name = "created_user")
    private String createdUser;

    @Column(name = "claim_type")
    private Integer claimType;

    @Column(name = "accident_number")
    private String accidentNumber;

    @Column(name = "is_third_party_involved")
    private String isThirdPartyInvolved;

    // Driver Details - saved in the same table
    @Column(name = "driver_emirates_id")
    private String driverEmiratesId;

    @Column(name = "driver_license_number")
    private String driverLicenseNumber;

    @Column(name = "driver_dob")
    @Temporal(TemporalType.DATE)
    private Date driverDob;

}

