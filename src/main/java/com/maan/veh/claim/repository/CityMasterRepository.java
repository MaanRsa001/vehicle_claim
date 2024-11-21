package com.maan.veh.claim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.veh.claim.entity.CityMaster;
import com.maan.veh.claim.entity.CityMasterId;

@Repository
public interface CityMasterRepository extends JpaRepository<CityMaster, CityMasterId> {

	List<CityMaster> findByStatusAndCompanyId(String string, String companyId);

}
