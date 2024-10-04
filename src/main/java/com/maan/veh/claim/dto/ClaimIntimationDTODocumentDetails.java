package com.maan.veh.claim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimIntimationDTODocumentDetails {
    @JsonProperty("documentData")
    private String documentData;

    @JsonProperty("documentFormat")
    private String documentFormat;

    @JsonProperty("documentId")
    private String documentId;

    @JsonProperty("documentName")
    private String documentName;

    @JsonProperty("documentRefNo")
    private String documentRefNo;

    @JsonProperty("documentType")
    private String documentType;

    @JsonProperty("documentURL")
    private String documentURL;
}
