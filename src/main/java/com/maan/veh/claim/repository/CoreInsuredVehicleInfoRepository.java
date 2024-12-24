package com.maan.veh.claim.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maan.veh.claim.entity.CoreInsuredVehicleInfo;
import com.maan.veh.claim.entity.CoreInsuredVehicleInfoId;

public interface CoreInsuredVehicleInfoRepository extends JpaRepository<CoreInsuredVehicleInfo, CoreInsuredVehicleInfoId> {

}
