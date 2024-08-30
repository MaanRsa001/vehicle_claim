package com.maan.veh.claim.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.veh.claim.entity.GarageWorkOrder;
import com.maan.veh.claim.entity.GarageWorkOrderId;

public interface GarageWorkOrderRepository extends JpaRepository<GarageWorkOrder, GarageWorkOrderId> , JpaSpecificationExecutor<GarageWorkOrder>  {

	List<GarageWorkOrder> findByClaimNoAndCreatedBy(String claimNo, String createdBy);
}
