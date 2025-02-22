package com.maan.veh.claim.qiic.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LpoApprovalSyncRequest {
	
	@JsonProperty("ClaimNo")
    private String claimNo;

    @JsonProperty("LpoId")
    private String lpoId;
    
    @JsonProperty("CompanyId")
    private String companyId;
}
