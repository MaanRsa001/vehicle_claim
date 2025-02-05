package com.maan.veh.claim.external;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class VcInuredVehicleApiReponse {
	
	    @JsonProperty("claimNo")
		 private String claimno ;
		 
		 @JsonProperty("policyNo")
		 private String policyno ;
		 
		 @JsonProperty("model")
		 private String model ;
		 
		 @JsonProperty("make")
		 private String make ;
		 
		 
		 @JsonProperty("modelKey")
		 private String modelkey ;
		 
		 
		 @JsonProperty("year")
		 private String year ;
		 
		 
		 @JsonProperty("chassisNo")
		 private String chassisno ;
		
		 @JsonProperty("insuredName")
		 private String insuredname ;
		 
	
		 
		 @JsonProperty("lossLocation")
		 private String losslocation ;
		 
		 @JsonProperty("vehicleregno")
		 private String vehicleregno ;
		 
		 @JsonProperty("entrydate")
		 private Date entrydate ;
		 
		 @JsonProperty("status")
		 private String status ;
		 
		 @JsonProperty("fnolSgsId")
		 private String fnolsgsid ;
		 
		@JsonProperty("garageid")
		private String garageid ;
		   
		@JsonProperty("quotationno")
		private String quotationno ;

		@JsonProperty("partyId")
		private String partyid ;
		
		
		@JsonProperty("customerId")
		private String customerid ;
		
		@JsonProperty("insuredId")
		private String insuredid ;
		
		@JsonProperty("makeKey")
		private String makekey ;
		
		@JsonProperty("vehRegNo")
		private String vehregno ;
		
		@JsonProperty("bodyType")
		private String bodytype ;
		
		@JsonProperty("prodId")
		private String prodid ;
		
		@JsonProperty("riskId")
		private String riskid ;
		
		@JsonProperty("fnolNo")
		private String fnolno ;
	
		@JsonProperty("claimStatus")
		private String claimstatus ;
		
}
