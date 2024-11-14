package com.maan.veh.claim.serviceimpl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.veh.claim.entity.GarageWorkOrder;
import com.maan.veh.claim.entity.InsuranceCompanyMaster;
import com.maan.veh.claim.entity.InsuranceCompanyMasterId;
import com.maan.veh.claim.entity.InsuredVehicleInfo;
import com.maan.veh.claim.repository.InsuranceCompanyMasterRepository;
import com.maan.veh.claim.request.InsuranceCompanyMasterRequest;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.ErrorList;
import com.maan.veh.claim.response.InsuranceCompanyMasterResponse;
import com.maan.veh.claim.service.InsuranceCompanyMasterService;

@Service
public class InsuranceCompanyMasterServiceImpl implements InsuranceCompanyMasterService {

    @Autowired
    private InsuranceCompanyMasterRepository repository;
    
    @Autowired
    private InputValidationUtil validation;

    @Override
    public CommonResponse createCompany(InsuranceCompanyMasterRequest req) {
    	CommonResponse response = new CommonResponse();
//        try {
//            // Step 1: Validate the request
//            List<ErrorList> errors = validation.validateCreateCompany(req);
//            if (!errors.isEmpty()) {
//                // Return early if there are validation errors
//                response.setErrors(errors);
//                response.setMessage("Validation Failed");
//                response.setIsError(true);
//                response.setResponse(Collections.emptyList());
//                return response;
//            }
//
//            // Step 2: Find existing work order by claim 
//            Optional<GarageWorkOrder> optionalWorkOrder = garageWorkOrderRepository.findByClaimNo(req.getClaimNo());
//            GarageWorkOrder workOrder = new GarageWorkOrder();
//            if(optionalWorkOrder.isPresent()) {
//            	workOrder = optionalWorkOrder.get();
//            }
//
//            // Step 4: Set common work order fields
//            workOrder.setClaimNo(req.getClaimNo());
//            workOrder.setWorkOrderNo(req.getWorkOrderNo());
//            workOrder.setWorkOrderType(req.getWorkOrderType());
//            workOrder.setWorkOrderTypeDesc(req.getWorkOrderTypeDesc());
//
//            // Parse and set the work order date with error handling
//            try {
//                workOrder.setWorkOrderDate(DD_MM_YYYY.parse(req.getWorkOrderDate()));
//            } catch (ParseException e) {
//                response.setErrors(Collections.singletonList("Invalid work order date format."));
//                response.setMessage("Failed");
//                response.setIsError(true);
//                return response;
//            }
//
//            // Step 5: Set settlement details
//            workOrder.setSettlementType(req.getSettlementType());
//            workOrder.setSettlementTypeDesc(req.getSettlementTypeDesc());
//            workOrder.setSettlementTo(req.getSettlementTo());
//            workOrder.setSettlementToDesc(req.getSettlementToDesc());
//
//            // Step 6: Set optional fields
//            if (StringUtils.isNotBlank(req.getGarageName())) {
//                workOrder.setGarageName(req.getGarageName());
//            }
//            if (StringUtils.isNotBlank(req.getGarageId())) {
//                workOrder.setGarageId(req.getGarageId());
//            }
//            workOrder.setLocation(req.getLocation());
//            workOrder.setRepairType(req.getRepairType());
//
//            // Step 7: Handle Quotation Number
//            if (StringUtils.isNotBlank(req.getQuotationNo())) {
//                workOrder.setQuotationNo(req.getQuotationNo());
//            } else {
//                long count = garageWorkOrderRepository.count();
//                String quoteNo = "QUO-" + req.getClaimNo() + "-" + count;
//                workOrder.setQuotationNo(quoteNo);
//            }
//
//            // Step 8: Set delivery and other dates
//            try {
//                workOrder.setDeliveryDate(DD_MM_YYYY.parse(req.getDeliveryDate()));
//            } catch (ParseException e) {
//                response.setErrors(Collections.singletonList("Invalid delivery date format."));
//                response.setMessage("Failed");
//                response.setIsError(true);
//                return response;
//            }
//
//            workOrder.setJointOrderYn(req.getJointOrderYn());
//            workOrder.setSubrogationYn(req.getSubrogationYn());
//
//            // Step 9: Financial details
//            workOrder.setTotalLoss(!StringUtils.isBlank(req.getTotalLoss())? new BigDecimal(req.getTotalLoss()) : BigDecimal.ZERO);
//            workOrder.setLossType(req.getLossType());
//            workOrder.setRemarks(req.getRemarks());
//
//            // Step 10: Set audit fields
//            workOrder.setCreatedBy(req.getCreatedBy());
//            workOrder.setCreatedDate(new Date());
//            workOrder.setUpdatedBy(req.getUpdatedBy());
//            workOrder.setUpdatedDate(new Date());
//            workOrder.setEntryDate(new Date());
//
//            // Step 11: Status and Quote Status
//            workOrder.setStatus(req.getUserType()); 
//            
//            workOrder.setQuoteStatus(req.getQuoteStatus());
//
//            // Step 12: Set Spareparts Dealer ID (optional)
//            workOrder.setSparepartsDealerId(StringUtils.isBlank(req.getSparepartsDealerId()) ? null : req.getSparepartsDealerId());
//
//            // Step 13: Save the work order
//            garageWorkOrderRepository.save(workOrder);
//
//            // Step 14: Update Insured Vehicle Status based on Quote Status
//            Optional<InsuredVehicleInfo> optionalInsuredVeh = insuredVehRepo.findByClaimNo(req.getClaimNo());
//            if (optionalInsuredVeh.isPresent()) {
//                InsuredVehicleInfo insuredVeh = optionalInsuredVeh.get();
//                insuredVeh.setStatus(req.getQuoteStatus());
//                insuredVehRepo.save(insuredVeh);
//            } else {
//                response.setErrors(Collections.singletonList("No insured vehicle found for claim number: " + req.getClaimNo()));
//                response.setMessage("Failed");
//                response.setIsError(true);
//                return response;
//            }
//            
//            // Step 15: Prepare response
//            Map<String, String> resMap = new HashMap<>();
//            resMap.put("ClaimNo", workOrder.getClaimNo());
//            resMap.put("QuotationNo", workOrder.getQuotationNo());
//
//            response.setErrors(Collections.emptyList());
//            response.setMessage("Success");
//            response.setResponse(resMap);
//
//        } catch (Exception e) {
//            // Handle any unexpected exceptions
//            e.printStackTrace();
//            response.setErrors(Collections.singletonList("An unexpected error occurred: " + e.getMessage()));
//            response.setMessage("Failed");
//            response.setIsError(true);
//            response.setResponse(Collections.emptyList());
//        }

        return response;
    }

    @Override
    public CommonResponse getCompany(String companyId) {
        InsuranceCompanyMaster company = repository.findByCompanyId(companyId);
        if (company == null) {
            return new CommonResponse("Company not found", null, true);
        }
        InsuranceCompanyMasterResponse response = mapToResponse(company);
        return new CommonResponse("Company retrieved successfully", response, false);
    }

    @Override
    public CommonResponse updateCompany(String companyId, InsuranceCompanyMasterRequest request) {
        InsuranceCompanyMaster company = repository.findByCompanyId(companyId);
        if (company == null) {
            return new CommonResponse("Company not found", null, true);
        }
        updateEntity(company, request);
        repository.save(company);
        return new CommonResponse("Company updated successfully", null, false);
    }

    @Override
    public CommonResponse deleteCompany(String companyId) {
        repository.deleteById(new InsuranceCompanyMasterId(companyId, null));
        return new CommonResponse("Company deleted successfully", null, false);
    }

    private InsuranceCompanyMaster mapToEntity(InsuranceCompanyMasterRequest request) {
		return null;
        // Map request to entity, converting Strings to appropriate data types
        // Implementation details go here...
    }

    private InsuranceCompanyMasterResponse mapToResponse(InsuranceCompanyMaster entity) {
		return null;
        // Map entity to response, converting data types to Strings
        // Implementation details go here...
    }

    private void updateEntity(InsuranceCompanyMaster entity, InsuranceCompanyMasterRequest request) {
        // Update entity fields based on request data
        // Implementation details go here...
    }
}
