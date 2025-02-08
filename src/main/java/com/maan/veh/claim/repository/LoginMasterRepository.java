package com.maan.veh.claim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.maan.veh.claim.entity.LoginMaster;
import com.maan.veh.claim.entity.LoginMasterId;

@Repository
public interface LoginMasterRepository extends JpaRepository<LoginMaster, LoginMasterId> {

	LoginMaster findByLoginIdIgnoreCaseAndPasswordAndStatus(String loginId, String password, String status);

	LoginMaster findByLoginId(String loginId);

	LoginMaster findByLoginIdIgnoreCaseAndPassword(String loginId, String password);

	List<LoginMaster> findByUserTypeAndCompanyId(String string,String companyId);

	List<LoginMaster> findByCompanyId(String companyId);

	@Query("SELECT MAX(CAST(l.oaCode AS int)) FROM LoginMaster l")
    Integer findMaxOaCode();

	List<LoginMaster> findByCoreAppCode(String coreAppCode);
}
