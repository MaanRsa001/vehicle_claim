package com.maan.veh.claim.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.veh.claim.entity.VcSparePartsDetails;
import com.maan.veh.claim.entity.VcSparePartsDetailsId;

@Repository
public interface VcSparePartsDetailsRepository extends JpaRepository<VcSparePartsDetails, VcSparePartsDetailsId> {

	VcSparePartsDetails findByClaimNumberAndQuotationNo(String claimNo,String quotationNo);
    // Custom query methods can be added here if needed
}

