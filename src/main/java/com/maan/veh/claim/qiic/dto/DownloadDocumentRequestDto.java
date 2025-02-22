package com.maan.veh.claim.qiic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class DownloadDocumentRequestDto {
    
    @JsonProperty("sgsId")
    private String sgsId;
    
    @JsonProperty("fileName")
    private String fileName;
    
    @JsonProperty("docPrintType")
    private String docPrintType;
    
}
