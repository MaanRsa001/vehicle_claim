package com.maan.veh.claim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DownloadDocumentRequest {


	@JsonProperty("CompanyId")
    private String companyId;
	
	@JsonProperty("ClaimNo")
    private String claimNo;
	
	@JsonProperty("GarageId")
    private String garageId;
    
    @JsonProperty("SgsId")
    private String sgsId;
    
    @JsonProperty("DocId")
    private String docId;
    
    @JsonProperty("FileName")
    private String fileName;
    
    @JsonProperty("DocPrintType")
    private String docPrintType;
}
