package com.maan.veh.claim.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class FileUploadRequestDto {
    
    @JsonProperty("sgsId")
    private Integer sgsId;

    @JsonProperty("amndVersionId")
    private String amndVersionId;

    @JsonProperty("productId")
    private String productId;

    @JsonProperty("transactionType")
    private String transactionType;

    @JsonProperty("partyId")
    private String partyId;

    @JsonProperty("partyName")
    private String partyName;

    @JsonProperty("partyType")
    private String partyType;

    @JsonProperty("createdBy")
    private String createdBy;

    @JsonProperty("documentTransactionType")
    private String documentTransactionType;

    @JsonProperty("attachmentRefNo")
    private String attachmentRefNo;

    @JsonProperty("attachmentDetails")
    private AttachmentDetails attachmentDetails;
    
    @Data
    public static class AttachmentDetails {
        @JsonProperty("DocumentDetails")
        private List<DocumentDetails> documentDetails;
    }
    
    @Data
    public static class DocumentDetails {
        
        @JsonProperty("DocumentId")
        private String documentId;
        
        @JsonProperty("DocumentName")
        private String documentName;
        
        @JsonProperty("DocumentType")
        private String documentType;
        
        @JsonProperty("DocumentData")
        private String documentData;
        
        @JsonProperty("DocumentFormat")
        private String documentFormat;
        
        @JsonProperty("DocumentURL")
        private String documentURL;
        
        @JsonProperty("DocumentRefNo")
        private String documentRefNo;
    }
}

