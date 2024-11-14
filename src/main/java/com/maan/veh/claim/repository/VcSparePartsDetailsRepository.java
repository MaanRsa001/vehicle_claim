package com.maan.veh.claim.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.veh.claim.entity.VcSparePartsDetails;

@Repository
public interface VcSparePartsDetailsRepository extends JpaRepository<VcSparePartsDetails, String> {

	VcSparePartsDetails findByClaimNumber(String claimNo);
    // Custom query methods can be added here if needed
}

