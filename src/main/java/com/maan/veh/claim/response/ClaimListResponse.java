package com.maan.veh.claim.response;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClaimListResponse {

    @JsonProperty("hasError")
    private String hasError;

    @JsonProperty("status")
    private String status;

    @JsonProperty("data")
    private List<ClaimData> data;

    @JsonProperty("message")
    private String message;

}
