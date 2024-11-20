package com.maan.veh.claim.serviceimpl;

import java.math.BigDecimal;
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
import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.veh.claim.entity.DamageSectionDetails;
import com.maan.veh.claim.entity.GarageWorkOrder;
import com.maan.veh.claim.entity.TotalAmountDetails;
import com.maan.veh.claim.entity.VcSparePartsDetails;
import com.maan.veh.claim.repository.DamageSectionDetailsRepository;
import com.maan.veh.claim.repository.GarageWorkOrderRepository;
import com.maan.veh.claim.repository.TotalAmountDetailsRepository;
import com.maan.veh.claim.repository.VcSparePartsDetailsRepository;
import com.maan.veh.claim.request.DamageSectionDetailsRequest;
import com.maan.veh.claim.request.DamageSectionDetailsSaveReq;
import com.maan.veh.claim.request.DealerSectionDetailsSaveReq;
import com.maan.veh.claim.request.GarageSectionDetailsSaveReq;
import com.maan.veh.claim.request.VcSparePartsDetailsRequest;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.DamageSectionDetailsResponse;
import com.maan.veh.claim.response.ErrorList;
import com.maan.veh.claim.service.DamageSectionDetailsService;

@Service
public class DamageSectionDetailsServiceImpl implements DamageSectionDetailsService {
	
	//private static SimpleDateFormat DD_MM_YYYY = new SimpleDateFormat("dd/MM/yyyy");
	
	@Autowired
	private DamageSectionDetailsRepository repository;
	
	@Autowired
    private TotalAmountDetailsRepository totalAmountDetailsRepository;
	
	@Autowired
	private GarageWorkOrderRepository garageWorkOrderRepo;
	
	@Autowired
    private InputValidationUtil validation;
	
	@Autowired
	private VcSparePartsDetailsRepository sparePartsDetailsRepo;

	@Override
	public List<DamageSectionDetailsResponse> getDamageDetailsByClaimNo(DamageSectionDetailsRequest request) {
		try {
			List<DamageSectionDetails> detailsList = repository.findByClaimNo(request.getClaimNo());

			return detailsList.stream().map(this::mapToResponse).collect(Collectors.toList());
		} catch (Exception e) {
			throw new ServiceException("Error fetching damage details", e);
		}
	}

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

	            // Create or Update a Record in total_amount_details
	            //updateTotalAmountDetails(claimNo);
	            
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

	public void updateTotalAmountDetails(String claimNo) {
	    // Aggregate data for the specific claim_no
	    List<DamageSectionDetails> damageDetailsList = repository.findByClaimNo(claimNo);
	    
	    BigDecimal netAmount = BigDecimal.ZERO;
	    BigDecimal totAmtAfterDeduct = BigDecimal.ZERO;
	    BigDecimal vatPercent = new BigDecimal("18.00");
	    BigDecimal vatRate = BigDecimal.ZERO;
	    BigDecimal vatAmount = BigDecimal.ZERO;
	    BigDecimal totAmtWithVat = BigDecimal.ZERO;
	    BigDecimal totalReplaceCost = BigDecimal.ZERO;
	    BigDecimal totalLabourCost = BigDecimal.ZERO;

	    BigDecimal totDeduct = BigDecimal.ZERO;
	    
	    for (DamageSectionDetails damageDetails : damageDetailsList) {
	    	
	    	if(damageDetails.getRepairReplace().equalsIgnoreCase("REPAIR")) {
	    		
	    		totDeduct = totDeduct.add(damageDetails.getLabourCostDeduct());
		    	netAmount = netAmount.add(damageDetails.getTotamtOfLabour());
	    		
	    	}else if(damageDetails.getRepairReplace().equalsIgnoreCase("REPLACE")) {
	    		
	    		totDeduct = totDeduct.add(damageDetails.getReplaceCostDeduct());
		    	netAmount = netAmount.add(damageDetails.getTotamtReplace());
	    	}
	    	
	        totalReplaceCost = totalReplaceCost.add(damageDetails.getTotamtReplace());
	        totalLabourCost = totalLabourCost.add(damageDetails.getTotamtOfLabour());
	        
	    }

	    // Calculate VAT and other related amounts
        vatRate = vatPercent.divide(new BigDecimal("100"));
	    vatAmount = netAmount.multiply(vatRate);
	    totAmtWithVat = netAmount.add(vatAmount);
	    totAmtAfterDeduct = totAmtWithVat.subtract(totDeduct);

	    // Create or update a record in total_amount_details
	    TotalAmountDetails totalAmountDetails = totalAmountDetailsRepository.findByClaimNo(claimNo)
	            .orElse(new TotalAmountDetails());

	    totalAmountDetails.setClaimNo(claimNo);

	    // Update calculated values
	    totalAmountDetails.setNetAmount(netAmount);                  // Total_amount of Repair or Replace
	    totalAmountDetails.setTotamtAftDeduction(totAmtAfterDeduct); // Total amount after deduction from repair or replace
	    totalAmountDetails.setVatRatePercent(vatPercent);            // VAT rate percent is fixed at 18.00%
	    totalAmountDetails.setVatRate(vatRate);                      // Calculated VAT rate based on VAT rate percent
	    totalAmountDetails.setVatAmount(vatAmount);                  // The amount of VAT based on net amount
	    totalAmountDetails.setTotamtWithVat(totAmtWithVat);          // Total amount with VAT
	    totalAmountDetails.setTotReplaceCost(totalReplaceCost);      // Total Replace Cost
	    totalAmountDetails.setTotLabourCost(totalLabourCost);        // Total Labour Cost

	    totalAmountDetails.setEntryDate(new Date());
	    totalAmountDetails.setCreatedBy("System"); 
	    totalAmountDetails.setStatus("Y");

	    totalAmountDetailsRepository.saveAndFlush(totalAmountDetails);
	}

	@Override
	public CommonResponse saveGarageDamageSectionDetails(List<GarageSectionDetailsSaveReq> reqList) {
		CommonResponse response = new CommonResponse();
	    try {
	        List<ErrorList> errors = validation.validateGarageDamageDetails(reqList);
	        if (errors.isEmpty()) {
	        	
	        	List<DamageSectionDetails> saveList = new ArrayList<DamageSectionDetails>();
	        	int damageSno = 1;
	        	
	            for (GarageSectionDetailsSaveReq req:reqList) {
	            	
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
					
					if ("Replace".equalsIgnoreCase(req.getRepairReplace())) {
						details.setNoOfParts(Integer.valueOf(req.getNoOfUnits()));
						details.setReplaceCost(new BigDecimal(req.getReplacementCharge()));
						details.setLabourCost(new BigDecimal(req.getReplacementCharge()));
						details.setGaragePrice(new BigDecimal(req.getUnitPrice()));
					}else {
						details.setNoOfParts(Integer.valueOf(req.getNoOfUnits()));
						details.setReplaceCost(new BigDecimal(req.getReplacementCharge()));
						details.setGaragePrice(new BigDecimal(req.getReplacementCharge()));
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
	            res.setReplacementCharge(data.getReplaceCost() != null ? data.getReplaceCost().toString() : "");
	            if("REPLACE".equalsIgnoreCase(data.getRepairReplace())) {
	            	res.setUnitPrice(data.getGaragePrice() != null ? data.getGaragePrice().toString() : "");
	            }
	            res.setGarageLoginId(data.getGarageLoginId());
	            res.setStatus(data.getStatus());
	            
	            groupedDamageDetails.add(res); 
	        }
	        
//	        groupedDamageDetails = groupedDamageDetails.stream()
//	        	    .filter(res -> "Replace".equalsIgnoreCase(res.getRepairReplace())) // Filter condition
//	        	    .collect(Collectors.toList()); // Collect the filtered results back to a list
	        
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
	            VcSparePartsDetails spareParts = sparePartsDetailsRepo.findByClaimNumberAndQuotationNo(req.getClaimNo(),req.getQuotationNo());
	            if (spareParts == null) {
	                spareParts = new VcSparePartsDetails();
	            }
	            
	            // Mapping fields from request to entity, with necessary type conversions
	            spareParts.setClaimNumber(req.getClaimNo());
	            spareParts.setQuotationNo(req.getQuotationNo());
	            spareParts.setGarageId(req.getGarageId());
	            spareParts.setReplacementCost(parseBigDecimal(req.getReplacementCost()));
	            spareParts.setReplacementCostDeductible(parseBigDecimal(req.getReplacementCostDeductible()));
	            spareParts.setSparePartDepreciation(parseBigDecimal(req.getSparePartDepreciation()));
	            spareParts.setDiscountOnSpareParts(parseBigDecimal(req.getDiscountOnSpareParts()));
	            spareParts.setTotalAmountReplacement(parseBigDecimal(req.getTotalAmountReplacement()));
	            spareParts.setRepairLabour(parseBigDecimal(req.getRepairLabour()));
	            spareParts.setRepairLabourDeductible(parseBigDecimal(req.getRepairLabourDeductible()));
	            spareParts.setRepairLabourDiscountAmount(parseBigDecimal(req.getRepairLabourDiscountAmount()));
	            spareParts.setTotalAmountRepairLabour(parseBigDecimal(req.getTotalAmountRepairLabour()));
	            spareParts.setNetAmount(parseBigDecimal(req.getNetAmount()));
	            spareParts.setUnknownAccidentDeduction(parseBigDecimal(req.getUnknownAccidentDeduction()));
	            spareParts.setAmountToBeRecovered(parseBigDecimal(req.getAmountToBeRecovered()));
	            spareParts.setTotalAfterDeductions(parseBigDecimal(req.getTotalAfterDeductions()));
	            spareParts.setVatRatePer(parseBigDecimal(req.getVatRatePer()));
	            spareParts.setVatRate(parseBigDecimal(req.getVatRate()));
	            spareParts.setVatAmount(parseBigDecimal(req.getVatAmount()));
	            spareParts.setTotalWithVAT(parseBigDecimal(req.getTotalWithVAT()));
	            spareParts.setSalvageDeduction(parseBigDecimal(req.getSalvageDeduction()));
	            
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

	private BigDecimal parseBigDecimal(String value) {
	    if (StringUtils.isBlank(value)) {
	        return BigDecimal.ZERO; // default value, if blank or null
	    }
	    try {
	        return new BigDecimal(value);
	    } catch (NumberFormatException e) {
	        e.printStackTrace();
	        return BigDecimal.ZERO; // or handle error as needed
	    }
	}

	@Override
	public CommonResponse viewsaveSpareParts(GarageSectionDetailsSaveReq req) {
	    CommonResponse response = new CommonResponse();
	    
	    try {
	        // Retrieve claim number from request
	        String claimNo = req.getClaimNo();
	        
	        // Retrieve spare parts details from the database using claim number
	        VcSparePartsDetails spareParts = sparePartsDetailsRepo.findByClaimNumberAndQuotationNo(claimNo,req.getQuotationNo());
	        
	        // Check if record exists for the provided claim number
	        if (spareParts != null) {
	            // Map entity data to VcSparePartsDetailsRequest
	            VcSparePartsDetailsRequest sparePartsResponse = new VcSparePartsDetailsRequest();
	            sparePartsResponse.setClaimNo(spareParts.getClaimNumber());
	            sparePartsResponse.setQuotationNo(spareParts.getQuotationNo());
	            sparePartsResponse.setGarageId(spareParts.getGarageId());
	            sparePartsResponse.setReplacementCost(spareParts.getReplacementCost().toString());
	            sparePartsResponse.setReplacementCostDeductible(spareParts.getReplacementCostDeductible().toString());
	            sparePartsResponse.setSparePartDepreciation(spareParts.getSparePartDepreciation().toString());
	            sparePartsResponse.setDiscountOnSpareParts(spareParts.getDiscountOnSpareParts().toString());
	            sparePartsResponse.setTotalAmountReplacement(spareParts.getTotalAmountReplacement().toString());
	            sparePartsResponse.setRepairLabour(spareParts.getRepairLabour().toString());
	            sparePartsResponse.setRepairLabourDeductible(spareParts.getRepairLabourDeductible().toString());
	            sparePartsResponse.setRepairLabourDiscountAmount(spareParts.getRepairLabourDiscountAmount().toString());
	            sparePartsResponse.setTotalAmountRepairLabour(spareParts.getTotalAmountRepairLabour().toString());
	            sparePartsResponse.setNetAmount(spareParts.getNetAmount().toString());
	            sparePartsResponse.setUnknownAccidentDeduction(spareParts.getUnknownAccidentDeduction() != null 
	                ? spareParts.getUnknownAccidentDeduction().toString() : "0.00");
	            sparePartsResponse.setAmountToBeRecovered(spareParts.getAmountToBeRecovered() != null 
	                ? spareParts.getAmountToBeRecovered().toString() : "0.00");
	            sparePartsResponse.setTotalAfterDeductions(spareParts.getTotalAfterDeductions().toString());
	            sparePartsResponse.setVatRatePer(spareParts.getVatRatePer() != null 
	                ? spareParts.getVatRatePer().toString() : "0.00");
	            sparePartsResponse.setVatRate(spareParts.getVatRate() != null 
	                ? spareParts.getVatRate().toString() : "0.00");
	            sparePartsResponse.setVatAmount(spareParts.getVatAmount().toString());
	            sparePartsResponse.setTotalWithVAT(spareParts.getTotalWithVAT().toString());
	            sparePartsResponse.setSalvageDeduction(spareParts.getSalvageDeduction().toString());
	            
	            // Set the response with the mapped data
	            response.setResponse(sparePartsResponse);
	            response.setMessage("Success");
	            response.setErrors(Collections.emptyList());
	            response.setIsError(false);
	        } 
//	        else {
//	            // If no record found, set appropriate error message
//	            response.setMessage("Failed: No record found for claim number " + claimNo);
//	            response.setErrors(Collections.singletonList(new ErrorList("102", "ClaimNumber", "No data found for claim number " + claimNo)));
//	            response.setIsError(true);
//	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        response.setMessage("Failed: An error occurred while retrieving spare parts details");
	        response.setErrors(Collections.singletonList(new ErrorList("500", "Exception", e.getMessage())));
	        response.setIsError(true);
	    }
	    
	    return response;
	}


}
