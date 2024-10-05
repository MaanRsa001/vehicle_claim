package com.maan.veh.claim.response;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SurveyorViewResponse {
	
	@JsonProperty("CompanyId")
    private String companyId;

    @JsonProperty("PolicyNo")
    private String policyNo;

    @JsonProperty("ClaimNo")
    private String claimNo;

    @JsonProperty("VehicleMake")
    private String vehicleMake;

    @JsonProperty("VehicleModel")
    private String vehicleModel;

    @JsonProperty("MakeYear")
    private String makeYear;

    @JsonProperty("ChassisNo")
    private String chassisNo;

    @JsonProperty("InsuredName")
    private String insuredName;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("VehicleRegno")
    private String vehicleRegNo;
    
    @JsonFormat(pattern="dd/MM/yyyy")
    @JsonProperty("EntryDate")
    private Date entryDate;

    @JsonProperty("Status")
    private String status;
    
    @JsonProperty("QuoteStatus")
    private String quoteStatus;
    
    @JsonProperty("QuotationNo")
    private String quotationNo;
    
    @JsonProperty("DamageSno")
    private String damageSno;

    @JsonProperty("DamageDictDesc")
    private String damageDictDesc;

    @JsonProperty("DamagePart")
    private String damagePart;

    @JsonProperty("RepairReplace")
    private String repairReplace;

    @JsonProperty("NoOfParts")
    private String noOfParts;

    @JsonProperty("GaragePrice")
    private String garagePrice;

    @JsonProperty("DealerPrice")
    private String dealerPrice;

    @JsonProperty("GarageLoginId")
    private String garageLoginId;

    @JsonProperty("DealerLoginId")
    private String dealerLoginId;
}
