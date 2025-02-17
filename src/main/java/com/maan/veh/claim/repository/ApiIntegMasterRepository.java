package com.maan.veh.claim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.veh.claim.entity.ApiIntegMaster;

@Repository
public interface ApiIntegMasterRepository extends JpaRepository<ApiIntegMaster, String> {
    
    List<ApiIntegMaster> findByCompanyIdAndApiTypeAndStatus(String companyId,String apiType,String status);

}
