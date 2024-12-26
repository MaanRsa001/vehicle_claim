/* 
*  Copyright (c) 2019. All right reserved
 * Created on 2022-11-21 ( Date ISO 2022-11-21 - Time 15:20:13 )
 * Generated by Telosys Tools Generator ( version 3.3.0 )
 */

/*
 * Created on 2022-11-21 ( 15:20:13 )
 * Generated by Telosys ( http://www.telosys.org/ ) version 3.3.0
 */


package com.maan.veh.claim.entity;


import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Table;

import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import java.util.Date;
import jakarta.persistence.*;




/**
* Domain class for entity "MailMaster"
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
@IdClass(MailMasterId.class)
@Table(name="eway_mail_master")


public class MailMaster implements Serializable {
 
private static final long serialVersionUID = 1L;
 
    //--- ENTITY PRIMARY KEY 
    @Id
    @Column(name="COMPANY_ID", nullable=false, length=20)
    private String     companyId ;

    @Id
    @Column(name="S_NO", nullable=false)
    private Integer    sNo ;

    @Id
    @Column(name="BRANCH_CODE", nullable=false, length=20)
    private String     branchCode ;

    @Id
    @Column(name="AMEND_ID", nullable=false)
    private Integer    amendId ;

    //--- ENTITY DATA FIELDS 
    @Column(name="SMTP_HOST", length=300)
    private String     smtpHost ;

    @Column(name="SMTP_USER", length=300)
    private String     smtpUser ;

    @Column(name="SMTP_PWD", length=300)
    private String     smtpPwd ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="EFFECTIVE_DATE_START", nullable=false)
    private Date       effectiveDateStart ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="EFFECTIVE_DATE_END", nullable=false)
    private Date       effectiveDateEnd ;

    @Column(name="ADDRESS", length=300)
    private String     address ;

    @Column(name="STATUS", length=300)
    private String     status ;

    @Column(name="CORE_APP_CODE", length=20)
    private String     coreAppCode ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ENTRY_DATE")
    private Date       entryDate ;

    @Column(name="REMARKS", length=300)
    private String     remarks ;

    @Column(name="REGULATORY_CODE", nullable=false, length=20)
    private String     regulatoryCode ;

    @Column(name="CREATED_BY", length=100)
    private String     createdBy ;

    @Column(name="SMTP_PORT")
    private Long     smtpPort ;



}


