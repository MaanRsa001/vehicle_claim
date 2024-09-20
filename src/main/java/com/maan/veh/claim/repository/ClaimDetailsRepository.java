package com.maan.veh.claim.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maan.veh.claim.entity.ClaimDetails;
import com.maan.veh.claim.entity.ClaimDetailsId;

public interface ClaimDetailsRepository extends JpaRepository<ClaimDetails, ClaimDetailsId> {

	ClaimDetails findByClaimReferenceNo(String claimReferenceNo);
    // Custom queries can be added here if needed
}
