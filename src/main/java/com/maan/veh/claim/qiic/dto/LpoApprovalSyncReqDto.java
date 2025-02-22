package com.maan.veh.claim.qiic.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LpoApprovalSyncReqDto {
	
	@JsonProperty("claimNo")
    private String claimNo;

    @JsonProperty("lpoId")
    private String lpoId;
}
