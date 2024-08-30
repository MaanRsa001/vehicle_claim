package com.maan.veh.claim.serviceimpl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.hibernate.service.spi.ServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.veh.claim.entity.DamageSectionDetails;
import com.maan.veh.claim.repository.DamageSectionDetailsRepository;
import com.maan.veh.claim.request.DamageSectionDetailsRequest;
import com.maan.veh.claim.response.DamageSectionDetailsResponse;
import com.maan.veh.claim.service.DamageSectionDetailsService;

@Service
public class DamageSectionDetailsServiceImpl implements DamageSectionDetailsService {

	@Autowired
	private DamageSectionDetailsRepository repository;

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
			response.setDamageDictDesc(Optional.ofNullable(details.getDamageDictDesc()).orElse(""));
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
}
