package com.maan.veh.claim.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VcSparePartsDetailsId  implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String claimNumber;
	
	private String quotationNo;
}
