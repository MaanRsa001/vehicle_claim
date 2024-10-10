package com.maan.veh.claim.serviceimpl;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.veh.claim.entity.DamageSectionDetails;
import com.maan.veh.claim.entity.TotalAmountDetails;
import com.maan.veh.claim.repository.DamageSectionDetailsRepository;
import com.maan.veh.claim.repository.TotalAmountDetailsRepository;
import com.maan.veh.claim.request.TotalAmountDetailsRequest;
import com.maan.veh.claim.request.TotalAmountDetailsSaveRequest;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.ErrorList;
import com.maan.veh.claim.service.TotalAmountDetailsService;

@Service
public class TotalAmountDetailsServiceImpl implements TotalAmountDetailsService {

    @Autowired
    private TotalAmountDetailsRepository repository;
    
	@Autowired
	private DamageSectionDetailsRepository damageSectionDetailsRepo;
    
    @Autowired
    private InputValidationUtil validation;

    @Override
    public CommonResponse getTotalAmountDetailsByClaimNo(TotalAmountDetailsRequest req) {
        CommonResponse response = new CommonResponse();
        try {
            // Retrieve data from the repository
            Optional<TotalAmountDetails> totalAmountDetails = repository.findByClaimNo(req.getClaimNo());

            // Prepare the response
            Map<String, String> data = new HashMap<>();
            if (totalAmountDetails.isPresent()) {
                TotalAmountDetails details = totalAmountDetails.get();
                data.put("ClaimNo", details.getClaimNo());
                data.put("NetAmount", details.getNetAmount() != null ? details.getNetAmount().toString() : null);
                data.put("TotamtAftDeduction", details.getTotamtAftDeduction() != null ? details.getTotamtAftDeduction().toString() : null);
                data.put("VatRatePercent", details.getVatRatePercent() != null ? details.getVatRatePercent().toString() : null);
                data.put("VatRate", details.getVatRate() != null ? details.getVatRate().toString() : null);
                data.put("VatAmount", details.getVatAmount() != null ? details.getVatAmount().toString() : null);
                data.put("TotamtWithVat", details.getTotamtWithVat() != null ? details.getTotamtWithVat().toString() : null);
                data.put("EntryDate", details.getEntryDate() != null ? details.getEntryDate().toString() : null);
                data.put("CreatedBy", details.getCreatedBy());
                data.put("Status", details.getStatus());
                data.put("TotReplaceCost", details.getTotReplaceCost() != null ? details.getTotReplaceCost().toString() : null);
                data.put("TotLabourCost", details.getTotLabourCost() != null ? details.getTotLabourCost().toString() : null);

                response.setMessage("Data retrieved successfully");
                response.setResponse(data);
                response.setErrors(Collections.emptyList());
                response.setIsError(false);
            } else {
                response.setMessage("No data found for the given claim number");
                response.setResponse(Collections.emptyMap());
                response.setErrors(Collections.emptyList());
                response.setIsError(true);
            }
        } catch (Exception e) {
            // Handle any unexpected errors
            response.setMessage("Failed to retrieve data");
            response.setResponse(null);
            response.setErrors(Collections.singletonList(e.getMessage()));
            response.setIsError(true);
        }
        return response;
    }

//    @Override
//    public CommonResponse saveTotalAmountDetails(TotalAmountDetailsRequest req) {
//        CommonResponse response = new CommonResponse();
//        try {
//            List<ErrorList> error = validation.validateTotalAmountDetails(req);
//            if (error.size() == 0) {
//                
//            	 // Aggregate data for the specific claim_no
//        	    List<DamageSectionDetails> damageDetailsList = damageSectionDetailsRepo.findByClaimNo(req.getClaimNo());
//        	    
//        	    BigDecimal netAmount = BigDecimal.ZERO;
//        	    BigDecimal totAmtAfterDeduct = BigDecimal.ZERO;
//        	    BigDecimal vatPercent = new BigDecimal("18.00");
//        	    BigDecimal vatRate = BigDecimal.ZERO;
//        	    BigDecimal vatAmount = BigDecimal.ZERO;
//        	    BigDecimal totAmtWithVat = BigDecimal.ZERO;
//        	    BigDecimal totalReplaceCost = BigDecimal.ZERO;
//        	    BigDecimal totalLabourCost = BigDecimal.ZERO;
//
//        	    BigDecimal totDeduct = BigDecimal.ZERO;
//        	    
//        	    for (DamageSectionDetails damageDetails : damageDetailsList) {
//        	    	
//        	    	if(damageDetails.getRepairReplace().equalsIgnoreCase("REPAIR")) {
//        	    		
//        	    		totDeduct = totDeduct.add(damageDetails.getLabourCostDeduct());
//        		    	netAmount = netAmount.add(damageDetails.getTotamtOfLabour());
//        	   	        totalLabourCost = totalLabourCost.add(damageDetails.getTotamtOfLabour());
//        	    		
//        	    	}else if(damageDetails.getRepairReplace().equalsIgnoreCase("REPLACE")) {
//        	    		
//        	    		totDeduct = totDeduct.add(damageDetails.getReplaceCostDeduct());
//        		    	netAmount = netAmount.add(damageDetails.getTotamtReplace());
//        		    	totalReplaceCost = totalReplaceCost.add(damageDetails.getTotamtReplace());
//        	    	}
//        	    	
//        	        
//        	    }
//
//        	    // Calculate VAT and other related amounts
//                vatRate = vatPercent.divide(new BigDecimal("100"));
//        	    vatAmount = netAmount.multiply(vatRate);
//        	    totAmtWithVat = netAmount.add(vatAmount);
//        	    totAmtAfterDeduct = totAmtWithVat.subtract(totDeduct);
//
//        	    // Create or update a record in total_amount_details
//        	    TotalAmountDetails totalAmountDetails = repository.findByClaimNo(req.getClaimNo())
//        	            .orElse(new TotalAmountDetails());
//
//        	    totalAmountDetails.setClaimNo(req.getClaimNo());
//
//        	    // Update calculated values
//        	    totalAmountDetails.setNetAmount(netAmount);                  // Total_amount of Repair or Replace
//        	    totalAmountDetails.setTotamtAftDeduction(totAmtAfterDeduct); // Total amount after deduction from repair or replace
//        	    totalAmountDetails.setVatRatePercent(vatPercent);            // VAT rate percent is fixed at 18.00%
//        	    totalAmountDetails.setVatRate(vatRate);                      // Calculated VAT rate based on VAT rate percent
//        	    totalAmountDetails.setVatAmount(vatAmount);                  // The amount of VAT based on net amount
//        	    totalAmountDetails.setTotamtWithVat(totAmtWithVat);          // Total amount with VAT
//        	    totalAmountDetails.setTotReplaceCost(totalReplaceCost);      // Total Replace Cost
//        	    totalAmountDetails.setTotLabourCost(totalLabourCost);        // Total Labour Cost
//
//        	    totalAmountDetails.setEntryDate(new Date());
//        	    totalAmountDetails.setCreatedBy("System"); 
//        	    totalAmountDetails.setStatus("Y");
//
//        	    repository.saveAndFlush(totalAmountDetails);
//
//                response.setErrors(Collections.emptyList());
//                response.setMessage("Success");
//                response.setResponse(Collections.emptyList());
//            } else {
//                response.setErrors(error);
//                response.setMessage("Failed");
//                response.setResponse(Collections.emptyList());
//                response.setIsError(true);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            response.setErrors(Collections.emptyList());
//            response.setMessage("Failed");
//            response.setResponse(e.getMessage());
//        }
//        return response;
//    }
    
    @Override
    public CommonResponse saveTotalAmountDetails(TotalAmountDetailsRequest req) {
        CommonResponse response = new CommonResponse();
        try {
            // Step 1: Validate the request
            List<ErrorList> error = validation.validateTotalAmountDetails(req);
            if (error.isEmpty()) {

                // Step 2: Fetch all damage details for the specific claim_no
                List<DamageSectionDetails> damageDetailsList = damageSectionDetailsRepo.findByClaimNo(req.getClaimNo());

                // Step 3: Initialize variables for original and new calculations
                BigDecimal netAmount = BigDecimal.ZERO;
                BigDecimal totAmtAfterDeduct = BigDecimal.ZERO;
                BigDecimal vatPercent = new BigDecimal("18.00");
                BigDecimal vatRate = vatPercent.divide(new BigDecimal("100"));
                BigDecimal vatAmount = BigDecimal.ZERO;
                BigDecimal totAmtWithVat = BigDecimal.ZERO;
                BigDecimal totalReplaceCost = BigDecimal.ZERO;
                BigDecimal totalLabourCost = BigDecimal.ZERO;
                BigDecimal totDeduct = BigDecimal.ZERO;

                BigDecimal insuranceAmount = BigDecimal.ZERO;
                BigDecimal customerAmount = BigDecimal.ZERO;
                BigDecimal insuranceVatAmount = BigDecimal.ZERO;
                BigDecimal customerVatAmount = BigDecimal.ZERO;

                for (DamageSectionDetails damageDetails : damageDetailsList) {

                    // Step 4: Process repair-type damages
                    if (damageDetails.getRepairReplace().equalsIgnoreCase("REPAIR")) {

                        // Total labor cost and deductions for repair
                        BigDecimal labourCost = damageDetails.getLabourCost();
                        BigDecimal labourCostDeduct = labourCost.multiply(damageDetails.getLabourCostDeductPercentage()).divide(new BigDecimal("100"));
                        BigDecimal labourCostDiscount = labourCost.multiply(damageDetails.getLabourDiscPercentage()).divide(new BigDecimal("100"));

                        BigDecimal insuranceAmt = labourCost.subtract(labourCostDeduct).subtract(labourCostDiscount);
                        BigDecimal customerAmt = labourCost.subtract(insuranceAmt);

                        BigDecimal insuranceVat = insuranceAmt.multiply(vatRate);
                        BigDecimal customerVat = customerAmt.multiply(vatRate);

                        insuranceAmount = insuranceAmount.add(insuranceAmt);
                        customerAmount = customerAmount.add(customerAmt);
                        insuranceVatAmount = insuranceVatAmount.add(insuranceVat);
                        customerVatAmount = customerVatAmount.add(customerVat);

                        // Add the labor cost to totals
                        netAmount = netAmount.add(labourCost);
                        totalLabourCost = totalLabourCost.add(labourCost);
                        totDeduct = totDeduct.add(labourCostDeduct);

                    } 
                    // Step 5: Process replacement-type damages
                    else if (damageDetails.getRepairReplace().equalsIgnoreCase("REPLACE")) {

                        // Total replacement cost and deductions
                        BigDecimal noOfParts = new BigDecimal(damageDetails.getNoOfParts());
                        BigDecimal dealerPrice = damageDetails.getDealerPrice();
                        BigDecimal replaceCost = damageDetails.getReplaceCost();

                        BigDecimal totalReplaceCostForDamage = noOfParts.multiply(dealerPrice).add(replaceCost);

                        BigDecimal replaceCostDeduct = totalReplaceCostForDamage.multiply(damageDetails.getReplaceCostDeductPercentage()).divide(new BigDecimal("100"));
                        BigDecimal sparePartDepreciation = totalReplaceCostForDamage.multiply(damageDetails.getSparepartDeprectionPercentage()).divide(new BigDecimal("100"));
                        BigDecimal sparePartDiscount = totalReplaceCostForDamage.multiply(damageDetails.getDiscountSparepartPercentage()).divide(new BigDecimal("100"));

                        BigDecimal insuranceAmt = totalReplaceCostForDamage.subtract(replaceCostDeduct).subtract(sparePartDepreciation).subtract(sparePartDiscount);
                        BigDecimal customerAmt = totalReplaceCostForDamage.subtract(insuranceAmt);

                        BigDecimal insuranceVat = insuranceAmt.multiply(vatRate);
                        BigDecimal customerVat = customerAmt.multiply(vatRate);

                        insuranceAmount = insuranceAmount.add(insuranceAmt);
                        customerAmount = customerAmount.add(customerAmt);
                        insuranceVatAmount = insuranceVatAmount.add(insuranceVat);
                        customerVatAmount = customerVatAmount.add(customerVat);

                        // Add the replace cost to totals
                        netAmount = netAmount.add(totalReplaceCostForDamage);
                        totalReplaceCost = totalReplaceCost.add(totalReplaceCostForDamage);
                        totDeduct = totDeduct.add(replaceCostDeduct);
                    }
                }

                // Step 6: Original VAT and amount calculations
                vatAmount = netAmount.multiply(vatRate);
                totAmtWithVat = netAmount.add(vatAmount);
                totAmtAfterDeduct = totAmtWithVat.subtract(totDeduct);

                // Step 7: Create or update a record in total_amount_details
                TotalAmountDetails totalAmountDetails = repository.findByClaimNo(req.getClaimNo())
                        .orElse(new TotalAmountDetails());

                totalAmountDetails.setClaimNo(req.getClaimNo());

                // Step 8: Update the fields from the original calculation
                totalAmountDetails.setNetAmount(netAmount);                  // Total amount (repair or replace)
                totalAmountDetails.setTotamtAftDeduction(totAmtAfterDeduct); // Total amount after deduction
                totalAmountDetails.setVatRatePercent(vatPercent);            // VAT rate (18%)
                totalAmountDetails.setVatRate(vatRate);                      // VAT rate percentage
                totalAmountDetails.setVatAmount(vatAmount);                  // VAT amount
                totalAmountDetails.setTotamtWithVat(totAmtWithVat);          // Total amount with VAT
                totalAmountDetails.setTotReplaceCost(totalReplaceCost);      // Total replace cost
                totalAmountDetails.setTotLabourCost(totalLabourCost);        // Total labor cost

                // Step 9: Update the new fields for insurance and customer amounts
                totalAmountDetails.setInsuranceAmount(insuranceAmount);      // Amount payable by insurance
                totalAmountDetails.setCustomerAmount(customerAmount);        // Amount payable by customer
                totalAmountDetails.setInsuranceVat(insuranceVatAmount);      // VAT amount for insurance
                totalAmountDetails.setCustomerVat(customerVatAmount);        // VAT amount for customer
                totalAmountDetails.setInsAmountWithVat(insuranceAmount.add(insuranceVatAmount)); // Insurance amount with VAT
                totalAmountDetails.setCustAmountWithVat(customerAmount.add(customerVatAmount));  // Customer amount with VAT

                totalAmountDetails.setEntryDate(new Date());
                totalAmountDetails.setCreatedBy("System");
                totalAmountDetails.setStatus("Y");

                // Step 10: Save the updated record to the database
                repository.saveAndFlush(totalAmountDetails);

                response.setErrors(Collections.emptyList());
                response.setMessage("Success");
                response.setResponse(Collections.emptyList());
            } else {
                response.setErrors(error);
                response.setMessage("Failed");
                response.setResponse(Collections.emptyList());
                response.setIsError(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrors(Collections.emptyList());
            response.setMessage("Failed");
            response.setResponse(e.getMessage());
        }
        return response;
    }



    @Override
    public CommonResponse getTotalAmountDetailsByCreatedBy(TotalAmountDetailsRequest request) {
        CommonResponse response = new CommonResponse();
        try {
            List<TotalAmountDetails> detailsList = repository.findByCreatedBy(request.getCreatedBy());
            Map<String, Map<String, String>> data = new HashMap<>();

            for (TotalAmountDetails details : detailsList) {
                Map<String, String> detailMap = new HashMap<>();
                detailMap.put("ClaimNo", details.getClaimNo());
                detailMap.put("NetAmount", details.getNetAmount() != null ? details.getNetAmount().toString() : "");
                detailMap.put("TotamtAftDeduction", details.getTotamtAftDeduction() != null ? details.getTotamtAftDeduction().toString() : "");
                detailMap.put("VatRatePercent", details.getVatRatePercent() != null ? details.getVatRatePercent().toString() : "");
                detailMap.put("VatRate", details.getVatRate() != null ? details.getVatRate().toString() : "");
                detailMap.put("VatAmount", details.getVatAmount() != null ? details.getVatAmount().toString() : "");
                detailMap.put("TotamtWithVat", details.getTotamtWithVat() != null ? details.getTotamtWithVat().toString() : "");
                detailMap.put("CreatedBy", details.getCreatedBy() != null ? details.getCreatedBy() : "");
                detailMap.put("Status", details.getStatus() != null ? details.getStatus() : "");
                detailMap.put("TotReplaceCost", details.getTotReplaceCost() != null ? details.getTotReplaceCost().toString() : "");
                detailMap.put("TotLabourCost", details.getTotLabourCost() != null ? details.getTotLabourCost().toString() : "");

                data.put(details.getClaimNo(), detailMap);
            }

            response.setMessage("Success");
            response.setResponse(data);
            response.setErrors(Collections.emptyList());
            response.setIsError(false);

        } catch (Exception e) {
            response.setErrors(Collections.singletonList(new ErrorList("500", "Exception", e.getMessage())));
            response.setMessage("Failed");
            response.setResponse(Collections.emptyMap());
            response.setIsError(true);
        }
        return response;
    }
 
}
