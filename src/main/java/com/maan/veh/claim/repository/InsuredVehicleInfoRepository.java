package com.maan.veh.claim.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maan.veh.claim.entity.InsuredVehicleInfo;
import com.maan.veh.claim.entity.InsuredVehicleInfoId;

public interface InsuredVehicleInfoRepository extends JpaRepository<InsuredVehicleInfo, InsuredVehicleInfoId> {

	List<InsuredVehicleInfo> findByCompanyId(Integer companyId);

	List<InsuredVehicleInfo> findByCompanyIdAndStatus(Integer companyId, String status);

	List<InsuredVehicleInfo> findByCompanyIdAndStatusIn(Integer valueOf, List<String> status);

	List<InsuredVehicleInfo> findByClaimNoInAndStatusIn(List<String> claimNo, List<String> status);

	List<InsuredVehicleInfo> findByCompanyIdAndGarageId(Integer valueOf, String garageId);

	Optional<InsuredVehicleInfo> findByClaimNoAndGarageId(String claimNo, String garageId);

	List<InsuredVehicleInfo> findByClaimNoInAndGarageId(List<String> claimWithReplacement, String garageId);

	List<InsuredVehicleInfo> findByClaimNoIn(List<String> claimNumbers);

   
}