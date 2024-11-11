package com.maan.veh.claim.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.maan.veh.claim.entity.VcDocumentUploadDetails;
import com.maan.veh.claim.entity.VcDocumentUploadDetailsId;

@Repository
public interface VcDocumentUploadDetailsRepository extends JpaRepository<VcDocumentUploadDetails, VcDocumentUploadDetailsId> {

	List<VcDocumentUploadDetails> findByClaimNo(String claimNo);

	List<VcDocumentUploadDetails> findAllByOrderByDocumentRefDesc();

	Optional<VcDocumentUploadDetails> findByClaimNoAndDocumentRef(String claimNo, Long documentRef);

}

