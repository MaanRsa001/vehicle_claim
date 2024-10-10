package com.maan.veh.claim.entity;
import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class VcClaimStatusId implements Serializable {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Long id;
    private String claimNo;

}

