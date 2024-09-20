package com.maan.veh.claim.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class ClaimDetailsId implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String claimNo;
    private String policyNo;

    }