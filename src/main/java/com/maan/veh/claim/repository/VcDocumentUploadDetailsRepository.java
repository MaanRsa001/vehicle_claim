package com.maan.veh.claim.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.maan.veh.claim.entity.VcDocumentUploadDetails;
import com.maan.veh.claim.entity.VcDocumentUploadDetailsId;

@Repository
public interface VcDocumentUploadDetailsRepository extends JpaRepository<VcDocumentUploadDetails, VcDocumentUploadDetailsId> {

	 @Query(value = "SELECT API_CHECK FROM CLAIM_DOCUMENT_MASTER WHERE DOCUMENT_ID=?1 AND PRODUCT_ID=?2 AND UPPER(TRIM(API_CHECK_NAME))=UPPER(TRIM(?3)) AND STATUS='Y' AND AMEND_ID=(SELECT MAX(AMEND_ID) FROM CLAIM_DOCUMENT_MASTER WHERE DOCUMENT_ID=?1 AND PRODUCT_ID=?2)", nativeQuery = true)
	String getapicheck(String docid, String productid, String apiname);
}

