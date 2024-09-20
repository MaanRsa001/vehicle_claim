package com.maan.veh.claim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimIntimationDTODocumentDetails {
    @JsonProperty("DocumentData")
    private String documentData;

    @JsonProperty("DocumentFormat")
    private String documentFormat;

    @JsonProperty("DocumentId")
    private String documentId;

    @JsonProperty("DocumentName")
    private String documentName;

    @JsonProperty("DocumentRefNo")
    private String documentRefNo;

    @JsonProperty("DocumentType")
    private String documentType;

    @JsonProperty("DocumentURL")
    private String documentURL;
}
