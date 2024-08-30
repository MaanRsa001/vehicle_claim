package com.maan.veh.claim.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.veh.claim.entity.BranchMaster;
import com.maan.veh.claim.entity.BranchMasterId;

@Repository
public interface BranchMasterRepository  extends JpaRepository<BranchMaster,BranchMasterId>{

	List<BranchMaster> findByBranchCode(String branchCode);

}
