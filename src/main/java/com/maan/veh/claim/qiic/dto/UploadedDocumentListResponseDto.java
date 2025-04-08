package com.maan.veh.claim.qiic.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UploadedDocumentListResponseDto {
	
	@JsonProperty("status")
    private int status;

    @JsonProperty("data")
    private List<DocumentData> data;

    @JsonProperty("message")
    private String message;

    @JsonProperty("hasError")
    private String hasError;

    @Data
    public static class DocumentData {
        @JsonProperty("docName")
        private String docName;

        @JsonProperty("docId")
        private String docId;

        @JsonProperty("sgsId")
        private String sgsId;

        @JsonProperty("srNo")
        private String srNo;

        @JsonProperty("docStatus")
        private String docStatus;

        @JsonProperty("docDesc")
        private String docDesc;

        @JsonProperty("docSgsId")
        private String docSgsId;
        
        @JsonProperty("templateId")
        private String templateId;
    }
}
