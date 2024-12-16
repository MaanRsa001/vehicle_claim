package com.maan.veh.claim.entity;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClaimIntimationDetailsId implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String policyNo; // Primary key
	private String policeReportNo;
}
