package com.maan.veh.claim.entity;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
@Entity
@Table(name = "vc_losstype_master")
@IdClass(ClaimLossTypeMasterId.class)
public class ClaimLossTypeMaster {

    @Id
    @Column(name = "TYPE_ID")
    private Integer typeId;

    @Id
    @Column(name = "CATEGORY_ID")
    private Integer categoryId;

    @Id
    @Column(name = "COMPANY_ID")
    private Integer companyId;

    @Column(name = "CATEGORY_DESC")
    private String categoryDesc;

    @Column(name = "TYPE_DESC")
    private String typeDesc;

    @Column(name = "CORE_APPCODE")
    private String coreAppCode;

    @Column(name = "STATUS")
    private String status;

    @Column(name = "ENTRY_DATE")
    private Date entryDate;

    @Column(name = "REMARKS")
    private String remarks;

    @Column(name = "COMP_YN")
    private String compYn;

    @Column(name = "GARAGE_YN")
    private String garageYn;

    @Column(name = "PART_OF_LOSS")
    private String partOfLoss;

    @Column(name = "MANDATORY_DOC_LIST")
    private String mandatoryDocList;

    @Column(name = "SURVEYOR_YN")
    private String surveyorYn;

    @Column(name = "EFFECTIVE_DATE")
    private Date effectiveDate;

    @Column(name = "AMENDID")
    private Integer amendId;

}
