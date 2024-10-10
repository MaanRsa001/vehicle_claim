package com.maan.veh.claim.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.veh.claim.entity.VcFlowMaster;
import com.maan.veh.claim.entity.VcFlowMasterId;

@Repository
public interface VcFlowMasterRepository extends JpaRepository<VcFlowMaster, VcFlowMasterId> {

}

