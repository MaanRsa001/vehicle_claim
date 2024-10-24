package com.maan.veh.claim.external;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ErrorDetail {

    @JsonProperty("errorSection")
    private String errorSection;

    @JsonProperty("errorDescription")
    private String errorDescription;

    @JsonProperty("errorField")
    private String errorField;

    @JsonProperty("errorCode")
    private String errorCode;


}

