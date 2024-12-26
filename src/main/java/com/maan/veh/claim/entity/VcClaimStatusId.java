package com.maan.veh.claim.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VcClaimStatusId implements Serializable {

    private static final long serialVersionUID = 1L;

    private String claimNo;
    private String claimNotificationNo;
    private String policyNumber;
    private String claimStatus;
}
