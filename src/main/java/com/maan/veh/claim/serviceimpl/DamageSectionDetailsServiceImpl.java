package com.maan.veh.claim.serviceimpl;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.veh.claim.entity.DamageSectionDetails;
import com.maan.veh.claim.entity.TotalAmountDetails;
import com.maan.veh.claim.repository.DamageSectionDetailsRepository;
import com.maan.veh.claim.repository.TotalAmountDetailsRepository;
import com.maan.veh.claim.request.DamageSectionDetailsRequest;
import com.maan.veh.claim.request.DamageSectionDetailsSaveReq;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.DamageSectionDetailsResponse;
import com.maan.veh.claim.response.ErrorList;
import com.maan.veh.claim.service.DamageSectionDetailsService;

@Service
public class DamageSectionDetailsServiceImpl implements DamageSectionDetailsService {
	
	private static SimpleDateFormat DD_MM_YYYY = new SimpleDateFormat("dd/MM/yyyy");
	
	@Autowired
	private DamageSectionDetailsRepository repository;
	
	@Autowired
    private TotalAmountDetailsRepository totalAmountDetailsRepository;
	
	@Autowired
    private InputValidationUtil validation;

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
	        	
	        	List<DamageSectionDetails> saveList = new ArrayList<DamageSectionDetails>();
	        	int damageSno = 1;
	        	String claimNo = reqList.get(0).getClaimNo();
	            for (DamageSectionDetailsSaveReq req:reqList) {
	            	
	            	DamageSectionDetails details = new DamageSectionDetails();
	            	
					details.setClaimNo(req.getClaimNo());
					details.setQuotationNo(req.getQuotationNo());
					details.setDamageSno(damageSno);
					details.setDamageDirection(req.getDamageDirection());
					details.setDamagePart(req.getDamagePart());
					details.setRepairReplace(req.getRepairReplace());
					details.setNoOfParts(Integer.valueOf(req.getNoOfParts()));
					details.setGaragePrice(new BigDecimal(req.getGaragePrice()));
					details.setDealerPrice(new BigDecimal(req.getDealerPrice()));
					details.setGarageLoginId(req.getGarageLoginId());
					details.setDealerLoginId(req.getDealerLoginId());
					details.setSurveyorId(req.getSurveyorId());
					details.setReplaceCost(new BigDecimal(req.getReplaceCost()));
					details.setReplaceCostDeduct(new BigDecimal(req.getReplaceCostDeduct()));
					details.setSparepartDeprection(new BigDecimal(req.getSparepartDeprection()));
					details.setDiscountSparepart(new BigDecimal(req.getDiscountSparepart()));
					details.setTotamtReplace(new BigDecimal(req.getTotamtReplace()));
					details.setLabourCost(new BigDecimal(req.getLabourCost()));
					details.setLabourCostDeduct(new BigDecimal(req.getLabourCostDeduct()));
					details.setLabourDisc(new BigDecimal(req.getLabourDisc()));
					details.setTotamtOfLabour(new BigDecimal(req.getTotamtOfLabour()));
					details.setTotPrice(new BigDecimal(req.getTotPrice()));
					details.setStatus(req.getStatus());
					details.setEntryDate(new Date());
					
					saveList.add(details);
					damageSno++;
				}
				repository.saveAllAndFlush(saveList);
				
				//Create or Update a Record in total_amount_details
				updateTotalAmountDetails(claimNo);

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


}
