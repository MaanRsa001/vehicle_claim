package com.maan.veh.claim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.veh.claim.entity.CountryMaster;
import com.maan.veh.claim.entity.CountryMasterId;

@Repository
public interface CountryMasterRepository extends JpaRepository<CountryMaster, CountryMasterId> {

	List<CountryMaster> findByStatusAndCompanyId(String string, String companyId);

}
