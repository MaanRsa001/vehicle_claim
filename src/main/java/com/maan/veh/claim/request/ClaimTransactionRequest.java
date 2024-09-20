package com.maan.veh.claim.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ClaimTransactionRequest {
	
	@JsonProperty("jwtToken")
    private String jwtToken;

    @JsonProperty("ClaimsTpReferenceNo")
    private String claimsTpReferenceNo;

    @JsonProperty("FnolNo")
    private String fnolNo;

    @JsonProperty("RequestMetaData")
    private ClaimTransactionRequestMetaData requestMetaData;

    @JsonProperty("TpPolicyReferenceNo")
    private String tpPolicyReferenceNo;

    @JsonProperty("TransactionRefNo")
    private String transactionRefNo;

    
}
