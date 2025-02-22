package com.maan.veh.claim.qiic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class CreateWorkBasketRequestDto {

    @JsonProperty("claimNo")
    private String claimNo;

    @JsonProperty("fnolNo")
    private String fnolNo;

    @JsonProperty("prodId")
    private String prodId;

    @JsonProperty("clcpId")
    private String clcpId;
}
