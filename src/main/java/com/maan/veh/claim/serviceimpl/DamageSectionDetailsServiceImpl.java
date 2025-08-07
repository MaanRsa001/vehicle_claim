package com.maan.veh.claim.serviceimpl;

import java.math.BigDecimal;
import java.text.DecimalFormat;
//import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

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
import com.maan.veh.claim.request.DamageSectionDetailsSaveReq;
import com.maan.veh.claim.request.DealerSectionDetailsSaveReq;
import com.maan.veh.claim.request.GarageSectionDetailsSaveReq;
import com.maan.veh.claim.request.VcSparePartsDetailsRequest;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.DamageSectionDetailsResponse;
import com.maan.veh.claim.response.DropDownRes;
import com.maan.veh.claim.response.ErrorList;
import com.maan.veh.claim.response.TotalAmountViewResponse;
import com.maan.veh.claim.service.DamageSectionDetailsService;

@Service
public class DamageSectionDetailsServiceImpl implements DamageSectionDetailsService {
	
	
	@Autowired
	private DamageSectionDetailsRepository repository;
	
	@Autowired
	private SparePartsSaveDetailsRepository sparePartsSaveRepo;
	
	@Autowired
	private GarageWorkOrderRepository garageWorkOrderRepo;
	
	@Autowired
    private InputValidationUtil validation;
	
	@Autowired
	private VcSparePartsDetailsRepository sparePartsDetailsRepo;
	
	@Autowired
	private DropDownServiceImpl dropDownServiceImpl;
	
	@Autowired
	private GarageWorkOrderServiceImpl garageWorkOrderServiceImpl;
	
	@Autowired
	private InsuredVehicleInfoRepository insuredVehicleInfoRepo;

	private DamageSectionDetailsResponse mapToResponse(DamageSectionDetails details) {
		DamageSectionDetailsResponse response = new DamageSectionDetailsResponse();
		
		try {
			response.setClaimNo(Optional.ofNullable(details.getClaimNo()).orElse(""));
			response.setQuotationNo(Optional.ofNullable(details.getQuotationNo()).orElse(""));
			response.setDamageSno(String.valueOf(details.getDamageSno()));
			response.setDamageDictDesc(Optional.ofNullable(details.getDamageDirection()).orElse(""));
			response.setDamagePart(Optional.ofNullable(details.getDamagePart()).orElse(""));
			response.setRepairReplace(Optional.ofNullable(details.getRepairReplace()).orElse(""));
			response.setNoOfParts(Optional.ofNullable(details.getNoOfParts()).map(String::valueOf).orElse(""));
			response.setGaragePrice(Optional.ofNullable(details.getGaragePrice()).map(String::valueOf).orElse(""));
			response.setDealerPrice(Optional.ofNullable(details.getDealerPrice()).map(String::valueOf).orElse(""));
			response.setGarageLoginId(Optional.ofNullable(details.getGarageLoginId()).orElse(""));
			response.setDealerLoginId(Optional.ofNullable(details.getDealerLoginId()).orElse(""));
			response.setSurveyorId(Optional.ofNullable(details.getSurveyorId()).map(String::valueOf).orElse(""));
			response.setReplaceCost(Optional.ofNullable(details.getReplaceCost()).map(String::valueOf).orElse(""));
			response.setReplaceCostDeduct(Optional.ofNullable(details.getReplaceCostDeduct()).map(String::valueOf).orElse(""));
			response.setSparepartDeprection(Optional.ofNullable(details.getSparepartDeprection()).map(String::valueOf).orElse(""));
			response.setDiscountSparepart(Optional.ofNullable(details.getDiscountSparepart()).map(String::valueOf).orElse(""));
			response.setTotamtReplace(Optional.ofNullable(details.getTotamtReplace()).map(String::valueOf).orElse(""));
			response.setLabourCost(Optional.ofNullable(details.getLabourCost()).map(String::valueOf).orElse(""));
			response.setLabourCostDeduct(Optional.ofNullable(details.getLabourCostDeduct()).map(String::valueOf).orElse(""));
			response.setLabourDisc(Optional.ofNullable(details.getLabourDisc()).map(String::valueOf).orElse(""));
			response.setTotamtOfLabour(Optional.ofNullable(details.getTotamtOfLabour()).map(String::valueOf).orElse(""));
			response.setTotPrice(Optional.ofNullable(details.getTotPrice()).map(String::valueOf).orElse(""));
			response.setEntryDate(details.getEntryDate()); 
			response.setStatus(Optional.ofNullable(details.getStatus()).orElse(""));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	@Override
	public CommonResponse saveDamageSectionDetails(List<DamageSectionDetailsSaveReq> reqList) {
	    CommonResponse response = new CommonResponse();
	    try {
	        List<ErrorList> errors = validation.validateDamageDetails(reqList);
	        if (errors.isEmpty()) {
	            
	            List<DamageSectionDetails> saveList = new ArrayList<>();
	            int damageSno = 1;
	            
	            List<String> claimList = new ArrayList<>(new HashSet<>(reqList.stream().map(DamageSectionDetailsSaveReq::getClaimNo).collect(Collectors.toList())));
	            String surveyorId = reqList.get(0).getSurveyorId();

	            
	            //String claimNo = reqList.get(0).getClaimNo();
	            for (DamageSectionDetailsSaveReq req : reqList) {
	                
	                DamageSectionDetails details = repository.findByClaimNoAndQuotationNoAndDamageSno(
	                    req.getClaimNo(), req.getQuotationNo(), Optional.ofNullable(req.getDamageSno()).map(Integer::valueOf).orElse(damageSno));

	                details.setClaimNo(req.getClaimNo());
	                details.setQuotationNo(req.getQuotationNo());
	                details.setDamageSno(Optional.ofNullable(req.getDamageSno()).map(Integer::valueOf).orElse(damageSno));
	                details.setSurveyorId(req.getSurveyorId());
	                details.setGarageDealer(req.getGarageDealer());

	                BigDecimal totalReplaceCost;
	                BigDecimal amount;
	                
	                if ("Garage".equalsIgnoreCase(req.getGarageDealer())) {
	                	amount = details.getGaragePrice();
	                } else {
	                	amount = details.getDealerPrice();
	                }
	                
	                
	                if ("Replace".equalsIgnoreCase(details.getRepairReplace())) {
	                    totalReplaceCost = new BigDecimal(details.getNoOfParts()).multiply(details.getGaragePrice())
		                        .add(details.getReplaceCost());
	                    
	                    BigDecimal replaceCostDeduct = totalReplaceCost
	                        .multiply(new BigDecimal(req.getReplaceCostDeductPercentage())).divide(new BigDecimal(100));
	                    BigDecimal sparePartDeprecation = totalReplaceCost
	                        .multiply(new BigDecimal(req.getSparepartDeprectionPercentage())).divide(new BigDecimal(100));
	                    BigDecimal discountOnSparePart = totalReplaceCost
	                        .multiply(new BigDecimal(req.getDiscountSparepartPercentage())).divide(new BigDecimal(100));
	                    
	                    details.setReplaceCostDeduct(replaceCostDeduct);
	                    details.setSparepartDeprection(sparePartDeprecation);
	                    details.setDiscountSparepart(discountOnSparePart);

	                    details.setReplaceCostDeductPercentage(new BigDecimal(req.getReplaceCostDeductPercentage()));
	                    details.setSparepartDeprectionPercentage(new BigDecimal(req.getSparepartDeprectionPercentage()));
	                    details.setDiscountSparepartPercentage(new BigDecimal(req.getDiscountSparepartPercentage()));
	                    
	                    details.setTotamtReplace(totalReplaceCost);
		                details.setTotPrice(totalReplaceCost);
	                    
	                } else {
	                    BigDecimal labourCostDeduct = details.getLabourCost()
	                        .multiply(new BigDecimal(req.getLabourCostDeductPercentage())).divide(new BigDecimal(100));
	                    BigDecimal labourDiscount = details.getLabourCost()
	                        .multiply(new BigDecimal(req.getLabourDiscPercentage())).divide(new BigDecimal(100));

	                    details.setLabourCostDeduct(labourCostDeduct);
	                    details.setLabourDisc(labourDiscount);

	                    details.setLabourCostDeductPercentage(new BigDecimal(req.getLabourCostDeductPercentage()));
	                    details.setLabourDiscPercentage(new BigDecimal(req.getLabourDiscPercentage()));
	                    details.setTotamtOfLabour(details.getLabourCost());
	                    details.setTotPrice(details.getLabourCost());
	                }

	                details.setStatus("Surveyor");
	                details.setEntryDate(new Date());

	                saveList.add(details);
	                damageSno++;
	            }
	            repository.saveAllAndFlush(saveList);
	            
	            asignRepairWorkToGarage(claimList,surveyorId);
	            
	            response.setErrors(Collections.emptyList());
	            response.setMessage("Success");
	            response.setResponse(Collections.emptyList());
	        } else {
	            response.setErrors(errors);
	            response.setMessage("Failed");
	            response.setResponse(Collections.emptyList());
	            response.setIsError(true);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.setErrors(Collections.emptyList());
	        response.setMessage("Failed");
	        String exceptionDetails = e.getClass().getSimpleName() + ": " + e.getMessage();
	        response.setResponse(exceptionDetails);
	    }
	    return response;
	}

	private void asignRepairWorkToGarage(List<String> claimList, String surveyorId) {

		try {
			List<DamageSectionDetails> saveList = new ArrayList<>();

			for (String claim : claimList) {

				List<DamageSectionDetails> details = repository.findByClaimNoAndRepairReplace(claim, "REPAIR");

				for (DamageSectionDetails data : details) {

					data.setSurveyorId(surveyorId);
					data.setGarageDealer("Garage");

					data.setTotamtOfLabour(data.getLabourCost());
					data.setTotPrice(data.getLabourCost());

					data.setStatus("Surveyor");
					data.setEntryDate(new Date());

					saveList.add(data);
				}

			}
			repository.saveAllAndFlush(saveList);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public CommonResponse saveGarageDamageSectionDetails(List<GarageSectionDetailsSaveReq> reqList) {
		CommonResponse response = new CommonResponse();
	    try {
	        List<ErrorList> errors = validation.validateGarageDamageDetails(reqList);
	        if (errors.isEmpty()) {
	        	
	        	List<DamageSectionDetails> saveList = new ArrayList<DamageSectionDetails>();
	        	int damageSno = 1;
	        	String claimNo = "";
	        	String garageId = "";
	        	String quotationNo = "";
	            for (GarageSectionDetailsSaveReq req:reqList) {
	            	claimNo = req.getClaimNo();
	            	garageId = req.getGarageLoginId();
	            	quotationNo = req.getQuotationNo();
	            	DamageSectionDetails details = repository.findByClaimNoAndQuotationNoAndDamageSno(req.getClaimNo(),req.getQuotationNo(),Optional.ofNullable(req.getDamageSno()).map(Integer::valueOf).orElse(damageSno));
	            	if(details == null ) {
	            		details = new DamageSectionDetails();
	            	}
	            	
					details.setClaimNo(req.getClaimNo());

					details.setQuotationNo(req.getQuotationNo());

					details.setDamageSno(Optional.ofNullable(req.getDamageSno()).map(Integer ::valueOf).orElse(damageSno));

					details.setDamageDirection(req.getDamageDirection());
					details.setDamagePart(req.getDamagePart());
					details.setRepairReplace(req.getRepairReplace());
					details.setRemarks(req.getRemarks());
					
					if ("Replace".equalsIgnoreCase(req.getRepairReplace())) {
						details.setNoOfParts(Integer.valueOf(req.getNoOfUnits()));
						details.setReplaceCost(new BigDecimal(req.getReplacementCharge()));
						details.setLabourCost(new BigDecimal(req.getReplacementCharge()));
						details.setGaragePrice(new BigDecimal(req.getUnitPrice()));
					}else {
						details.setNoOfParts(StringUtils.isNotBlank(req.getNoOfUnits())?Integer.valueOf(req.getNoOfUnits()):0);
						details.setReplaceCost(new BigDecimal(req.getReplacementCharge()));
						details.setGaragePrice(new BigDecimal(req.getReplacementCharge()));
						details.setLabourCostDeductPercentage(StringUtils.isNotBlank(req.getDeductablePer())? new BigDecimal(req.getDeductablePer()):BigDecimal.ZERO);
						details.setLabourCostDeduct(StringUtils.isNotBlank(req.getDeductableAmount())? new BigDecimal(req.getDeductableAmount()):BigDecimal.ZERO);
						details.setAsPerInvoice(StringUtils.isNotBlank(req.getAsPerInvoice())? req.getAsPerInvoice():"false");
						details.setGarageDealer("Garage");
					}
					
					
					details.setGarageLoginId(req.getGarageLoginId());
					
					details.setStatus("Garage");
					details.setEntryDate(new Date());
					
					saveList.add(details);
					damageSno++;
				}
				repository.saveAllAndFlush(saveList);

	            response.setErrors(Collections.emptyList());
	            response.setMessage("Success");
	            response.setResponse(Collections.emptyList());
	            try {
					Optional<InsuredVehicleInfo> optional = insuredVehicleInfoRepo.findByClaimNoAndGarageId(claimNo, garageId);
					GarageWorkOrder garageWorkOrder = garageWorkOrderRepo.findByClaimNoAndQuotationNo(claimNo, quotationNo);
					garageWorkOrderServiceImpl.directGarageSave(optional.get(), garageWorkOrder);
				} catch (Exception e) {
					
					System.out.println("Error in saving spare parts table "+e.getMessage());
				}
	        } else {
	            response.setErrors(errors);
	            response.setMessage("Failed");
	            response.setResponse(Collections.emptyList());
	            response.setIsError(true);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.setErrors(Collections.emptyList());
	        response.setMessage("Failed");
	        String exceptionDetails = e.getClass().getSimpleName() + ": " + e.getMessage();
	        response.setResponse(exceptionDetails);
	    }
	    return response;
	}

	@Override
	public CommonResponse saveDealerDamageSectionDetails(List<DealerSectionDetailsSaveReq> reqList) {
		CommonResponse response = new CommonResponse();
		
		
	    try {
	        List<ErrorList> errors = validation.validateDealerDamageDetails(reqList);
	        if (errors.isEmpty()) {
	        	
	        	List<DamageSectionDetails> saveList = new ArrayList<DamageSectionDetails>();
	        	int damageSno = 1;

	        	String claimNo = reqList.stream()
	        	        .filter(req -> StringUtils.isNotBlank(req.getClaimNo())) 
	        	        .findFirst()
	        	        .map(DealerSectionDetailsSaveReq::getClaimNo) 
	        	        .orElse(""); 

	        	String quotationNo = reqList.stream()
	        	        .filter(req -> StringUtils.isNotBlank(req.getQuotationNo())) 
	        	        .findFirst()
	        	        .map(DealerSectionDetailsSaveReq::getQuotationNo)
	        	        .orElse(""); 
	        	
	        	String dealerId = reqList.stream()
	        	        .filter(req -> StringUtils.isNotBlank(req.getDealerLoginId())) 
	        	        .findFirst()
	        	        .map(DealerSectionDetailsSaveReq::getDealerLoginId)
	        	        .orElse(""); 
	        	
	            for (DealerSectionDetailsSaveReq req:reqList) {
	            	
	            	
	            	DamageSectionDetails details = repository.findByClaimNoAndQuotationNoAndDamageSno(req.getClaimNo(),req.getQuotationNo(),Optional.ofNullable(req.getDamageSno()).map(Integer::valueOf).orElse(damageSno));
	            	
					details.setClaimNo(req.getClaimNo());

					details.setQuotationNo(req.getQuotationNo());

					details.setDamageSno(Optional.ofNullable(req.getDamageSno()).map(Integer ::valueOf).orElse(damageSno));
					
					details.setDealerPrice(new BigDecimal(req.getUnitPrice()));
					details.setDealerLoginId(req.getDealerLoginId());
					details.setStatus("Dealer");
					details.setEntryDate(new Date());
					
					saveList.add(details);
					damageSno++;
				}
				repository.saveAllAndFlush(saveList);
				
				GarageWorkOrder workOrder = garageWorkOrderRepo.findByClaimNoAndQuotationNo(claimNo,quotationNo);
				workOrder.setSparepartsDealerId(dealerId);
				workOrder.setStatus("Dealer");
				garageWorkOrderRepo.save(workOrder);
				
	            response.setErrors(Collections.emptyList());
	            response.setMessage("Success");
	            response.setResponse(Collections.emptyList());
	        } else {
	            response.setErrors(errors);
	            response.setMessage("Failed");
	            response.setResponse(Collections.emptyList());
	            response.setIsError(true);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.setErrors(Collections.emptyList());
	        response.setMessage("Failed");
	        String exceptionDetails = e.getClass().getSimpleName() + ": " + e.getMessage();
	        response.setResponse(exceptionDetails);
	    }
	    return response;
	}

	@Override
	public CommonResponse viewGarageDamageSectionDetails(GarageSectionDetailsSaveReq req) {
		CommonResponse response = new CommonResponse();
	    try {
	    	DecimalFormat df = new DecimalFormat("0.00");
	    	
	        List<GarageSectionDetailsSaveReq> groupedDamageDetails = new ArrayList<>();
	        
	        
	        // Fetch damage section details based on ClaimNo and QuotationNo
	        List<DamageSectionDetails> details = repository.findByClaimNoAndQuotationNo(req.getClaimNo(), req.getQuotationNo());
	        
	        for (DamageSectionDetails data : details) {
	            GarageSectionDetailsSaveReq res = new GarageSectionDetailsSaveReq();
	            
	            // Populate the fields from the retrieved data
	            res.setClaimNo(data.getClaimNo());
	            res.setQuotationNo(data.getQuotationNo());
	            res.setDamageSno(data.getDamageSno() != null ? data.getDamageSno().toString() : "");
	            res.setDamageDirection(data.getDamageDirection());
	            res.setDamagePart(data.getDamagePart());
	            res.setRepairReplace(data.getRepairReplace());    
	            res.setNoOfUnits(data.getNoOfParts() != null ? data.getNoOfParts().toString() : "");
	            res.setReplacementCharge(data.getReplaceCost() != null ? df.format(data.getReplaceCost()) : "");
	            if("REPLACE".equalsIgnoreCase(data.getRepairReplace())) {
	            	if("Garage".equalsIgnoreCase(data.getGarageDealer())) {
	            		res.setUnitPrice(data.getGaragePrice() != null ? df.format(data.getGaragePrice()) : "");
	            	}else if("Dealer".equalsIgnoreCase(data.getGarageDealer())) {
	            		res.setUnitPrice(data.getDealerPrice() != null ? df.format(data.getDealerPrice()) : "");
	            	}else {
	            		res.setUnitPrice(data.getGaragePrice() != null ? df.format(data.getGaragePrice()) : "");
	            	}          	
	            }
	            res.setGaragePrice(data.getGaragePrice() != null ? df.format(data.getGaragePrice()) : "");
	            res.setDealerPrice(data.getDealerPrice() != null ? df.format(data.getDealerPrice()) : "");
	            res.setGarageLoginId(data.getGarageLoginId());
	            res.setStatus(data.getStatus());
	            res.setDeductablePer(data.getLabourCostDeductPercentage() != null ? df.format(data.getLabourCostDeductPercentage()) : "0.00");
	            res.setDeductableAmount(data.getLabourCostDeduct() != null ? df.format(data.getLabourCostDeduct()) : "0.00");
	            res.setAsPerInvoice(data.getAsPerInvoice());
	            res.setEntryDate(data.getEntryDate());
	            res.setRemarks(data.getRemarks());
	            groupedDamageDetails.add(res); 
	        }
	        
	        
	        // Set the response
	        response.setErrors(Collections.emptyList());
	        response.setMessage("Success");
	        response.setResponse(groupedDamageDetails);  
	        
	    } catch (Exception e) {
	        // Handle exceptions
	    	String exceptionDetails = e.getClass().getSimpleName() + ": " + e.getMessage();
	        response.setResponse(exceptionDetails);
	        response.setMessage("Failed");
	        response.setResponse(null);
	    }
	    return response;
	}

	@Override
	public CommonResponse viewGarageTotalDamageSectionDetails(GarageSectionDetailsSaveReq req) {
	    CommonResponse response = new CommonResponse();
	    try {
	        // Initialize totals as BigDecimal
	        BigDecimal totalGaragePrice = BigDecimal.ZERO;
	        BigDecimal totalReplacementCharge = BigDecimal.ZERO;
	        BigDecimal totalAmount = BigDecimal.ZERO;

	        // Fetch damage section details based on ClaimNo and QuotationNo
	        List<DamageSectionDetails> details = repository.findByClaimNoAndQuotationNo(req.getClaimNo(), req.getQuotationNo());
	        
	        for (DamageSectionDetails data : details) {
	            // Retrieve garage price and number of units as BigDecimal
	            BigDecimal garagePrice = data.getGaragePrice() != null ? data.getGaragePrice() : BigDecimal.ZERO;
	            BigDecimal numberOfUnits = data.getNoOfParts() != null ? new BigDecimal(data.getNoOfParts()) : BigDecimal.ZERO; 
	            BigDecimal replacementCharge = data.getReplaceCost() != null ? data.getReplaceCost() : BigDecimal.ZERO;

	            // Calculate total garage price for the current row
	            BigDecimal garagePriceForRow = garagePrice.multiply(numberOfUnits);
	            totalGaragePrice = totalGaragePrice.add(garagePriceForRow);
	            
	            // Update total replacement charge
	            totalReplacementCharge = totalReplacementCharge.add(replacementCharge);
	        }

	        // Calculate total amount
	        totalAmount = totalGaragePrice.add(totalReplacementCharge);

	        // Prepare the response map with totals
	        Map<String, BigDecimal> responseMap = new HashMap<>();
	        responseMap.put("totalGaragePrice", totalGaragePrice); // Total garage price
	        responseMap.put("totalReplacementCharge", totalReplacementCharge); // Total replacement charge
	        responseMap.put("totalAmount", totalAmount); // Total amount

	        // Set the response
	        response.setErrors(Collections.emptyList());
	        response.setMessage("Success");
	        response.setResponse(responseMap);  // Response contains totals
	        
	    } catch (Exception e) {
	        // Handle exceptions
	        String exceptionDetails = e.getClass().getSimpleName() + ": " + e.getMessage();
	        response.setErrors(Collections.singletonList(exceptionDetails)); // Set the error message
	        response.setMessage("Failed");
	        response.setResponse(null);
	    }
	    return response;
	}

	@Override
	public CommonResponse viewDealerDamageSectionDetails(GarageSectionDetailsSaveReq req) {
		CommonResponse response = new CommonResponse();
	    try {
	        
	        List<GarageSectionDetailsSaveReq> groupedDamageDetails = new ArrayList<>();
	        
	        // Fetch damage section details based on ClaimNo and QuotationNo
	        List<DamageSectionDetails> details = repository.findByClaimNoAndQuotationNo(req.getClaimNo(), req.getQuotationNo());
	        
	        for (DamageSectionDetails data : details) {
	            GarageSectionDetailsSaveReq res = new GarageSectionDetailsSaveReq();
	            
	            // Populate the fields from the retrieved data
	            res.setClaimNo(data.getClaimNo());
	            res.setQuotationNo(data.getQuotationNo());
	            res.setDamageSno(data.getDamageSno() != null ? data.getDamageSno().toString() : "");
	            res.setDamageDirection(data.getDamageDirection());
	            res.setDamagePart(data.getDamagePart());
	            res.setRepairReplace(data.getRepairReplace());    
	            res.setNoOfUnits(data.getNoOfParts() != null ? data.getNoOfParts().toString() : "");
	            //res.setReplacementCharge(data.getReplaceCost() != null ? data.getReplaceCost().toString() : "");
	            res.setUnitPrice(data.getDealerPrice() != null ? data.getDealerPrice().toString() : "");
	            res.setDealerLoginId(data.getDealerLoginId());
	            res.setStatus(data.getStatus());
	            res.setGarageLoginId(data.getGarageLoginId());
	            groupedDamageDetails.add(res); 
	        }
	        
	        groupedDamageDetails = groupedDamageDetails.stream()
	        	    .filter(res -> "Replace".equalsIgnoreCase(res.getRepairReplace())) // Filter condition
	        	    .collect(Collectors.toList()); 
	        
	        // Set the response
	        response.setErrors(Collections.emptyList());
	        response.setMessage("Success");
	        response.setResponse(groupedDamageDetails);  
	        
	    } catch (Exception e) {
	        // Handle exceptions
	    	String exceptionDetails = e.getClass().getSimpleName() + ": " + e.getMessage();
	        response.setResponse(exceptionDetails);
	        response.setMessage("Failed");
	        response.setResponse(null);
	    }
	    return response;
	}

	@Override
	public CommonResponse deleteGarageDamageSectionDetails(List<GarageSectionDetailsSaveReq> reqList) {
		CommonResponse response = new CommonResponse();
	    try {
	        List<ErrorList> errors = validation.validateDeleteGarageDamageDetails(reqList);
	        if (errors.isEmpty()) {
	        	
	        	List<DamageSectionDetails> deleteList = new ArrayList<DamageSectionDetails>();
	        	
	            for (GarageSectionDetailsSaveReq req:reqList) {
	            	
	            	DamageSectionDetails details = repository.findByClaimNoAndQuotationNoAndDamageSno(req.getClaimNo(),req.getQuotationNo(),Optional.ofNullable(req.getDamageSno()).map(Integer::valueOf).orElse(1));
	            	if(details != null ) {
	            		deleteList.add(details);
	            	}else {
	            		
	            	}
				}
				repository.deleteAll(deleteList);

	            response.setErrors(Collections.emptyList());
	            response.setMessage("Success");
	            response.setResponse(Collections.emptyList());
	        } else {
	            response.setErrors(errors);
	            response.setMessage("Failed");
	            response.setResponse(Collections.emptyList());
	            response.setIsError(true);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.setErrors(Collections.emptyList());
	        response.setMessage("Failed");
	        String exceptionDetails = e.getClass().getSimpleName() + ": " + e.getMessage();
	        response.setResponse(exceptionDetails);
	    }
	    return response;
	}

	@Override
	public CommonResponse viewSurveyorDamageSectionDetails(GarageSectionDetailsSaveReq req) {
		CommonResponse response = new CommonResponse();
	    try {
	        
			List<DamageSectionDetails> detailsList = repository.findByClaimNoAndQuotationNo(req.getClaimNo(),req.getQuotationNo());

			List<DamageSectionDetailsResponse> list = detailsList.stream().map(this::mapToResponse).collect(Collectors.toList());

	        // Set the response
	        response.setErrors(Collections.emptyList());
	        response.setMessage("Success");
	        response.setResponse(list);  
	        
	    } catch (Exception e) {
	        // Handle exceptions
	    	String exceptionDetails = e.getClass().getSimpleName() + ": " + e.getMessage();
	        response.setResponse(exceptionDetails);
	        response.setMessage("Failed");
	        response.setResponse(null);
	    }
	    return response;
	}

	@Override
	public CommonResponse saveSpareParts(VcSparePartsDetailsRequest req) {
	    CommonResponse response = new CommonResponse(); 
	    
	    
	    try {
	        List<ErrorList> errors = validation.validateSaveSpareParts(req);
	              
	        if (errors.isEmpty()) {
	        	
	            VcSparePartsDetails spareParts = sparePartsDetailsRepo.findByClaimNumberAndQuotationNoAndDamageSnoAndGarageId(
	                req.getClaimNo(), req.getQuotationNo(), req.getDamageSno(), req.getGarageId());
	            
	            if (spareParts == null) {
	                spareParts = new VcSparePartsDetails();
	            }
	            
	            // common fields getting value directly from request
	            spareParts.setClaimNumber(req.getClaimNo());
	            spareParts.setQuotationNo(req.getQuotationNo());
	            spareParts.setGarageId(req.getGarageId());
	            spareParts.setSparePartType(req.getSparePartType());
	            spareParts.setSparePartTypeDesc(req.getSparePartTypeDesc());
	            spareParts.setOriginalDiscount(toBigDecimal(req.getOriginalDiscount()));
	            spareParts.setDiscountPercentage(toBigDecimal(req.getDiscountPercentage()));
	            spareParts.setReplacementCostDeductible(toBigDecimal(req.getReplacementCostDeductible()));
	            spareParts.setDamageType(req.getDamageType());
	            spareParts.setDepreciationType(req.getDepreciationType());
	            spareParts.setDepreciationTypeDesc(req.getDepreciationTypeDesc());
	            spareParts.setDepreciation(toBigDecimal(req.getDepreciation()));
	            spareParts.setReferralStatus(req.getReferralStatus());
	            spareParts.setRepairLabour(toBigDecimal(req.getRepairLabour()));
	            spareParts.setRepairLabourDiscount(toBigDecimal(req.getRepairLabourDiscount()));
	            spareParts.setRepairLabourDeductible(toBigDecimal(req.getRepairLabourDeductible()));
	            spareParts.setRemarks(req.getRemarks());
	            spareParts.setDamageDirection(req.getDamageDirection());
	            spareParts.setDamageDirectionDesc(req.getDamageDirectionDesc());
	            spareParts.setPartType(req.getPartType());
	            spareParts.setPartTypeDesc(req.getPartTypeDesc());
	            spareParts.setReplaceRepair(req.getReplaceRepair());
	            spareParts.setNoOfUnits(toInteger(req.getNoOfUnits()));
	            spareParts.setSparePartsCost(toBigDecimal(req.getSparePartsCost()));
	            spareParts.setLabourCharge(toBigDecimal(req.getLabourCharge()));
	            spareParts.setDamageSno(req.getDamageSno());
	            
	            if(req.getReplaceRepair().equalsIgnoreCase("REPLACE")) {
		        	BigDecimal ReplacementCostDeductible = toBigDecimal(req.getReplacementCostDeductible());
		    	    BigDecimal Depreciation = toBigDecimal(req.getDepreciation());
		    	    BigDecimal totalCost = toBigDecimal(req.getNoOfUnits()).multiply(toBigDecimal(req.getSparePartsCost()));	
		    	    BigDecimal DiscountPercentage= toBigDecimal(req.getDiscountPercentage());
		    	    BigDecimal DiscountAmount=(DiscountPercentage.divide(new BigDecimal(100))).multiply(totalCost);
		    	    BigDecimal sparePartsDamageAmount=totalCost.subtract(DiscountAmount).subtract(ReplacementCostDeductible).subtract(Depreciation);
		            
		    	    // Calculated fields for replace
		            spareParts.setDiscountAmount(DiscountAmount);
		            spareParts.setTotalCost(sparePartsDamageAmount);
		        	}
	            
	            BigDecimal repairLabourDeductible = toBigDecimal(req.getRepairLabourDeductible());
	    	    BigDecimal TotalAmountRepairLabour = toBigDecimal(req.getLabourCharge());
	    	    BigDecimal RepairLabourDiscount = toBigDecimal(req.getRepairLabourDiscount());
	    	    BigDecimal repairLabourDiscountAmount=(RepairLabourDiscount.divide(new BigDecimal(100))).multiply(TotalAmountRepairLabour);
	    	    BigDecimal repairLabourTotalAmount=TotalAmountRepairLabour.subtract(repairLabourDiscountAmount).subtract(repairLabourDeductible);
	            
	    	    // common calculated fields for both repair and replace
	            spareParts.setRepairLabourDiscountAmount(repairLabourDiscountAmount);
	            spareParts.setTotalAmountRepairLabour(repairLabourTotalAmount);
	            
	            // Save the updated spare parts details
	            sparePartsDetailsRepo.save(spareParts);
	            
	            response.setErrors(Collections.emptyList()); 
	            response.setMessage("Success");
	            response.setResponse(Collections.emptyList());
	            response.setIsError(false);
	        } else {
	            response.setErrors(errors);
	            response.setMessage("Failed");
	            response.setResponse(Collections.emptyList());
	            response.setIsError(true);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.setErrors(Collections.emptyList());
	        response.setMessage("Failed");
	        String exceptionDetails = e.getClass().getSimpleName() + ": " + e.getMessage();
	        response.setResponse(exceptionDetails);
	        response.setIsError(true);
	    }
	    return response;
	}
	
	public CommonResponse saveSparePartsOld(VcSparePartsDetailsRequest req) {
	    CommonResponse response = new CommonResponse();
	    try {
	        List<ErrorList> errors = validation.validateSaveSpareParts(req);
	        
	        if (errors.isEmpty()) {
	            VcSparePartsDetails spareParts = sparePartsDetailsRepo.findByClaimNumberAndQuotationNoAndDamageSnoAndGarageId(
	                req.getClaimNo(), req.getQuotationNo(), req.getDamageSno(), req.getGarageId());
	            
	            if (spareParts == null) {
	                spareParts = new VcSparePartsDetails();
	            }
	            
	            // Mapping all fields from request to entity, with type conversions where necessary
	            spareParts.setClaimNumber(req.getClaimNo());
	            spareParts.setQuotationNo(req.getQuotationNo());
	            spareParts.setGarageId(req.getGarageId());
	            spareParts.setSparePartType(req.getSparePartType());
	            spareParts.setSparePartTypeDesc(req.getSparePartTypeDesc());
	            spareParts.setOriginalDiscount(toBigDecimal(req.getOriginalDiscount()));
	            spareParts.setDiscountPercentage(toBigDecimal(req.getDiscountPercentage()));
	            spareParts.setDiscountAmount(toBigDecimal(req.getDiscountAmount()));
	            spareParts.setReplacementCostDeductible(toBigDecimal(req.getReplacementCostDeductible()));
	            spareParts.setDamageType(req.getDamageType());
	            spareParts.setDepreciationType(req.getDepreciationType());
	            spareParts.setDepreciationTypeDesc(req.getDepreciationTypeDesc());
	            spareParts.setDepreciation(toBigDecimal(req.getDepreciation()));
	            spareParts.setReferralStatus(req.getReferralStatus());
	            spareParts.setRepairLabour(toBigDecimal(req.getRepairLabour()));
	            spareParts.setRepairLabourDiscount(toBigDecimal(req.getRepairLabourDiscount()));
	            spareParts.setRepairLabourDiscountAmount(toBigDecimal(req.getRepairLabourDiscountAmount()));
	            spareParts.setRepairLabourDeductible(toBigDecimal(req.getRepairLabourDeductible()));
	            spareParts.setTotalAmountRepairLabour(toBigDecimal(req.getTotalAmountRepairLabour()));
	            spareParts.setRemarks(req.getRemarks());
	            spareParts.setDamageDirection(req.getDamageDirection());
	            spareParts.setDamageDirectionDesc(req.getDamageDirectionDesc());
	            spareParts.setPartType(req.getPartType());
	            spareParts.setPartTypeDesc(req.getPartTypeDesc());
	            spareParts.setReplaceRepair(req.getReplaceRepair());
	            spareParts.setNoOfUnits(toInteger(req.getNoOfUnits()));
	            spareParts.setSparePartsCost(toBigDecimal(req.getSparePartsCost()));
	            spareParts.setLabourCharge(toBigDecimal(req.getLabourCharge()));
	            spareParts.setTotalCost(toBigDecimal(req.getTotalCost()));
	            spareParts.setDamageSno(req.getDamageSno());

	            // Save the updated spare parts details
	            sparePartsDetailsRepo.save(spareParts);
	            
	            response.setErrors(Collections.emptyList());
	            response.setMessage("Success");
	            response.setResponse(Collections.emptyList());
	            response.setIsError(false);
	        } else {
	            response.setErrors(errors);
	            response.setMessage("Failed");
	            response.setResponse(Collections.emptyList());
	            response.setIsError(true);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.setErrors(Collections.emptyList());
	        response.setMessage("Failed");
	        String exceptionDetails = e.getClass().getSimpleName() + ": " + e.getMessage();
	        response.setResponse(exceptionDetails);
	        response.setIsError(true);
	    }
	    return response;
	}

	/**
	 * Converts a string to BigDecimal safely, returning null if invalid.
	 */
	private BigDecimal toBigDecimal(String value) {
	    if (StringUtils.isNotBlank(value)) {
	        try {
	            return new BigDecimal(value);
	        } catch (NumberFormatException e) {
	            return null;
	        }
	    }
	    return null;
	}

	/**
	 * Converts a string to Integer safely, returning null if invalid.
	 */
	private Integer toInteger(String value) {
	    if (StringUtils.isNotBlank(value)) {
	        try {
	            return Integer.parseInt(value);
	        } catch (NumberFormatException e) {
	            return null;
	        }
	    }
	    return null;
	}


	@Override
	public CommonResponse viewsaveSpareParts(GarageSectionDetailsSaveReq req) {
	    CommonResponse response = new CommonResponse();
	    try {
	        String claimNo = req.getClaimNo();
	        String quotationNo = req.getQuotationNo();
	        String garageId = req.getGarageLoginId();

	        List<VcSparePartsDetails> sparePartsList = 
	            sparePartsDetailsRepo.findByClaimNumberAndQuotationNoAndGarageId(claimNo, quotationNo, garageId);

	        if (sparePartsList != null && !sparePartsList.isEmpty()) {
	            List<VcSparePartsDetailsRequest> sparePartsResponses = sparePartsList.stream().map(spareParts -> {
	                VcSparePartsDetailsRequest sparePartsResponse = new VcSparePartsDetailsRequest();
	                
	                sparePartsResponse.setClaimNo(spareParts.getClaimNumber());
	                sparePartsResponse.setQuotationNo(spareParts.getQuotationNo());
	                sparePartsResponse.setGarageId(spareParts.getGarageId());
	                sparePartsResponse.setSparePartType(spareParts.getSparePartType());
	                sparePartsResponse.setSparePartTypeDesc(spareParts.getSparePartTypeDesc());
	                sparePartsResponse.setOriginalDiscount(convertBigDecimalToString(spareParts.getOriginalDiscount()));
	                sparePartsResponse.setDiscountPercentage(convertBigDecimalToString(spareParts.getDiscountPercentage()));
	                sparePartsResponse.setDiscountAmount(convertBigDecimalToString(spareParts.getDiscountAmount()));
	                sparePartsResponse.setReplacementCostDeductible(convertBigDecimalToString(spareParts.getReplacementCostDeductible()));
	                sparePartsResponse.setDamageType(spareParts.getDamageType());
	                sparePartsResponse.setDepreciationType(spareParts.getDepreciationType());
	                sparePartsResponse.setDepreciationTypeDesc(spareParts.getDepreciationTypeDesc());
	                sparePartsResponse.setDepreciation(convertBigDecimalToString(spareParts.getDepreciation()));
	                sparePartsResponse.setReferralStatus(spareParts.getReferralStatus());
	                sparePartsResponse.setRepairLabour(convertBigDecimalToString(spareParts.getRepairLabour()));
	                sparePartsResponse.setRepairLabourDiscount(convertBigDecimalToString(spareParts.getRepairLabourDiscount()));
	                sparePartsResponse.setRepairLabourDiscountAmount(convertBigDecimalToString(spareParts.getRepairLabourDiscountAmount()));
	                sparePartsResponse.setRepairLabourDeductible(convertBigDecimalToString(spareParts.getRepairLabourDeductible()));
	                sparePartsResponse.setTotalAmountRepairLabour(convertBigDecimalToString(spareParts.getTotalAmountRepairLabour()));
	                sparePartsResponse.setRemarks(spareParts.getRemarks());
	                sparePartsResponse.setDamageDirection(spareParts.getDamageDirection());
	                sparePartsResponse.setDamageDirectionDesc(spareParts.getDamageDirectionDesc());
	                sparePartsResponse.setPartType(spareParts.getPartType());
	                sparePartsResponse.setPartTypeDesc(spareParts.getPartTypeDesc());
	                sparePartsResponse.setReplaceRepair(spareParts.getReplaceRepair());
	                sparePartsResponse.setNoOfUnits(spareParts.getNoOfUnits() != null ? spareParts.getNoOfUnits().toString() : null);
	                sparePartsResponse.setSparePartsCost(convertBigDecimalToString(spareParts.getSparePartsCost()));
	                sparePartsResponse.setLabourCharge(convertBigDecimalToString(spareParts.getLabourCharge()));
	                sparePartsResponse.setTotalCost(convertBigDecimalToString(spareParts.getTotalCost()));
	                sparePartsResponse.setDamageSno(spareParts.getDamageSno());
	                return sparePartsResponse;
	            }).collect(Collectors.toList());

	            response.setResponse(sparePartsResponses);
	            response.setMessage("Success");
	            response.setErrors(Collections.emptyList());
	            response.setIsError(false);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.setMessage("Failed: An error occurred while retrieving spare parts details");
	        response.setErrors(Collections.singletonList(new ErrorList("500", "Exception", e.getMessage())));
	        response.setIsError(true);
	    }
	    return response;
	}

	private String convertBigDecimalToString(BigDecimal value) {
	    return value != null ? value.toString() : null;
	}
	
	@Override
	public CommonResponse viewsaveSparePartsDamageId(GarageSectionDetailsSaveReq req) {
	    CommonResponse response = new CommonResponse();
	    try {
	        String claimNo = req.getClaimNo();
	        String quotationNo = req.getQuotationNo();
	        String garageId = req.getGarageLoginId();
	        String damageSno=req.getDamageSno();
	        
	        VcSparePartsDetailsRequest nullResponse = new VcSparePartsDetailsRequest();

	        List<VcSparePartsDetails> sparePartsList = 
	            sparePartsDetailsRepo.findByClaimNumberAndQuotationNoAndGarageIdAndDamageSno(claimNo, quotationNo, garageId,damageSno);

	        if (sparePartsList != null && !sparePartsList.isEmpty()) {
	            List<VcSparePartsDetailsRequest> sparePartsResponses = sparePartsList.stream().map(spareParts -> {
	                VcSparePartsDetailsRequest sparePartsResponse = new VcSparePartsDetailsRequest();
	                
	                sparePartsResponse.setClaimNo(spareParts.getClaimNumber());
	                sparePartsResponse.setQuotationNo(spareParts.getQuotationNo());
	                sparePartsResponse.setGarageId(spareParts.getGarageId());
	                sparePartsResponse.setSparePartType(spareParts.getSparePartType());
	                sparePartsResponse.setSparePartTypeDesc(spareParts.getSparePartTypeDesc());
	                sparePartsResponse.setOriginalDiscount(convertBigDecimalToString(spareParts.getOriginalDiscount()));
	                sparePartsResponse.setDiscountPercentage(convertBigDecimalToString(spareParts.getDiscountPercentage()));
	                sparePartsResponse.setDiscountAmount(convertBigDecimalToString(spareParts.getDiscountAmount()));
	                sparePartsResponse.setReplacementCostDeductible(convertBigDecimalToString(spareParts.getReplacementCostDeductible()));
	                sparePartsResponse.setDamageType(spareParts.getDamageType());
	                sparePartsResponse.setDepreciationType(spareParts.getDepreciationType());
	                sparePartsResponse.setDepreciationTypeDesc(spareParts.getDepreciationTypeDesc());
	                sparePartsResponse.setDepreciation(convertBigDecimalToString(spareParts.getDepreciation()));
	                sparePartsResponse.setReferralStatus(spareParts.getReferralStatus());
	                sparePartsResponse.setRepairLabour(convertBigDecimalToString(spareParts.getRepairLabour()));
	                sparePartsResponse.setRepairLabourDiscount(convertBigDecimalToString(spareParts.getRepairLabourDiscount()));
	                sparePartsResponse.setRepairLabourDiscountAmount(convertBigDecimalToString(spareParts.getRepairLabourDiscountAmount()));
	                sparePartsResponse.setRepairLabourDeductible(convertBigDecimalToString(spareParts.getRepairLabourDeductible()));
	                sparePartsResponse.setTotalAmountRepairLabour(convertBigDecimalToString(spareParts.getTotalAmountRepairLabour()));
	                sparePartsResponse.setRemarks(spareParts.getRemarks());
	                sparePartsResponse.setDamageDirection(spareParts.getDamageDirection());
	                sparePartsResponse.setDamageDirectionDesc(spareParts.getDamageDirectionDesc());
	                sparePartsResponse.setPartType(spareParts.getPartType());
	                sparePartsResponse.setPartTypeDesc(spareParts.getPartTypeDesc());
	                sparePartsResponse.setReplaceRepair(spareParts.getReplaceRepair());
	                sparePartsResponse.setNoOfUnits(spareParts.getNoOfUnits() != null ? spareParts.getNoOfUnits().toString() : null);
	                sparePartsResponse.setSparePartsCost(convertBigDecimalToString(spareParts.getSparePartsCost()));
	                sparePartsResponse.setLabourCharge(convertBigDecimalToString(spareParts.getLabourCharge()));
	                sparePartsResponse.setTotalCost(convertBigDecimalToString(spareParts.getTotalCost()));
	                sparePartsResponse.setDamageSno(spareParts.getDamageSno());
	                return sparePartsResponse;
	            }).collect(Collectors.toList());

	            response.setResponse(sparePartsResponses);
	            response.setMessage("Success");
	            response.setErrors(Collections.emptyList());
	            response.setIsError(false);
	        } else {
	        	response.setResponse(nullResponse);
	            response.setMessage("Success");
	            response.setErrors(Collections.emptyList());
	            response.setIsError(false);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.setMessage("Failed: An error occurred while retrieving spare parts details");
	        response.setErrors(Collections.singletonList(new ErrorList("500", "Exception", e.getMessage())));
	        response.setIsError(true);
	    }
	    return response;
	}

	@Override
	public CommonResponse getDamageDetails(String companyId) {
		CommonResponse response = new CommonResponse();
	    try {
	        
	        List<GarageSectionDetailsSaveReq> groupedDamageDetails = new ArrayList<>();
	        

	        List<DropDownRes> damageDirection = dropDownServiceImpl.getDamageDirection(companyId);
	        
	        for (DropDownRes data : damageDirection) {
	            GarageSectionDetailsSaveReq res = new GarageSectionDetailsSaveReq();
	            
	            // Populate the fields from the retrieved data
	            res.setClaimNo("");
	            res.setQuotationNo("");
	            res.setDamageSno("");
	            res.setDamageDirection(data.getCodeDesc());
	            res.setDamageDirectionCode(data.getCode());
	            res.setDamagePart("");
	            res.setRepairReplace("");    
	            res.setNoOfUnits("");
	            res.setReplacementCharge("0.00");
	            res.setUnitPrice("0.00");
	            res.setGarageLoginId("");
	            res.setStatus("");
	            res.setDeductablePer("0.00");
	            res.setDeductableAmount("0.00");
	            res.setAsPerInvoice("");
	            groupedDamageDetails.add(res); 
	        }
	        
	        
	        // Set the response
	        response.setErrors(Collections.emptyList());
	        response.setMessage("Success");
	        response.setResponse(groupedDamageDetails);  
	        
	    } catch (Exception e) {
	        // Handle exceptions
	    	String exceptionDetails = e.getClass().getSimpleName() + ": " + e.getMessage();
	        response.setResponse(exceptionDetails);
	        response.setMessage("Failed");
	        response.setResponse(null);
	    }
	    return response;
	}

	@Override
	public CommonResponse viewSurveyorTotalAmount(GarageSectionDetailsSaveReq req) {
	    CommonResponse response = new CommonResponse();
	    try {
	        String claimNo = req.getClaimNo();
	        String quotationNo = req.getQuotationNo();

	        List<SparePartsSaveDetails> sparePartsList = sparePartsSaveRepo.findByClaimNoAndQuotationNo(claimNo, quotationNo);

	        if (sparePartsList != null && !sparePartsList.isEmpty()) {
	            List<TotalAmountViewResponse> sparePartsResponses = sparePartsList.stream().map(spareParts -> {
	                TotalAmountViewResponse sparePartsResponse = new TotalAmountViewResponse();

	                // Basic Details
	                sparePartsResponse.setClaimNo(spareParts.getClaimNo());
	                sparePartsResponse.setQuotationNo(spareParts.getQuotationNo());
	                sparePartsResponse.setGarageId(req.getGarageLoginId());

	                // Replacement (Spare Parts) Costs
	                sparePartsResponse.setSparePartsCost(convertBigDecimalToString(spareParts.getReplacementCost()));
	                sparePartsResponse.setSparePartsDepreciation(convertBigDecimalToString(spareParts.getSparePartDepreciation()));
	                sparePartsResponse.setSparePartsDiscount(convertBigDecimalToString(spareParts.getDiscountOnSpareParts()));
	                sparePartsResponse.setSparePartsDeductible(convertBigDecimalToString(spareParts.getReplacementCostDeductible()));
	                sparePartsResponse.setTotalAmountSpareParts(convertBigDecimalToString(spareParts.getTotalAmountReplacement()));

	                // Repair Labour Costs
	                sparePartsResponse.setRepairLabourCost(convertBigDecimalToString(spareParts.getRepairLabour()));
	                sparePartsResponse.setRepairLabourDiscount(convertBigDecimalToString(spareParts.getRepairLabourDiscountAmount()));
	                sparePartsResponse.setRepairLabourDeductible(convertBigDecimalToString(spareParts.getRepairLabourDeductible()));
	                sparePartsResponse.setTotalAmountRepairLabour(convertBigDecimalToString(spareParts.getTotalAmountRepairLabour()));

	                // Other Amounts
	                sparePartsResponse.setNetAmount(convertBigDecimalToString(spareParts.getNetAmount()));
//	                sparePartsResponse.setUnknownAccidentDeduction(StringUtils.isBlank(spareParts.getUnknownAccidentDeduction())?toBigDecimal("0"):convertBigDecimalToString(spareParts.getUnknownAccidentDeduction()));
//	                sparePartsResponse.setAmountToBeRecovered(StringUtils.isBlank(spareParts.getAmountToBeRecovered())?toBigDecimal("0"):convertBigDecimalToString(spareParts.getAmountToBeRecovered()));
	                sparePartsResponse.setTotalAfterDeduction(convertBigDecimalToString(spareParts.getTotalAfterDeductions()));

	                // VAT Details
//	                sparePartsResponse.setVatRate(StringUtils.isBlank(spareParts.getVatRate())?"0":convertBigDecimalToString(spareParts.getVatRatePercentage()));
	                sparePartsResponse.setVatAmount(convertBigDecimalToString(spareParts.getVatAmount()));
	                sparePartsResponse.setTotalAmountWithVAT(convertBigDecimalToString(spareParts.getTotalWithVat()));
	                sparePartsResponse.setVatRate(
	                	    (spareParts.getVatRate() == null || StringUtils.isBlank(spareParts.getVatRate().toString())) 
	                	        ? "0" 
	                	        : convertBigDecimalToString(spareParts.getVatRatePercentage())
	                	);
	                sparePartsResponse.setUnknownAccidentDeduction(
	                	    (spareParts.getUnknownAccidentDeduction() == null) 
	                	        ? "0" 
	                	        : convertBigDecimalToString(spareParts.getUnknownAccidentDeduction())
	                	);

	                	sparePartsResponse.setAmountToBeRecovered(
	                	    (spareParts.getAmountToBeRecovered() == null) 
	                	        ? "0" 
	                	        : convertBigDecimalToString(spareParts.getAmountToBeRecovered())
	                	);


	                return sparePartsResponse;
	            }).collect(Collectors.toList());

	            response.setResponse(sparePartsResponses);
	            response.setMessage("Success");
	            response.setErrors(Collections.emptyList());
	            response.setIsError(false);
	        } else {
	            response.setMessage("Failed: No records found for ClaimNo: " + claimNo);
	            response.setErrors(Collections.singletonList(new ErrorList("102", "ClaimNo", "No data found for claim number " + claimNo)));
	            response.setIsError(true);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.setMessage("Failed: An error occurred while retrieving spare parts details");
	        response.setErrors(Collections.singletonList(new ErrorList("500", "Exception", e.getMessage())));
	        response.setIsError(true);
	    }
	    return response;
	}

//	@Override
//	public CommonResponse saveSurveyorTotalAmount(TotalAmountViewResponse req) {
//	    CommonResponse response = new CommonResponse();
//	    try {
//	        // Perform validation
//	        List<ErrorList> errors = validation.validateSaveSurveyorTotalAmount(req);
//
//	        if (errors.isEmpty()) {
//	            // Check if record already exists in DB
//	            List<SparePartsSaveDetails> sparePartsList = sparePartsSaveRepo.findByClaimNoAndQuotationNo(
//	                req.getClaimNo(), req.getQuotationNo());
//
//	            SparePartsSaveDetails spareParts = new SparePartsSaveDetails();
//
//	            if (sparePartsList != null && !sparePartsList.isEmpty()) {
//	                spareParts = sparePartsList.get(0);
//	            }
//
//	            // Mapping request fields to entity
//	            spareParts.setClaimNo(req.getClaimNo());
//	            spareParts.setQuotationNo(req.getQuotationNo());
//
//	            // Mapping numeric fields safely
//	            spareParts.setReplacementCost(toBigDecimal(req.getSparePartsCost()));
//	            spareParts.setSparePartDepreciation(toBigDecimal(req.getSparePartsDepreciation()));
//	            spareParts.setDiscountOnSpareParts(toBigDecimal(req.getSparePartsDiscount()));
//	            spareParts.setReplacementCostDeductible(toBigDecimal(req.getSparePartsDeductible()));
//	            spareParts.setTotalAmountReplacement(toBigDecimal(req.getTotalAmountSpareParts()));
//
//	            spareParts.setRepairLabour(toBigDecimal(req.getRepairLabourCost()));
//	            spareParts.setRepairLabourDiscountAmount(toBigDecimal(req.getRepairLabourDiscount()));
//	            spareParts.setRepairLabourDeductible(toBigDecimal(req.getRepairLabourDeductible()));
//	            spareParts.setTotalAmountRepairLabour(toBigDecimal(req.getTotalAmountRepairLabour()));
//	            spareParts.setNetAmount(StringUtils.isBlank(req.getNetAmount())?toBigDecimal("0"):toBigDecimal(req.getNetAmount()));
//	            spareParts.setUnknownAccidentDeduction(StringUtils.isBlank(req.getUnknownAccidentDeduction())?toBigDecimal("0"):toBigDecimal(req.getUnknownAccidentDeduction()));
//	            spareParts.setAmountToBeRecovered(StringUtils.isBlank(req.getAmountToBeRecovered())?toBigDecimal("0"):toBigDecimal(req.getAmountToBeRecovered()));
//	            spareParts.setTotalAfterDeductions(toBigDecimal(req.getTotalAfterDeduction()));
//
//	            spareParts.setVatRatePercentage(StringUtils.isBlank(req.getVatRate())?toBigDecimal("0"):toBigDecimal(req.getVatRate()));
//	            spareParts.setVatAmount(toBigDecimal(req.getVatAmount()));
//	            spareParts.setTotalWithVat(toBigDecimal(req.getTotalAmountWithVAT()));
//
//	            // Save the entity to the repository
//	            sparePartsSaveRepo.save(spareParts);
//
//	            response.setErrors(Collections.emptyList());
//	            response.setMessage("Success: Surveyor total amount saved successfully.");
//	            response.setResponse(Collections.singletonList("Surveyor total amount saved successfully."));
//	            response.setIsError(false);
//	        } else {
//	            response.setErrors(errors);
//	            response.setMessage("Validation Failed");
//	            response.setResponse(Collections.emptyList());
//	            response.setIsError(true);
//	        }
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        response.setErrors(Collections.singletonList(new ErrorList("500", "Exception", e.getMessage())));
//	        response.setMessage("Failed: Error occurred while saving surveyor total amount.");
//	        response.setResponse(Collections.emptyList());
//	        response.setIsError(true);
//	    }
//	    return response;
//	}


	@Override
	public CommonResponse saveSurveyorTotalAmount(TotalAmountViewResponse req) {
	    CommonResponse response = new CommonResponse();
	    try {
	        // Perform validation
	        List<ErrorList> errors = validation.validateSaveSurveyorTotalAmount(req);

	        if (!errors.isEmpty()) {
	            response.setErrors(errors);
	            response.setMessage("Validation Failed");
	            response.setResponse(Collections.emptyList());
	            response.setIsError(true);
	            return response;
	        }

	        // Check if record already exists in DB
	        List<SparePartsSaveDetails> sparePartsList = sparePartsSaveRepo.findByClaimNoAndQuotationNo(
	                req.getClaimNo(), req.getQuotationNo());

	        SparePartsSaveDetails spareParts = sparePartsList != null && !sparePartsList.isEmpty() 
	                ? sparePartsList.get(0) 
	                : new SparePartsSaveDetails();

	        // Mapping request fields to entity
	        spareParts.setClaimNo(req.getClaimNo());
	        spareParts.setQuotationNo(req.getQuotationNo());

	        List<DamageSectionDetails> damageList = repository.findByClaimNoAndQuotationNo(
	                req.getClaimNo(), req.getQuotationNo());

	        // Initializing BigDecimal values
	        BigDecimal sparePartsCost = BigDecimal.ZERO;
	        BigDecimal sparePartsDepreciation = BigDecimal.ZERO;
	        BigDecimal sparePartsDiscount = BigDecimal.ZERO;
	        BigDecimal sparePartsDeductible = BigDecimal.ZERO;
	        BigDecimal totalAmountSpareParts = BigDecimal.ZERO;
	        BigDecimal repairLabourCost = BigDecimal.ZERO;
	        BigDecimal repairLabourDiscount = BigDecimal.ZERO;
	        BigDecimal repairLabourDeductible = BigDecimal.ZERO;
	        BigDecimal totalAmountRepairLabour = BigDecimal.ZERO;
	        BigDecimal netAmount = BigDecimal.ZERO;
	        BigDecimal totalAfterDeduction = BigDecimal.ZERO;
	        BigDecimal vatAmount = BigDecimal.ZERO;
	        BigDecimal totalAmountWithVAT = BigDecimal.ZERO;

	        BigDecimal unknownAccidentDeduction = toBigDecimal(req.getUnknownAccidentDeduction());
	        BigDecimal amountToBeRecovered = toBigDecimal(req.getAmountToBeRecovered());
	        BigDecimal vatRate = toBigDecimal(req.getVatRate());

	        for (DamageSectionDetails dam : damageList) {
	            if (dam == null) continue;

	            VcSparePartsDetails spare = sparePartsDetailsRepo.findByClaimNumberAndQuotationNoAndDamageSno(
	                    req.getClaimNo(), req.getQuotationNo(), String.valueOf(dam.getDamageSno()));

	            if ("REPLACE".equalsIgnoreCase(dam.getRepairReplace())) {
	                BigDecimal noOfUnits = dam.getNoOfParts() != null ? new BigDecimal(dam.getNoOfParts().toString()) : BigDecimal.ZERO;
	                BigDecimal unitPrice = dam.getGaragePrice() != null ? new BigDecimal(dam.getGaragePrice().toString()) : BigDecimal.ZERO;

	                if ("Dealer".equalsIgnoreCase(dam.getGarageDealer()) && dam.getDealerPrice() != null) {
	                    unitPrice = new BigDecimal(dam.getDealerPrice().toString());
	                }

	                sparePartsCost = sparePartsCost.add(noOfUnits.multiply(unitPrice));
	            }

	            repairLabourCost = repairLabourCost.add(dam.getReplaceCost() != null ? dam.getReplaceCost() : BigDecimal.ZERO);

	            if (spare != null) {
	                if ("REPLACE".equalsIgnoreCase(spare.getReplaceRepair())) {
	                    sparePartsDepreciation = sparePartsDepreciation.add(spare.getDepreciation() != null ? spare.getDepreciation() : BigDecimal.ZERO);
	                    sparePartsDiscount = sparePartsDiscount.add(spare.getDiscountAmount() != null ? spare.getDiscountAmount() : BigDecimal.ZERO);
	                    sparePartsDeductible = sparePartsDeductible.add(spare.getReplacementCostDeductible() != null ? spare.getReplacementCostDeductible() : BigDecimal.ZERO);
	                }

	                repairLabourDiscount = repairLabourDiscount.add(spare.getRepairLabourDiscountAmount() != null ? spare.getRepairLabourDiscountAmount() : BigDecimal.ZERO);
	                repairLabourDeductible = repairLabourDeductible.add(spare.getRepairLabourDeductible() != null ? spare.getRepairLabourDeductible() : BigDecimal.ZERO);
	            }
	        }

	        // Calculating total values
	        totalAmountSpareParts = sparePartsCost.subtract(sparePartsDepreciation).subtract(sparePartsDiscount).subtract(sparePartsDeductible);
	        totalAmountRepairLabour = repairLabourCost.subtract(repairLabourDiscount).subtract(repairLabourDeductible);
	        netAmount = totalAmountSpareParts.add(totalAmountRepairLabour);
	        totalAfterDeduction = netAmount.subtract(unknownAccidentDeduction).subtract(amountToBeRecovered);
	        vatAmount = vatRate.divide(BigDecimal.valueOf(100)).multiply(totalAfterDeduction);
	        totalAmountWithVAT = totalAfterDeduction.add(vatAmount);

	        // Setting calculated fields
	        spareParts.setReplacementCost(sparePartsCost);
	        spareParts.setSparePartDepreciation(sparePartsDepreciation);
	        spareParts.setDiscountOnSpareParts(sparePartsDiscount);
	        spareParts.setReplacementCostDeductible(sparePartsDeductible);
	        spareParts.setTotalAmountReplacement(totalAmountSpareParts);
	        spareParts.setRepairLabour(repairLabourCost);
	        spareParts.setRepairLabourDiscountAmount(repairLabourDiscount);
	        spareParts.setRepairLabourDeductible(repairLabourDeductible);
	        spareParts.setTotalAmountRepairLabour(totalAmountRepairLabour);
	        spareParts.setNetAmount(netAmount);
	        spareParts.setUnknownAccidentDeduction(unknownAccidentDeduction);
	        spareParts.setAmountToBeRecovered(amountToBeRecovered);
	        spareParts.setTotalAfterDeductions(totalAfterDeduction);
	        spareParts.setVatRatePercentage(vatRate);
	        spareParts.setVatAmount(vatAmount);
	        spareParts.setTotalWithVat(totalAmountWithVAT);

	        // Save the entity to the repository
	        sparePartsSaveRepo.save(spareParts);

	        response.setErrors(Collections.emptyList());
	        response.setMessage("Success: Surveyor total amount saved successfully.");
	        response.setResponse(Collections.singletonList("Surveyor total amount saved successfully."));
	        response.setIsError(false);

	    } catch (Exception e) {
	        e.printStackTrace();
	        response.setErrors(Collections.singletonList(new ErrorList("500", "Exception", e.getMessage())));
	        response.setMessage("Failed: Error occurred while saving surveyor total amount.");
	        response.setResponse(Collections.emptyList());
	        response.setIsError(true);
	    }
	    return response;
	}





}
