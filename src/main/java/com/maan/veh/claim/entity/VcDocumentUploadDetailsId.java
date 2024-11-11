package com.maan.veh.claim.entity;
import java.io.Serializable;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VcDocumentUploadDetailsId implements Serializable {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String claimNo;
    private int documentRef;
    private int companyId;
}

