package com.maan.veh.claim.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.veh.claim.entity.VcFlowMaster;
import com.maan.veh.claim.entity.VcFlowMasterId;

@Repository
public interface VcFlowMasterRepository extends JpaRepository<VcFlowMaster, VcFlowMasterId> {

	List<VcFlowMaster> findByUsertype(String string);

	List<VcFlowMaster> findByUsertypeAndStatusId(String string, String quoteStatus);

	List<VcFlowMaster> findByStatusId(String quoteStatus);

	List<VcFlowMaster> findByUsertypeAndCompanyId(String usertype, String companyId);

	List<VcFlowMaster> findByUsertypeAndCompanyIdAndFlowId(String usertype, String companyId, String flowId);

}

