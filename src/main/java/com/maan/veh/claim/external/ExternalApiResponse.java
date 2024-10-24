package com.maan.veh.claim.external;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ExternalApiResponse {

    @JsonProperty("hasError")
    private boolean hasError;

    @JsonProperty("status")
    private int status;

    @JsonProperty("data")
    private ExternalApiData data;

    @JsonProperty("message")
    private String message;

}

