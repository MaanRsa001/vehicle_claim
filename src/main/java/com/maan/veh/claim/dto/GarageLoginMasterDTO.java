package com.maan.veh.claim.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GarageLoginMasterDTO {
	
	@JsonProperty("Garagename")
	private String garageName;

	@JsonProperty("Garageid")
	private String garageId;
	
	@JsonProperty("CompanyId")
	private String companyId;
	
	@JsonProperty("CoreAppCode")
	private String coreAppCode;
	
	@JsonProperty("Address")
	private String address;
	
	@JsonProperty("CityName")
	private String cityName;
	
	@JsonProperty("Status")
	private String status;
	
	@JsonProperty("PassWord")
	private String passWord;
	
	@JsonProperty("RePassWord")
	private String RepassWord;
	
	@JsonProperty("BranchCode")
	private String branchCode;
	
	@JsonProperty("Garageaddress")
	private String garageAddress;
	
	@JsonProperty("Statename")
	private String stateName;
		
	@JsonProperty("Contactpersonname")
	private String contactPersonName;
	
	@JsonProperty("Mobileno")
	private String mobileNo;
	
	@JsonProperty("Emailid")
	private String emailid;
	
	@JsonFormat(pattern = "dd/MM/yyyy")
	@JsonProperty("Effectivedate")
	private Date effectiveDate;
	
	@JsonProperty("Remarks")
	private String remarks;
	
	@JsonProperty("UserType")
	private String userType;
	
	@JsonProperty("CreatedBy")
	private String createdBy;
	
}
