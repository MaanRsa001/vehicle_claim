package com.maan.veh.claim.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ClaimTransactionRequestDTO {
	

    @JsonProperty("ClaimsTpReferenceNo")
    private String claimsTpReferenceNo;

    @JsonProperty("FnolNo")
    private String fnolNo;

    @JsonProperty("RequestMetaData")
    private ClaimTransactionRequestDTOMetaData requestMetaData;

    @JsonProperty("TpPolicyReferenceNo")
    private String tpPolicyReferenceNo;

    @JsonProperty("TransactionRefNo")
    private String transactionRefNo;

    
}

