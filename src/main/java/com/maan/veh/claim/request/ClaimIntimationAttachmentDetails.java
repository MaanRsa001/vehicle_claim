package com.maan.veh.claim.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimIntimationAttachmentDetails {
    @JsonProperty("DocumentDetails")
    private List<ClaimIntimationDocumentDetails> documentDetails;

   }
