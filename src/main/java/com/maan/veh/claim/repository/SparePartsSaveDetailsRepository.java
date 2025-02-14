package com.maan.veh.claim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.veh.claim.entity.SparePartsSaveDetails;
import com.maan.veh.claim.entity.SparePartsSaveDetailsId;

@Repository
public interface SparePartsSaveDetailsRepository extends JpaRepository<SparePartsSaveDetails,SparePartsSaveDetailsId>{

	SparePartsSaveDetails findByClaimNo(String claimNo);

	List<SparePartsSaveDetails> findByGarageCode(String coreAppCode);

	List<SparePartsSaveDetails> findByGarageCodeOrderByEntryDateDesc(String coreAppCode);

	SparePartsSaveDetails findByClaimNoAndGarageCode(String claimNo, String coreAppCode);

}
