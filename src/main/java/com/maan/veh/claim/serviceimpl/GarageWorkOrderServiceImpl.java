package com.maan.veh.claim.serviceimpl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.veh.claim.entity.GarageWorkOrder;
import com.maan.veh.claim.repository.GarageWorkOrderRepository;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.ErrorList;
import com.maan.veh.claim.response.GarageWorkOrderResponse;
import com.maan.veh.claim.response.GarageWorkOrderSaveReq;
import com.maan.veh.claim.service.GarageWorkOrderService;

@Service
public class GarageWorkOrderServiceImpl implements GarageWorkOrderService {
	
	private static SimpleDateFormat DD_MM_YYYY = new SimpleDateFormat("dd/MM/yyyy");

    @Autowired
    private GarageWorkOrderRepository garageWorkOrderRepository;
    
    @Autowired
    private InputValidationUtil validation;

    @Override
    public List<GarageWorkOrderResponse> getGarageWorkOrders(String claimNo, String createdBy) {
    	List<GarageWorkOrderResponse> res = new ArrayList<>();
        try {
			List<GarageWorkOrder> data = garageWorkOrderRepository.findByClaimNoAndCreatedBy(claimNo, createdBy);
			
				for(GarageWorkOrder workOrder : data ) {
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
			         response.setJointOrderYn(workOrder.getJointOrderYn());
			         response.setSubrogationYn(workOrder.getSubrogationYn());
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
			         res.add(response);
				}
		} catch (Exception e) {
			e.printStackTrace();
		}
                   
               return res;
    }

	@Override
	public CommonResponse saveWorkOrder(GarageWorkOrderSaveReq req) {
		CommonResponse response = new CommonResponse();
		try {
			List<ErrorList> error =validation.validateWorkOrder(req);
			if(error.size()>0) {
				GarageWorkOrder work = new GarageWorkOrder();
				work.setClaimNo(req.getClaimNo());
				work.setWorkOrderNo(req.getWorkOrderNo());
				work.setWorkOrderType(req.getWorkOrderType());
				work.setWorkOrderDate(DD_MM_YYYY.parse(req.getWorkOrderDate()));
				work.setSettlementType(req.getSettlementType());
		        work.setSettlementTo(req.getSettlementTo());
		        work.setGarageName(req.getGarageName());
		        work.setGarageId(Integer.valueOf(req.getGarageId()));
		        work.setLocation(req.getLocation());
		        work.setRepairType(req.getRepairType());
		        work.setQuotationNo(req.getQuotationNo());
		        work.setDeliveryDate(DD_MM_YYYY.parse(req.getDeliveryDate()));
		        work.setJointOrderYn(req.getJointOrderYn());
		        work.setSubrogationYn(req.getSubrogationYn());
		        work.setTotalLoss(new BigDecimal(req.getTotalLoss()));
		        work.setLossType(req.getLossType());
		        work.setRemarks(req.getRemarks());
		        work.setCreatedBy(req.getCreatedBy());
		        work.setCreatedDate(new Date());
		        work.setUpdatedBy(req.getUpdatedBy());
		        work.setUpdatedDate(DD_MM_YYYY.parse(req.getUpdatedDate()));
		        work.setEntryDate(new Date());
		        work.setStatus("Y");
		        work.setSparepartsDealerId(Integer.valueOf(req.getSparepartsDealerId()));
		        
		        response.setErrors(Collections.emptyList());
		        response.setMessage("Success");
		        response.setResponse(Collections.emptyList());
			}else {
				 response.setErrors(error);
			     response.setMessage("Failed");
			     response.setResponse(Collections.emptyList());
			     response.setIsError(true);
			}
		}catch (Exception e) {
			e.printStackTrace();
			 response.setErrors(Collections.emptyList());
		     response.setMessage("Failed");
		     response.setResponse(e.getMessage());
		}
		return response;
	}
}
