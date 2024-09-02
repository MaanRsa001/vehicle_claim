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
import com.maan.veh.claim.repository.DamageSectionDetailsRepository;
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
	        	
	            for (DamageSectionDetailsSaveReq req:reqList) {
	            	
	            	DamageSectionDetails details = new DamageSectionDetails();
	            	
					details.setClaimNo(req.getClaimNo());
					details.setDamageSno(damageSno);
					details.setDamageDirection(req.getDamageDirection());
					details.setDamagePart(req.getDamagePart());
					details.setRepairReplace(req.getRepairReplace());
					details.setNoOfParts(Integer.valueOf(req.getNoOfParts()));
					details.setGaragePrice(new BigDecimal(req.getGaragePrice()));
					details.setDealerPrice(new BigDecimal(req.getDealerPrice()));
					details.setGarageLoginId(req.getGarageLoginId());
					details.setDealerLoginId(req.getDealerLoginId());
					details.setSurveyorId(Integer.valueOf(req.getSurveyorId()));
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
				repository.saveAll(saveList);

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

}
