package com.maan.veh.claim.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.veh.claim.entity.VcClaimStatus;
import com.maan.veh.claim.entity.VcClaimStatusId;

@Repository
public interface VcClaimStatusRepository extends JpaRepository<VcClaimStatus, VcClaimStatusId> {

	Optional<VcClaimStatus> findByClaimNo(String claimNo);
  
}

