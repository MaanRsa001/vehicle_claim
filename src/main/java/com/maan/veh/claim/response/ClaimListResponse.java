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

    @JsonProperty("HasError")
    private String hasError;

    @JsonProperty("Status")
    private String status;

    @JsonProperty("Data")
    private List<ClaimData> data;

    @JsonProperty("Message")
    private String message;

}
