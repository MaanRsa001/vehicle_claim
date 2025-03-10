package com.maan.veh.claim.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class VcSparePartsDetailsId implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String claimNumber;
    private String quotationNo;
    private String damageSno;
    private String garageId;
}
