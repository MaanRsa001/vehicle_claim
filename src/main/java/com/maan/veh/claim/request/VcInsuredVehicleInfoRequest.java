package com.maan.veh.claim.request;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class VcInsuredVehicleInfoRequest {
	
	 @JsonProperty("Companyid")
	 private int companyid;
	 
	 @JsonProperty("Claimno")
	 private String claimno ;
	 
	 @JsonProperty("Policyno")
	 private String policyno ;
	 
	 
	 @JsonProperty("Vehiclemake")
	 private String vehiclemake ;
	 
	 @JsonProperty("Vehiclemodel")
	 private String vehiclemodel ;
	 
	 @JsonProperty("Makeyear")
	 private int makeyear ;
	 
	 
	 @JsonProperty("Chassisno")
	 private String chassisno ;
	
	 @JsonProperty("Insuredname")
	 private String insuredname ;
	 
	 
	 @JsonProperty("Type")
	 private String type ;
	 
	 @JsonProperty("Losslocation")
	 private String losslocation ;
	 
	 @JsonProperty("Vehicleregno")
	 private String vehicleregno ;
	 
	 @JsonProperty("Entrydate")
	 private Date entrydate ;
	 
	 @JsonProperty("Status")
	 private String status ;
	 
	 @JsonProperty("Fnolsgsid")
	 private String fnolsgsid ;
	 
	@JsonProperty("Garageid")
	private String garageid ;
	   
	@JsonProperty("Quotationno")
	private String quotationno ;
}
