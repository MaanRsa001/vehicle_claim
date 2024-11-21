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
@IdClass(CityMasterId.class)
@Table(name="vc_city_master")
public class CityMaster {
	
	@Id
	@Column(name = "CITY_ID", nullable = false)
	private Integer cityId;

	@Id
	@Column(name = "AMEND_ID", nullable = false)
	private Integer amendId;

	@Id
	@Column(name = "COMPANY_ID", nullable = false)
	private String companyId;

	@Column(name = "CITY_NAME", length = 100)
	private String cityName;

	@Column(name = "EFFECTIVE_DATE_END", nullable = false)
	private Date effectiveDateEnd;

	@Column(name = "EFFECTIVE_DATE_START", nullable = false)
	private Date effectiveDateStart;

	@Column(name = "STATUS", length = 10)
	private String status;

	@Column(name = "REMARKS", length = 100)
	private String remarks;

	@Column(name = "ENTRY_DATE")
	private Date entryDate;

	@Column(name = "CORE_APP_CODE", length = 20)
	private String coreAppCode;

	@Column(name = "CREATED_BY", nullable = false, length = 100)
	private String createdBy;

	@Column(name = "UPDATED_BY", length = 100)
	private String updatedBy;

	@Column(name = "UPDATED_DATE")
	private Date updatedDate;
}
