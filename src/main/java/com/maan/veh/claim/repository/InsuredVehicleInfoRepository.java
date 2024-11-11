package com.maan.veh.claim.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maan.veh.claim.entity.InsuredVehicleInfo;
import com.maan.veh.claim.entity.InsuredVehicleInfoId;

public interface InsuredVehicleInfoRepository extends JpaRepository<InsuredVehicleInfo, InsuredVehicleInfoId> {

	List<InsuredVehicleInfo> findByCompanyId(Integer companyId);

	List<InsuredVehicleInfo> findByCompanyIdAndStatus(Integer companyId, String status);


	Optional<InsuredVehicleInfo> findByClaimNo(String claimNo);

	List<InsuredVehicleInfo> findByCompanyIdAndStatusIn(Integer valueOf, List<String> status);

	List<InsuredVehicleInfo> findByClaimNoInAndStatusIn(List<String> claimNo, List<String> status);

	List<InsuredVehicleInfo> findByClaimNoIn(List<String> claimNoList);

	List<InsuredVehicleInfo> findByCompanyIdAndGarageId(Integer valueOf, String garageId);

   
}