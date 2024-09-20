package com.maan.veh.claim.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimIntimationDTOAttachmentDetails {
    @JsonProperty("DocumentDetails")
    private List<ClaimIntimationDTODocumentDetails> documentDetails;

    
}
