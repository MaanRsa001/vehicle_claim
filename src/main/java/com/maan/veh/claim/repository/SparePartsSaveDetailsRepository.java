package com.maan.veh.claim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.veh.claim.entity.SparePartsSaveDetails;
import com.maan.veh.claim.entity.SparePartsSaveDetailsId;

@Repository
public interface SparePartsSaveDetailsRepository extends JpaRepository<SparePartsSaveDetails,SparePartsSaveDetailsId>{

	SparePartsSaveDetails findByClaimNo(String claimNo);

}
