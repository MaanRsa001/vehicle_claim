/* 
*  Copyright (c) 2019. All right reserved
 * Created on 2022-11-21 ( Date ISO 2022-11-21 - Time 15:20:14 )
 * Generated by Telosys Tools Generator ( version 3.3.0 )
 */

/*
 * Created on 2022-11-21 ( 15:20:14 )
 * Generated by Telosys ( http://www.telosys.org/ ) version 3.3.0
 */


package com.maan.veh.claim.entity;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import lombok.*;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import jakarta.persistence.*;




/**
* Domain class for entity "MenuMaster"
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
@IdClass(MenuMasterId.class)
@Table(name="vc_menu_master")


public class MenuMaster implements Serializable {
 
private static final long serialVersionUID = 1L;
 
    //--- ENTITY PRIMARY KEY 
	@Id
	@Column(name="MENU_ID", nullable=false)
	private Integer    menuId ;
	
	@Id
	@Column(name="USERTYPE", length=100,nullable=false)
	private String     usertype ;
	
	@Id
	@Column(name="COMPANY_ID", length=50,nullable=false)
	private String     companyId ; 
	    

    //--- ENTITY DATA FIELDS 
    @Column(name="MENU_NAME", length=300)
    private String     menuName ;

    @Column(name="MENU_URL", length=4000)
    private String     menuUrl ;

    @Column(name="PARENT_MENU", length=10)
    private String     parentMenu ;

    @Column(name="BRANCH_CODE", length=10)
    private String     branchCode ;

    @Column(name="PRODUCT_ID")
    private Integer    productId ;

    @Column(name="STATUS", nullable=false, length=1)
    private String     status ;

    @Column(name="RSACODE", length=25)
    private String     rsacode ;


    @Column(name="ISCLICK", length=100)
    private String     isclick ;

    @Column(name="DISPLAY_ORDER")
    private Integer    displayOrder ;

    @Column(name="DISPLAY_YN", length=1)
    private String     displayYn ;

    @Column(name="MENU_LOGO", length=200)
    private String     menuLogo ;

    @Column(name="CREATED_BY")
	private String createdBy;
    
    @Column(name="ENTRY_DATE")
	private Date entryDate;
    
    @Column(name="MENU_NAME_LOCAL", length=100)
    private String     menuNameLocal ;
    //--- ENTITY LINKS ( RELATIONSHIP )


}



