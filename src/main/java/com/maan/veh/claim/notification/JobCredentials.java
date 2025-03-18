package com.maan.veh.claim.notification;

import java.io.Serializable;
import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JobCredentials implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@JsonProperty("Applicationid")
	private String applicationId;
	@JsonProperty("Mailcc")
	private String mailCc;
	@JsonProperty("Smtphost")
	private String smtpHost;
	@JsonProperty("Smtpuser")
	private String smtpUser;
	@JsonProperty("Smtppwd")
	private String smtpPwd;
	@JsonProperty("Expdate")
	private String expDate;
	@JsonProperty("Exptime")
	private String expTime;
	@JsonProperty("Pwdcnt")
	private BigDecimal pwdCnt;
	@JsonProperty("Pwdlen")
	private BigDecimal pwdLen;
	@JsonProperty("Homeapplicationid")
	private String homeApplicationId;
	@JsonProperty("Address")
	private String address;
	@JsonProperty("Status")
	private String status;
	@JsonProperty("Remarks")
	private String remarks;
	@JsonProperty("Companyname")
	private String companyName;
	@JsonProperty("Toaddress")
	private String toAddress;
	@JsonProperty("Authorizyn")
	private String authorizYn;
	@JsonProperty("Smtpport")
	private BigDecimal smtpPort;
	@JsonProperty("CompanyId")
	private String companyId;
	@JsonProperty("S_NO")
	private Integer sNo;
	@JsonProperty("BRANCH_CODE")
	private String branchCode;
	@JsonProperty("AMEND_ID")
	private Integer amendId;

}
