package com.maan.veh.claim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DownloadDocumentRequestDto {
    
    @JsonProperty("sgsId")
    private String sgsId;
    
    @JsonProperty("docId")
    private String docId;
    
    @JsonProperty("fileName")
    private String fileName;
    
    @JsonProperty("docPrintType")
    private String docPrintType;
    
}