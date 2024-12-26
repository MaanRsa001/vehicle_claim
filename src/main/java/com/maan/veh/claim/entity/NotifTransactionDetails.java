/* 
*  Copyright (c) 2019. All right reserved
 * Created on 2023-01-11 ( Date ISO 2023-01-11 - Time 13:27:46 )
 * Generated by Telosys Tools Generator ( version 3.3.0 )
 */

/*
 * Created on 2023-01-11 ( 13:27:46 )
 * Generated by Telosys ( http://www.telosys.org/ ) version 3.3.0
 */


package com.maan.veh.claim.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import jakarta.persistence.Table;

import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.List;

import java.math.BigDecimal;
import java.util.Date;
import jakarta.persistence.*;




/**
* Domain class for entity "NotifTransactionDetails"
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
@Table(name="notif_transaction_details")


public class NotifTransactionDetails implements Serializable {
 
private static final long serialVersionUID = 1L;
 
    //--- ENTITY PRIMARY KEY 
    @Id
    //@GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="NOTIF_NO", nullable=false)
    public Long    notifNo ;

    //--- ENTITY DATA FIELDS 
    @Column(name="CUSTOMER_NAME", length=20)
    public String     customerName ;

    @Column(name="CUSTOMER_MAIL_ID", length=20)
    public String     customerMailid ;

    @Column(name="CUSTOMER_PHONE_NO")
    public BigDecimal customerPhoneNo ;

    @Column(name="CUSTOMER_PHONE_CODE")
    public Integer    customerPhoneCode ;

    @Column(name="CUSTOMER_MESSENGER_CODE")
    public Integer    customerMessengerCode ;

    @Column(name="CUSTOMER_MESSENGER_PHONE")
    public BigDecimal customerMessengerPhone ;

    @Column(name="BROKER_NAME", length=20)
    public String     brokerName ;

    @Column(name="BROKER_COMPANY_NAME", length=20)
    public String     brokerCompanyName ;

    @Column(name="BROKER_MAIL_ID", length=20)
    public String     brokerMailId ;

    @Column(name="BROKER_PHONE_NO")
    public BigDecimal brokerPhoneNo ;

    @Column(name="BROKER_PHONE_CODE")
    public Integer    brokerPhoneCode ;

    @Column(name="BROKER_MESSENGER_CODE")
    public Integer    brokerMessengerCode ;

    @Column(name="BROKER_MESSENGER_PHONE")
    public BigDecimal brokerMessengerPhone ;

    @Column(name="UW_NAME", length=20)
    public String     uwName ;

    @Column(name="UW_MAIL_ID", length=20)
    public String     uwMailid ;

    @Column(name="UW_PHONE_CODE")
    public Integer    uwPhonecode ;

    @Column(name="UW_PHONE_NO")
    public BigDecimal uwPhoneNo ;

    @Column(name="UW_MESSENGER_CODE")
    public Integer    uwMessengerCode ;

    @Column(name="UW_MESSENGER_PHONE")
    public BigDecimal uwMessengerPhone ;

    @Column(name="COMPANY_NAME", length=20)
    public String     companyName ;

    @Column(name="PRODUCT_NAME", length=20)
    public String     productName ;

    @Column(name="SECTION_NAME", length=20)
    public String     sectionName ;

    @Column(name="STATUS_MESSAGE", length=20)
    public String     statusMessage ;

    @Column(name="OTP")
    public Integer    otp ;

    @Column(name="POLICY_NO")
    public String policyNo ;

    @Column(name="QUOTE_NO")
    public String quoteNo ;

    @Column(name="NOTIF_DESCRIPTION", length=100)
    public String     notifDescription ;

    @Column(name="NOTIF_TEMPLATE_NAME", length=20)
    public String     notifTemplatename ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="ENTRY_DATE")
    public Date       entryDate ;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="NOTIFCATION_PUSH_DATE")
    public Date       notifcationPushDate ;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="NOTIFCATION_END_DATE")
    public Date       notifcationEndDate ;

    @Column(name="NOTIF_PUSHED_STATUS", length=20)
    public String     notifPushedStatus ;

    @Column(name="NOTIF_PRIORITY", nullable=false)
    public Integer    notifPriority ;

    @Column(name="TINY_URL", length=15)
    public String     tinyUrl ;

    @Column(name="COMPANY_ID", nullable=false, length=15)
    public String     companyid ;

    @Column(name="PRODUCT_ID", nullable=false)
    public Integer    productid ;

    @Column(name="COMPANY_ADDRESS")
    public String companyAddress;
    @Column(name="COMPANY_LOGO")
    public String companyLogo;
    //--- ENTITY LINKS ( RELATIONSHIP )
    @Column(name="ATTACH_FILE_PATH")
    public String attachFilePath;
    
    @Column(name="PUSHED_BY", length=100)
    public String pushedBy;
    
    @Column(name="UWLOGIN_ID", length=100)
    public String uwloginId;
    
    @Column(name="UWUSER_TYPE", length=100)
    public String uwUserType;
    
    @Column(name="UWSUBUSER_TYPE", length=100)
    public String uwSubuserType;

    @Column(name="CUSTOMER_REFNO", length=100)
    public String customerRefno;

    @Column(name="PAGE_TYPE", length=100)
    public String pageType;

    @Column(name="BRANCH_CODE", length=100)
    public String branchCode;

    @Column(name="REFNO", length=100)
    public String refno;

    @Column(name="TINY_URL_ID", length=100)
    public String tinyUrlId;
    
    
    @Column(name="TINY_URL_ACTIVE", length=100)
    public String tinyUrlActive;
    
    @Column(name="TINY_GROUP_ID", length=100)
    public String tinyGroupId;
    
    @Column(name="REG_NO", length=100)
    public String regNo;
    
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="EXPIRY_DATE")
    public Date       expiryDate ;
    
}


