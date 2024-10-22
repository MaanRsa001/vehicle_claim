package com.maan.veh.claim.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maan.veh.claim.entity.ClaimIntimationDetails;
import com.maan.veh.claim.entity.ClaimIntimationDetailsId;

public interface ClaimIntimationDetailsRepository extends JpaRepository<ClaimIntimationDetails, ClaimIntimationDetailsId> {

	Optional<ClaimIntimationDetails> findByPolicyNo(String policyNo);
}