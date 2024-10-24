package com.maan.veh.claim.external;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {

    @JsonProperty("Code")
    private String code;

    @JsonProperty("Field")
    private String field;

    @JsonProperty("Message")
    private String message;

}

