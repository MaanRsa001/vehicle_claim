package com.maan.veh.claim.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.maan.veh.claim.entity.GarageWorkOrder;
import com.maan.veh.claim.entity.GarageWorkOrderId;
import com.maan.veh.claim.response.GarageWorkOrderResponse;

public interface GarageWorkOrderRepository extends JpaRepository<GarageWorkOrder, GarageWorkOrderId> {

	List<GarageWorkOrderResponse> findByClaimNoAndCreatedBy(String claimNo, String createdBy);
}
