package com.maan.veh.claim.repository;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import com.maan.veh.claim.entity.GarageWorkOrder;
import com.maan.veh.claim.entity.GarageWorkOrderId;

public interface GarageWorkOrderRepository extends JpaRepository<GarageWorkOrder, GarageWorkOrderId> , JpaSpecificationExecutor<GarageWorkOrder>  {

	GarageWorkOrder findByClaimNoAndCreatedBy(String claimNo, String createdBy);

	List<GarageWorkOrder> findByCreatedBy(String createdBy);

	GarageWorkOrder findByClaimNoAndQuotationNo(String claimNo, String quotationNo);

	List<GarageWorkOrder> findByGarageId(String garageId);

	List<GarageWorkOrder> findBySparepartsDealerId(String sparepartsDealerId);

	GarageWorkOrder findByClaimNoAndWorkOrderNo(String claimNo, String workOrderNo);

	Optional<GarageWorkOrder> findByClaimNoAndGarageId(String claimNo, String createdBy);

	List<GarageWorkOrder> findByClaimNo(String claimNo);


}
