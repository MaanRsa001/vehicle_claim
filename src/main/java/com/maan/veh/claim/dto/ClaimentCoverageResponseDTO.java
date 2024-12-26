package com.maan.veh.claim.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimentCoverageResponseDTO {
	
	@JsonProperty("hasError")
    private boolean hasError;

    @JsonProperty("status")
    private int status;

    @JsonProperty("data")
    private Object data; // Can be replaced with a specific type if needed.

    @JsonProperty("dataset")
    private Dataset dataset;

    @JsonProperty("message")
    private String message;

    @Data
    public static class Dataset {

        @JsonProperty("claimantList")
        private List<String> claimantList;

        @JsonProperty("coveragesList")
        private List<String> coveragesList;
    }
}
