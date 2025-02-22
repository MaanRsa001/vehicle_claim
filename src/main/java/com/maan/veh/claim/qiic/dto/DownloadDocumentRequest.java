package com.maan.veh.claim.qiic.dto;

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
    
    @JsonProperty("FileName")
    private String fileName;
    
    @JsonProperty("DocPrintType")
    private String docPrintType;
}
