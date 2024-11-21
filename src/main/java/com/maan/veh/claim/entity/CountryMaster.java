package com.maan.veh.claim.entity;

import java.util.Date;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

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
@Entity
@DynamicInsert
@DynamicUpdate
@Builder
@IdClass(CountryMasterId.class)
@Table(name="vc_country_master")
public class CountryMaster {
	
	@Id
    @Column(name = "COMPANY_ID", nullable = false, length = 100)
    private String companyId;

    @Id
    @Column(name = "COUNTRY_ID", nullable = false, length = 20)
    private String countryId;

    @Id
    @Column(name = "COUNTRY_SHORT_CODE", nullable = false, length = 20)
    private String countryShortCode;

    @Id
    @Column(name = "AMEND_ID", nullable = false)
    private Integer amendId;

    @Column(name = "COUNTRY_NAME", length = 50)
    private String countryName;

    @Column(name = "NATIONALITY", length = 200)
    private String nationality;

    @Column(name = "CORE_APP_CODE", length = 20)
    private String coreAppCode;

    @Column(name = "MOBILE_CODE", length = 100)
    private String mobileCode;

    @Column(name = "ENTRY_DATE")
    private Date entryDate;

    @Column(name = "STATUS", length = 1)
    private String status;

    @Column(name = "EFFECTIVE_DATE_START")
    private Date effectiveDateStart;

    @Column(name = "EFFECTIVE_DATE_END")
    private Date effectiveDateEnd;

    @Column(name = "REMARKS", length = 100)
    private String remarks;

    @Column(name = "CREATED_BY", nullable = false, length = 100)
    private String createdBy;

    @Column(name = "UPDATED_BY", length = 100)
    private String updatedBy;

    @Column(name = "UPDATED_DATE")
    private Date updatedDate;
}
