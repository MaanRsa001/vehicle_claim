package com.maan.veh.claim.serviceimpl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.veh.claim.entity.DamageSectionDetails;
import com.maan.veh.claim.entity.GarageWorkOrder;
import com.maan.veh.claim.entity.InsuredVehicleInfo;
import com.maan.veh.claim.entity.SparePartsSaveDetails;
import com.maan.veh.claim.entity.VcSparePartsDetails;
import com.maan.veh.claim.repository.DamageSectionDetailsRepository;
import com.maan.veh.claim.repository.GarageWorkOrderRepository;
import com.maan.veh.claim.repository.InsuredVehicleInfoRepository;
import com.maan.veh.claim.repository.SparePartsSaveDetailsRepository;
import com.maan.veh.claim.repository.VcSparePartsDetailsRepository;
import com.maan.veh.claim.request.GarageWorkOrderRequest;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.ErrorList;
import com.maan.veh.claim.response.GarageWorkOrderResponse;
import com.maan.veh.claim.response.GarageWorkOrderSaveReq;
import com.maan.veh.claim.response.GetAllQuoteResponse;
import com.maan.veh.claim.service.GarageWorkOrderService;

@Service
public class GarageWorkOrderServiceImpl implements GarageWorkOrderService {
	
	private static SimpleDateFormat DD_MM_YYYY = new SimpleDateFormat("dd/MM/yyyy");

    @Autowired
    private GarageWorkOrderRepository garageWorkOrderRepository;
    
    @Autowired
    private InsuredVehicleInfoRepository insuredVehRepo;
    
    @Autowired
    private InputValidationUtil validation;
    
    @Autowired
	private DamageSectionDetailsRepository damageRepository;
    
    @Autowired
	private VcSparePartsDetailsRepository sparePartsDetailsRepo;
    
    @Autowired
    private SparePartsSaveDetailsRepository SparePartsSaveDetailsRepo;

    @Override
    public CommonResponse getGarageWorkOrders(GarageWorkOrderRequest req) {
    	CommonResponse comResponse = new CommonResponse(); 
        try {
			List<GarageWorkOrder> data = garageWorkOrderRepository.findByCreatedBy(req.getCreatedBy());
			
			if(data.size()>0) {
		    	List<GarageWorkOrderResponse> res = new ArrayList<>();
				for(GarageWorkOrder workOrder : data ) {
					 GarageWorkOrderResponse response = new GarageWorkOrderResponse();
			         response.setClaimNo(workOrder.getClaimNo());
			         response.setWorkOrderNo(workOrder.getWorkOrderNo());
			         response.setWorkOrderType(workOrder.getWorkOrderType());
			         response.setWorkOrderTypeDesc(workOrder.getWorkOrderTypeDesc());
			         response.setWorkOrderDate(workOrder.getWorkOrderDate());
			         response.setSettlementType(workOrder.getSettlementType());
			         response.setSettlementTypeDesc(workOrder.getSettlementTypeDesc());
			         response.setSettlementTo(workOrder.getSettlementTo());
			         response.setSettlementToDesc(workOrder.getSettlementToDesc());
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
			         response.setSparepartsDealerId(Optional.ofNullable(workOrder.getSparepartsDealerId()).map(String ::valueOf).orElse(""));
			         response.setQuoteStatus(workOrder.getQuoteStatus());			         res.add(response);
				}
				
				comResponse.setErrors(Collections.emptyList());
				comResponse.setMessage("Success");
				comResponse.setResponse(res);
			
			}else {
				
				comResponse.setErrors(Collections.emptyList());
				comResponse.setMessage("Failed");
				comResponse.setResponse(Collections.emptyList());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
                   
         return comResponse;
    }

    @Override
    public CommonResponse saveWorkOrder(GarageWorkOrderSaveReq req) {
        CommonResponse response = new CommonResponse();
        try {
            // Step 1: Validate the request
            List<ErrorList> errors = validation.validateWorkOrder(req);
            if (!errors.isEmpty()) {
                // Return early if there are validation errors
                response.setErrors(errors);
                response.setMessage("Validation Failed");
                response.setIsError(true);
                response.setResponse(Collections.emptyList());
                return response;
            }

            // Step 2: Find existing work order by claim 
            Optional<GarageWorkOrder> optionalWorkOrder = garageWorkOrderRepository.findByClaimNoAndGarageId(req.getClaimNo(),req.getGarageId());
            GarageWorkOrder workOrder = new GarageWorkOrder();
            if(optionalWorkOrder.isPresent()) {
            	workOrder = optionalWorkOrder.get();
            }

            // Step 4: Set common work order fields
            workOrder.setClaimNo(req.getClaimNo());
            workOrder.setWorkOrderNo(req.getWorkOrderNo());
            workOrder.setWorkOrderType(req.getWorkOrderType());
            workOrder.setWorkOrderTypeDesc(req.getWorkOrderTypeDesc());

            // Parse and set the work order date with error handling
            //try {
                workOrder.setWorkOrderDate(req.getWorkOrderDate());
//            } catch (ParseException e) {
//                response.setErrors(Collections.singletonList("Invalid work order date format."));
//                response.setMessage("Failed");
//                response.setIsError(true);
//                return response;
//            }

            // Step 5: Set settlement details
            workOrder.setSettlementType(req.getSettlementType());
            workOrder.setSettlementTypeDesc(req.getSettlementTypeDesc());
            workOrder.setSettlementTo(req.getSettlementTo());
            workOrder.setSettlementToDesc(req.getSettlementToDesc());

            // Step 6: Set optional fields
            if (StringUtils.isNotBlank(req.getGarageName())) {
                workOrder.setGarageName(req.getGarageName());
            }
            if (StringUtils.isNotBlank(req.getGarageId())) {
                workOrder.setGarageId(req.getGarageId());
            }
            workOrder.setLocation(req.getLocation());
            workOrder.setRepairType(req.getRepairType());

            // Step 7: Handle Quotation Number
            if (StringUtils.isNotBlank(req.getQuotationNo())) {
                workOrder.setQuotationNo(req.getQuotationNo());
            } else {
            	
                long count = garageWorkOrderRepository.count();
                count = count + 1;
                //String quoteNo = "QUO-" + req.getClaimNo() + "-" + count ;
                String extractedValue = req.getClaimNo().substring(req.getClaimNo().lastIndexOf("/") + 1);
                String quoteNo = "QUO-" + extractedValue +"-" +count ;
                workOrder.setQuotationNo(quoteNo);
            }

            // Step 8: Set delivery and other dates
            //try {
                workOrder.setDeliveryDate(req.getDeliveryDate());
//            } catch (ParseException e) {
//                response.setErrors(Collections.singletonList("Invalid delivery date format."));
//                response.setMessage("Failed");
//                response.setIsError(true);
//                return response;
//            }

            workOrder.setJointOrderYn(req.getJointOrderYn());
            workOrder.setSubrogationYn(req.getSubrogationYn());

            // Step 9: Financial details
            workOrder.setTotalLoss(!StringUtils.isBlank(req.getTotalLoss())? new BigDecimal(req.getTotalLoss()) : BigDecimal.ZERO);
            workOrder.setLossType(req.getLossType());
            workOrder.setRemarks(req.getRemarks());

            // Step 10: Set audit fields
            workOrder.setCreatedBy(req.getCreatedBy());
            workOrder.setCreatedDate(new Date());
            workOrder.setUpdatedBy(req.getUpdatedBy());
            workOrder.setUpdatedDate(new Date());
            workOrder.setEntryDate(new Date());

            // Step 11: Status and Quote Status
            workOrder.setStatus(req.getUserType()); 
            
            workOrder.setQuoteStatus(req.getQuoteStatus());

            // Step 12: Set Spareparts Dealer ID (optional)
            workOrder.setSparepartsDealerId(StringUtils.isBlank(req.getSparepartsDealerId()) ? null : req.getSparepartsDealerId());

            // Step 13: Save the work order
            garageWorkOrderRepository.save(workOrder);

            // Step 14: Update Insured Vehicle Status based on Quote Status
            Optional<InsuredVehicleInfo> optionalInsuredVeh = insuredVehRepo.findByClaimNoAndGarageId(req.getClaimNo(),req.getGarageId());
            if (optionalInsuredVeh.isPresent()) {
                InsuredVehicleInfo insuredVeh = optionalInsuredVeh.get();
                insuredVeh.setStatus(req.getQuoteStatus());
                insuredVeh.setQuotationNo(workOrder.getQuotationNo());
                insuredVehRepo.save(insuredVeh);
			} else if(StringUtils.isNotBlank(req.getFnolSgsId())) {
				// Instantiate a new InsuredVehicleInfo object
//				InsuredVehicleInfo newInsuredVeh = new InsuredVehicleInfo();
//
//				// Map fields from GarageWorkOrderSaveReq to InsuredVehicleInfo
//				newInsuredVeh.setCompanyId(req.getCompanyId() != null ? Integer.valueOf(req.getCompanyId()) : null);
//				newInsuredVeh.setFnolSgsId(req.getFnolSgsId());
//				newInsuredVeh.setPolicyNo(req.getPolicyNo());
//				newInsuredVeh.setClaimNo(req.getClaimNo());
//				newInsuredVeh.setVehicleMake(req.getVehicleMake());
//				newInsuredVeh.setVehicleModel(req.getVehicleModel());
//				newInsuredVeh.setMakeYear(req.getMakeYear() != null ? Integer.valueOf(req.getMakeYear()) : null);
//				newInsuredVeh.setChassisNo(req.getChassisNo());
//				newInsuredVeh.setInsuredName(req.getInsuredName());
//				newInsuredVeh.setType(req.getType());
//				newInsuredVeh.setVehicleRegNo(req.getVehicleRegNo());
//				newInsuredVeh.setEntryDate(req.getEntryDate());
//				newInsuredVeh.setStatus(req.getStatus());
//				newInsuredVeh.setGarageId(req.getGarageId());
//				newInsuredVeh.setQuotationNo(req.getQuotationNo());
//				insuredVehRepo.save(newInsuredVeh);
////                response.setErrors(Collections.singletonList("No insured vehicle found for claim number: " + req.getClaimNo()));
////                response.setMessage("Failed");
////                response.setIsError(true);
////                return response;
			}
            
            // Step 15: Prepare response
            Map<String, String> resMap = new HashMap<>();
            resMap.put("ClaimNo", workOrder.getClaimNo());
            resMap.put("QuotationNo", workOrder.getQuotationNo());

            response.setErrors(Collections.emptyList());
            response.setMessage("Success");
            response.setResponse(resMap);

        } catch (Exception e) {
            // Handle any unexpected exceptions
            e.printStackTrace();
            response.setErrors(Collections.singletonList("An unexpected error occurred: " + e.getMessage()));
            response.setMessage("Failed");
            response.setIsError(true);
            response.setResponse(Collections.emptyList());
        }

        return response;
    }

	
	
    @Override
    public CommonResponse getGarageWorkOrdersByClaimNo(GarageWorkOrderRequest request) {
        CommonResponse response = new CommonResponse(); 
        try {
            // Fetch the work order using Optional to avoid NullPointerException
            Optional<GarageWorkOrder> optionalWorkOrder = garageWorkOrderRepository.findByClaimNoAndGarageId(request.getClaimNo(),request.getGarageLoginId());

            if (optionalWorkOrder.isPresent()) {
                // Work order found, map its fields to the response DTO
                GarageWorkOrder data = optionalWorkOrder.get();
                GarageWorkOrderResponse garage = new GarageWorkOrderResponse();
                
                // Set fields in GarageWorkOrderResponse
                garage.setClaimNo(data.getClaimNo());
                garage.setWorkOrderNo(data.getWorkOrderNo());
                garage.setWorkOrderType(data.getWorkOrderType());
                garage.setWorkOrderTypeDesc(data.getWorkOrderTypeDesc());
                garage.setWorkOrderDate(data.getWorkOrderDate());
                garage.setSettlementType(data.getSettlementType());
                garage.setSettlementTypeDesc(data.getSettlementTypeDesc());
                garage.setSettlementTo(data.getSettlementTo());
                garage.setSettlementToDesc(data.getSettlementToDesc());
                garage.setGarageName(data.getGarageName());
                garage.setGarageId(Optional.ofNullable(data.getGarageId()).map(String::valueOf).orElse(null));  // Handle nullable values
                garage.setLocation(data.getLocation());
                garage.setRepairType(data.getRepairType());
                garage.setQuotationNo(data.getQuotationNo());
                garage.setDeliveryDate(data.getDeliveryDate());
                garage.setJointOrderYn(data.getJointOrderYn());
                garage.setSubrogationYn(data.getSubrogationYn());
                garage.setTotalLoss(Optional.ofNullable(data.getTotalLoss()).map(BigDecimal::toString).orElse(null)); // Handle nullable BigDecimal
                garage.setLossType(data.getLossType());
                garage.setRemarks(data.getRemarks());
                garage.setCreatedBy(data.getCreatedBy());
                garage.setCreatedDate(data.getCreatedDate());
                garage.setUpdatedBy(data.getUpdatedBy());
                garage.setUpdatedDate(data.getUpdatedDate());
                garage.setEntryDate(data.getEntryDate());
                garage.setStatus(data.getStatus());
                garage.setSparepartsDealerId(Optional.ofNullable(data.getSparepartsDealerId()).map(String::valueOf).orElse(null));
                garage.setQuoteStatus(data.getQuoteStatus());
                List<DamageSectionDetails> damageList = damageRepository.findByClaimNoAndQuotationNo(data.getClaimNo(), data.getQuotationNo());

                boolean foundReplace = damageList.stream()
                    .anyMatch(damage -> "REPLACE".equalsIgnoreCase(damage.getRepairReplace()));

                garage.setDealerYn(foundReplace ? "Y" : "N");

                // Success response
                response.setErrors(Collections.emptyList());
                response.setMessage("Success");
                response.setResponse(garage);

            } else {
                // No work order found for the given claim number
                //response.setErrors(Collections.singletonList("No work order found for claim number: " + request.getClaimNo()));
                response.setMessage("Failed");
                response.setResponse(Collections.emptyList());
                response.setIsError(true);
            }
        } catch (Exception e) {
            // Log the exception and return a failure response
            e.printStackTrace();
            response.setErrors(Collections.singletonList("An unexpected error occurred: " + e.getMessage()));
            response.setMessage("Failed");
            response.setIsError(true);
            response.setResponse(Collections.emptyList());
        }

        return response;
    }


	@Override
	public CommonResponse getAllGarageWorkOrders(GarageWorkOrderRequest request) {
		CommonResponse comResponse = new CommonResponse(); 
        try {
			List<GarageWorkOrder> data = garageWorkOrderRepository.findAll();
			
			if(data.size()>0) {
		    	List<GarageWorkOrderResponse> res = new ArrayList<>();
				for(GarageWorkOrder workOrder : data ) {
					 GarageWorkOrderResponse response = new GarageWorkOrderResponse();
			         response.setClaimNo(workOrder.getClaimNo());
			         response.setWorkOrderNo(workOrder.getWorkOrderNo());
			         response.setWorkOrderType(workOrder.getWorkOrderType());
			         response.setWorkOrderTypeDesc(workOrder.getWorkOrderTypeDesc());
			         response.setWorkOrderDate(workOrder.getWorkOrderDate());
			         response.setSettlementType(workOrder.getSettlementType());
			         response.setSettlementTypeDesc(workOrder.getSettlementTypeDesc());
			         response.setSettlementTo(workOrder.getSettlementTo());
			         response.setSettlementToDesc(workOrder.getSettlementToDesc());
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
			         response.setSparepartsDealerId(Optional.ofNullable(workOrder.getSparepartsDealerId()).map(String ::valueOf).orElse(""));
			         response.setQuoteStatus(workOrder.getQuoteStatus());
			         
			         res.add(response);
				}
				
				comResponse.setErrors(Collections.emptyList());
				comResponse.setMessage("Success");
				comResponse.setResponse(res);
			
			}else {
				
				comResponse.setErrors(Collections.emptyList());
				comResponse.setMessage("Failed");
				comResponse.setResponse(Collections.emptyList());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
                   
         return comResponse;
	}

	@Override
	public CommonResponse assignWorkOrder(GarageWorkOrderSaveReq req) {
		CommonResponse response = new CommonResponse();
        try {
            // Step 1: Validate the request
            List<ErrorList> errors = validation.validateAssignWorkOrder(req);
            if (!errors.isEmpty()) {
                // Return early if there are validation errors
                response.setErrors(errors);
                response.setMessage("Validation Failed");
                response.setIsError(true);
                response.setResponse(Collections.emptyList());
                return response;
            }

            // Step 2: Find existing work order by claim 
            Optional<GarageWorkOrder> optionalWorkOrder = garageWorkOrderRepository.findByClaimNoAndGarageId(req.getClaimNo(),req.getGarageId());
            GarageWorkOrder workOrder = new GarageWorkOrder();
            if(optionalWorkOrder.isPresent()) {
            	workOrder = optionalWorkOrder.get();
            }

            // Step 4: Set common work order fields
            workOrder.setClaimNo(req.getClaimNo());
            workOrder.setWorkOrderNo(req.getWorkOrderNo());
            workOrder.setWorkOrderType(req.getWorkOrderType());
            workOrder.setWorkOrderTypeDesc(req.getWorkOrderTypeDesc());
            workOrder.setWorkOrderDate(req.getWorkOrderDate());

            // Step 5: Set settlement details
            workOrder.setSettlementType(req.getSettlementType());
            workOrder.setSettlementTypeDesc(req.getSettlementTypeDesc());
            workOrder.setSettlementTo(req.getSettlementTo());
            workOrder.setSettlementToDesc(req.getSettlementToDesc());

            // Step 6: Set optional fields
            if (StringUtils.isNotBlank(req.getGarageName())) {
                workOrder.setGarageName(req.getGarageName());
            }
            if (StringUtils.isNotBlank(req.getGarageId())) {
                workOrder.setGarageId(req.getGarageId());
            }
            workOrder.setLocation(req.getLocation());
            workOrder.setRepairType(req.getRepairType());

            workOrder.setDeliveryDate(req.getDeliveryDate());

            workOrder.setJointOrderYn(req.getJointOrderYn());
            workOrder.setSubrogationYn(req.getSubrogationYn());

            // Step 9: Financial details
            workOrder.setTotalLoss(!StringUtils.isBlank(req.getTotalLoss())? new BigDecimal(req.getTotalLoss()) : BigDecimal.ZERO);
            workOrder.setLossType(req.getLossType());
            workOrder.setRemarks(req.getRemarks());

            // Step 10: Set audit fields
            workOrder.setCreatedBy(req.getCreatedBy());
            workOrder.setCreatedDate(new Date());
            workOrder.setUpdatedBy(req.getUpdatedBy());
            workOrder.setUpdatedDate(new Date());
            workOrder.setEntryDate(new Date());

            // Step 11: Status and Quote Status
            workOrder.setStatus(req.getUserType()); 
            
            
           // Step 12: Set Spareparts Dealer ID (optional)
            workOrder.setSparepartsDealerId(StringUtils.isBlank(req.getSparepartsDealerId()) ? null : req.getSparepartsDealerId());

            workOrder.setQuoteStatus(req.getQuoteStatus());
            
            List<DamageSectionDetails> damageList = damageRepository.findByClaimNoAndQuotationNo(workOrder.getClaimNo(), workOrder.getQuotationNo());

            boolean foundReplace = damageList.stream()
                .anyMatch(damage -> "REPLACE".equalsIgnoreCase(damage.getRepairReplace()));
            
            //if workorder have no Replace damage , surveyor can't asign a dealer
            if("DA".equalsIgnoreCase(req.getQuoteStatus()) && !foundReplace) {
            	errors.add(new ErrorList("100", "Dealer Asigned", "No Replace Damage Present for the Quotation Can't be Asigned to Dealer"));
            	response.setErrors(errors);
                response.setMessage("Validation Failed");
                response.setIsError(true);
                response.setResponse(Collections.emptyList());
                return response;
            }
            
            // Step 13: Save the work order
            garageWorkOrderRepository.save(workOrder);

            // Step 14: Update Insured Vehicle Status based on Quote Status
            Optional<InsuredVehicleInfo> optionalInsuredVeh = insuredVehRepo.findByClaimNoAndGarageId(req.getClaimNo(),req.getGarageId());
            if (optionalInsuredVeh.isPresent()) {
                InsuredVehicleInfo insuredVeh = optionalInsuredVeh.get();
                insuredVeh.setStatus(req.getQuoteStatus());
                insuredVehRepo.save(insuredVeh);
            } else {
                response.setErrors(Collections.singletonList("No insured vehicle found for claim number: " + req.getClaimNo()));
                response.setMessage("Failed");
                response.setIsError(true);
                return response;
            }
            
            // Step 15: Prepare response
            Map<String, String> resMap = new HashMap<>();
            resMap.put("ClaimNo", workOrder.getClaimNo());
            resMap.put("QuotationNo", workOrder.getQuotationNo());

            response.setErrors(Collections.emptyList());
            response.setMessage("Success");
            response.setResponse(resMap);

        } catch (Exception e) {
            // Handle any unexpected exceptions
            e.printStackTrace();
            response.setErrors(Collections.singletonList("An unexpected error occurred: " + e.getMessage()));
            response.setMessage("Failed");
            response.setIsError(true);
            response.setResponse(Collections.emptyList());
        }

        return response;
	}

	@Override
	public CommonResponse getAllQuoteByClaimNo(GarageWorkOrderRequest req) {
		CommonResponse comResponse = new CommonResponse(); 
        try {
			List<GarageWorkOrder> data = garageWorkOrderRepository.findByClaimNo(req.getClaimNo());
			
			if(data.size()>0) {
		    	List<GetAllQuoteResponse> res = new ArrayList<>();
				for(GarageWorkOrder workOrder : data ) {
					 GetAllQuoteResponse response = new GetAllQuoteResponse();
			         response.setClaimNo(workOrder.getClaimNo());
			         response.setWorkOrderNo(workOrder.getWorkOrderNo());
			         response.setWorkOrderType(workOrder.getWorkOrderType());
			         response.setWorkOrderTypeDesc(workOrder.getWorkOrderTypeDesc());
			         response.setWorkOrderDate(workOrder.getWorkOrderDate());
			         response.setSettlementType(workOrder.getSettlementType());
			         response.setSettlementTypeDesc(workOrder.getSettlementTypeDesc());
			         response.setSettlementTo(workOrder.getSettlementTo());
			         response.setSettlementToDesc(workOrder.getSettlementToDesc());
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
			         response.setSparepartsDealerId(Optional.ofNullable(workOrder.getSparepartsDealerId()).map(String ::valueOf).orElse(""));
			         response.setQuoteStatus(workOrder.getQuoteStatus());			         
			         
			         VcSparePartsDetails spareParts = sparePartsDetailsRepo.findByClaimNumberAndQuotationNo(workOrder.getClaimNo(),workOrder.getQuotationNo());
			         
			         if(spareParts != null) {
			        	 response.setReplacementCost(spareParts.getReplacementCost().toString());
				         response.setReplacementCostDeductible(spareParts.getReplacementCostDeductible().toString());
				         response.setSparePartDepreciation(spareParts.getSparePartDepreciation().toString());
				         response.setDiscountOnSpareParts(spareParts.getDiscountOnSpareParts().toString());
				         response.setTotalAmountReplacement(spareParts.getTotalAmountReplacement().toString());
				         response.setRepairLabour(spareParts.getRepairLabour().toString());
				         response.setRepairLabourDeductible(spareParts.getRepairLabourDeductible().toString());
				         response.setRepairLabourDiscountAmount(spareParts.getRepairLabourDiscountAmount().toString());
				         response.setTotalAmountRepairLabour(spareParts.getTotalAmountRepairLabour().toString());
				         response.setNetAmount(spareParts.getNetAmount().toString());
				         response.setUnknownAccidentDeduction(spareParts.getUnknownAccidentDeduction() != null 
				                ? spareParts.getUnknownAccidentDeduction().toString() : "0.00");
				         response.setAmountToBeRecovered(spareParts.getAmountToBeRecovered() != null 
				                ? spareParts.getAmountToBeRecovered().toString() : "0.00");
				         response.setTotalAfterDeductions(spareParts.getTotalAfterDeductions().toString());
				         response.setVatRatePer(spareParts.getVatRatePer() != null 
				                ? spareParts.getVatRatePer().toString() : "0.00");
				         response.setVatRate(spareParts.getVatRate() != null 
				                ? spareParts.getVatRate().toString() : "0.00");
				         response.setVatAmount(spareParts.getVatAmount().toString());
				         response.setTotalWithVAT(spareParts.getTotalWithVAT().toString());
				         response.setSalvageDeduction(spareParts.getSalvageDeduction().toString()); 
			         }
			         
			         
			         res.add(response);
				}
				
				comResponse.setErrors(Collections.emptyList());
				comResponse.setMessage("Success");
				comResponse.setResponse(res);
			
			}else {
				
				comResponse.setErrors(Collections.emptyList());
				comResponse.setMessage("Failed");
				comResponse.setResponse(Collections.emptyList());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
                   
         return comResponse;
	}

	@Override
	public CommonResponse surveyorQuoteSave(GarageWorkOrderRequest req) {
		CommonResponse comResponse = new CommonResponse(); 
        try {
        	SparePartsSaveDetails spareSave = SparePartsSaveDetailsRepo.findByClaimNo(req.getClaimNo());
        	if(spareSave == null) {
        		spareSave = new SparePartsSaveDetails();
        	}
        	
			GarageWorkOrder workOrder = garageWorkOrderRepository.findByClaimNoAndQuotationNo(req.getClaimNo(),req.getQuotationNo());
			
			if(workOrder != null) {
				spareSave.setClaimNo(workOrder.getClaimNo());
				spareSave.setWorkOrderNo(workOrder.getWorkOrderNo());
				spareSave.setWorkOrderType(workOrder.getWorkOrderType());
				spareSave.setWorkOrderDate(workOrder.getWorkOrderDate());
				spareSave.setAccountSettlementType(workOrder.getSettlementType());
				spareSave.setAccountSettlementName(workOrder.getSettlementTo());
				spareSave.setGarageQuotationNo(workOrder.getQuotationNo());
				spareSave.setGarageCode(workOrder.getGarageId().toString());
				spareSave.setDeliveredTo(workOrder.getGarageName());
				spareSave.setQuotationNo(workOrder.getQuotationNo());
				spareSave.setDeliveryDate(workOrder.getDeliveryDate());
				spareSave.setJointOrder(workOrder.getJointOrderYn());
				spareSave.setSubrogation(workOrder.getSubrogationYn());
				spareSave.setTotalLoss(workOrder.getTotalLoss());
				spareSave.setTotalLossType(workOrder.getLossType());
				spareSave.setRemarks(workOrder.getRemarks());
				spareSave.setSparePartsDealer(Optional.ofNullable(workOrder.getSparepartsDealerId()).map(String ::valueOf).orElse(""));		         
			         
			         VcSparePartsDetails spareParts = sparePartsDetailsRepo.findByClaimNumberAndQuotationNo(workOrder.getClaimNo(),workOrder.getQuotationNo());
			         
			         if(spareParts != null) {
			        	 spareSave.setReplacementCost(spareParts.getReplacementCost());
			        	 spareSave.setReplacementCostDeductible(spareParts.getReplacementCostDeductible());
			        	 spareSave.setSparePartDepreciation(spareParts.getSparePartDepreciation());
			        	 spareSave.setDiscountOnSpareParts(spareParts.getDiscountOnSpareParts());
			        	 spareSave.setTotalAmountReplacement(spareParts.getTotalAmountReplacement());
			        	 spareSave.setRepairLabour(spareParts.getRepairLabour());
			        	 spareSave.setRepairLabourDeductible(spareParts.getRepairLabourDeductible());
			        	 spareSave.setRepairLabourDiscountAmount(spareParts.getRepairLabourDiscountAmount());
			        	 spareSave.setTotalAmountRepairLabour(spareParts.getTotalAmountRepairLabour());
			        	 spareSave.setNetAmount(spareParts.getNetAmount());
			        	 spareSave.setUnknownAccidentDeduction(spareParts.getUnknownAccidentDeduction());
			        	 spareSave.setAmountToBeRecovered(spareParts.getAmountToBeRecovered());
			        	 spareSave.setTotalAfterDeductions(spareParts.getTotalAfterDeductions());
			        	 spareSave.setVatRatePercentage(spareParts.getVatRatePer());
			        	 spareSave.setVatRate(spareParts.getVatRate());
			        	 spareSave.setVatAmount(spareParts.getVatAmount());
			        	 spareSave.setTotalWithVat(spareParts.getTotalWithVAT());
			  
			         }
			         
			         
			         SparePartsSaveDetailsRepo.save(spareSave);	
				
				comResponse.setErrors(Collections.emptyList());
				comResponse.setMessage("Success");
				comResponse.setResponse(Collections.emptyList());
			
			}else {
				
				comResponse.setErrors(Collections.emptyList());
				comResponse.setMessage("Failed");
				comResponse.setResponse(Collections.emptyList());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
                   
         return comResponse;
	}
}
