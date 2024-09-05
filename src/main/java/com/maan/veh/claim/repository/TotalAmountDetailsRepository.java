package com.maan.veh.claim.repository;

import com.maan.veh.claim.entity.TotalAmountDetails;
import com.maan.veh.claim.entity.TotalAmountDetailsId;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TotalAmountDetailsRepository extends JpaRepository<TotalAmountDetails, TotalAmountDetailsId> {

	Optional<TotalAmountDetails> findByClaimNo(String claimNo);
    // You can add custom query methods here if needed

	List<TotalAmountDetails> findByCreatedBy(String createdBy);
}
