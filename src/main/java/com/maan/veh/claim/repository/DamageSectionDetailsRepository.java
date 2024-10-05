package com.maan.veh.claim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.veh.claim.entity.DamageSectionDetails;
import com.maan.veh.claim.entity.DamageSectionDetailsId;

public interface DamageSectionDetailsRepository extends JpaRepository<DamageSectionDetails, DamageSectionDetailsId> , JpaSpecificationExecutor<DamageSectionDetails>{
    List<DamageSectionDetails> findByClaimNo(String claimNo);

	DamageSectionDetails findByClaimNoAndQuotationNoAndDamageSno(String claimNo, String quotationNo, Integer damageSno);

	List<DamageSectionDetails> findByClaimNoAndQuotationNo(String claimNo, String quotationNo);

	List<DamageSectionDetails> findByStatus(String string);

	List<DamageSectionDetails> findByStatusAndGarageLoginId(String string, String garageId);

	List<DamageSectionDetails> findByClaimNoIn(List<String> claimNumbers);

	List<DamageSectionDetails> findByClaimNoInAndStatusNotIn(List<String> claimNumbers, List<String> usertype);
}
