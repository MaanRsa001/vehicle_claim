package com.maan.veh.claim.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.veh.claim.entity.MenuMaster;
import com.maan.veh.claim.entity.MenuMasterId;

@Repository
public interface MenuMasterRepository extends JpaRepository<MenuMaster, MenuMasterId> {


	List<MenuMaster> findByCompanyIdAndUsertypeIgnoreCase(String companyId, String userType);

	List<MenuMaster> findByCompanyIdAndStatusAndUsertypeIgnoreCase(String companyId, String string, String userType);


}
