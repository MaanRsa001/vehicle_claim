package com.maan.veh.claim.qiic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimStatusUpdateRequestDto {

	@JsonProperty("claimNo")
    private String claimNo;

    @JsonProperty("prodId")
    private String prodId;
}
