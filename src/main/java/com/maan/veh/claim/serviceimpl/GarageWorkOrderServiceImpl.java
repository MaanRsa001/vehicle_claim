package com.maan.veh.claim.serviceimpl;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.veh.claim.repository.GarageWorkOrderRepository;
import com.maan.veh.claim.response.GarageWorkOrderResponse;
import com.maan.veh.claim.service.GarageWorkOrderService;

@Service
public class GarageWorkOrderServiceImpl implements GarageWorkOrderService {

    @Autowired
    private GarageWorkOrderRepository garageWorkOrderRepository;

    @Override
    public List<GarageWorkOrderResponse> getGarageWorkOrders(String claimNo, String createdBy) {
        return garageWorkOrderRepository.findByClaimNoAndCreatedBy(claimNo, createdBy)
                .stream()
                .map(workOrder -> {
                    GarageWorkOrderResponse response = new GarageWorkOrderResponse();
                    response.setClaimNo(workOrder.getClaimNo());
                    response.setWorkOrderNo(workOrder.getWorkOrderNo());
                    response.setWorkOrderType(workOrder.getWorkOrderType());
                    response.setWorkOrderDate(workOrder.getWorkOrderDate());
                    response.setSettlementType(workOrder.getSettlementType());
                    response.setSettlementTo(workOrder.getSettlementTo());
                    response.setGarageName(workOrder.getGarageName());
                    response.setGarageId(workOrder.getGarageId().toString());
                    response.setLocation(workOrder.getLocation());
                    response.setRepairType(workOrder.getRepairType());
                    response.setQuotationNo(workOrder.getQuotationNo());
                    response.setDeliveryDate(workOrder.getDeliveryDate());
                    response.setJointOrderYn(workOrder.getJointOrderYn().toString());
                    response.setSubrogationYn(workOrder.getSubrogationYn().toString());
                    response.setTotalLoss(workOrder.getTotalLoss().toString());
                    response.setLossType(workOrder.getLossType());
                    response.setRemarks(workOrder.getRemarks());
                    response.setCreatedBy(workOrder.getCreatedBy());
                    response.setCreatedDate(workOrder.getCreatedDate());
                    response.setUpdatedBy(workOrder.getUpdatedBy());
                    response.setUpdatedDate(workOrder.getUpdatedDate());
                    response.setEntryDate(workOrder.getEntryDate());
                    response.setStatus(workOrder.getStatus());
                    response.setSparepartsDealerId(workOrder.getSparepartsDealerId().toString());
                    return response;
                })
                .collect(Collectors.toList());
    }
}
