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
@Table(name = "core_insured_vehicle_info")
@IdClass(CoreInsuredVehicleInfoId.class)
public class CoreInsuredVehicleInfo {

    @Id
    @Column(name = "company_id", nullable = false)
    private Integer companyId;

    @Id
    @Column(name = "policy_no", nullable = false, length = 50)
    private String policyNo;

    @Id
    @Column(name = "claim_no", nullable = false, length = 50)
    private String claimNo;

    @Column(name = "vehicle_make", length = 50)
    private String vehicleMake;

    @Column(name = "vehicle_model", length = 50)
    private String vehicleModel;

    @Column(name = "make_year")
    private String makeYear;

    @Column(name = "chassis_no", length = 50)
    private String chassisNo;

    @Column(name = "insured_name", length = 100)
    private String insuredName;

    @Column(name = "type", length = 50)
    private String type;

    @Column(name = "vehicle_regno", length = 50)
    private String vehicleRegNo;
    
    @Column(name = "loss_location", length = 300)
    private String lossLocation;

    @Column(name = "entry_date")
    private Date entryDate;

    @Column(name = "status", length = 20)
    private String status;
    
    @Column(name = "fnol_sgs_id", length = 100)
    private String fnolSgsId;
    
    @Id
    @Column(name = "garage_id")
    private String garageId;
    
    @Column(name = "surveyor_id")
    private String surveyorId;
    
    @Column(name = "dealer_id")
    private String dealerId;
    
    @Column(name = "quotation_no")
    private String quotationNo;
    
    @Column(name = "lpo_id")
    private String lpoId;
}
