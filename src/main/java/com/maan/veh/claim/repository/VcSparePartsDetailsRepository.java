package com.maan.veh.claim.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.veh.claim.entity.VcSparePartsDetails;
import com.maan.veh.claim.entity.VcSparePartsDetailsId;

@Repository
public interface VcSparePartsDetailsRepository extends JpaRepository<VcSparePartsDetails, VcSparePartsDetailsId> {

	List<VcSparePartsDetails> findByClaimNumberAndQuotationNoAndGarageId(String claimNo, String quotationNo,
			String garageLoginId);

	VcSparePartsDetails findByClaimNumberAndQuotationNoAndDamageSnoAndGarageId(String claimNo, String quotationNo,
			String damageSno, String garageId);
}

