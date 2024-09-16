/* 
*  Copyright (c) 2019. All right reserved
 * Created on 2022-01-05 ( Date ISO 2022-01-05 - Time 16:11:20 )
 * Generated by Telosys Tools Generator ( version 3.3.0 )
 */

/*
 * Created on 2022-01-05 ( 16:11:20 )
 * Generated by Telosys ( http://www.telosys.org/ ) version 3.3.0
 */


package com.maan.veh.claim.entity;


import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;




/**
* Domain class for entity "VehicleBodypartsMaster"
*
* @author Telosys Tools Generator
*
*/
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@IdClass(VehicleBodypartsMasterId.class)
@Table(name="VC_VEHICLE_BODYPARTS_MASTER")


public class VehicleBodypartsMaster implements Serializable {
 
private static final long serialVersionUID = 1L;
 
    //--- ENTITY PRIMARY KEY 
    @Id
    @Column(name="PART_ID", nullable=false)
    private BigDecimal partId ;

    @Id
    @Column(name="COMPANY_ID", nullable=false)
    private BigDecimal companyId ;

    @Id
    @Column(name="SECTION_ID", nullable=false)
    private BigDecimal sectionId ;

    //--- ENTITY DATA FIELDS 
    @Column(name="PART_DESCRIPTION", length=500)
    private String     partDescription ;

    @Temporal(TemporalType.DATE)
    @Column(name="ENTRY_DATE")
    private Date       entryDate ;

    @Column(name="STATUS", length=1)
    private String     status ;

    @Column(name="REMARKS", length=500)
    private String     remarks ;

    @Column(name="DEPRECIABLE_YN", length=1)
    private String     depreciableYn ;

    @Column(name="DEPRECIATION_PERCENT")
    private BigDecimal depreciationPercent ;

    @Temporal(TemporalType.DATE)
    @Column(name="EFFECTIVE_DATE", nullable=false)
    private Date       effectiveDate ;

    @Column(name="AMENDID", nullable=false)
    private BigDecimal amendid ;


    //--- ENTITY LINKS ( RELATIONSHIP )


}



