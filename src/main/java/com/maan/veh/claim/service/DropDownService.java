package com.maan.veh.claim.service;

import java.util.List;

import com.maan.veh.claim.response.DropDownRes;

public interface DropDownService {

	List<DropDownRes> getDamageDirection(String companyId);

	List<DropDownRes> getDamageDropdown(String companyId);

	List<DropDownRes> getWorkOrderType(String companyId);

	List<DropDownRes> getSettlementType(String companyId);

	List<DropDownRes> getLossType(String companyId);

	List<DropDownRes> getDamageType(String companyId);

	List<DropDownRes> getLosstype(String companyId);

	List<DropDownRes> getbodyPart(String companyId);

	List<DropDownRes> getVatPercentage(String companyId);

	List<DropDownRes> getAccountForSettlement(String companyId);

	List<DropDownRes> getRepairReplace(String companyId);

	List<DropDownRes> getGarageLoginId(String companyId);

	List<DropDownRes> getStatus(String companyId);

	List<DropDownRes> getDealerLoginId(String companyId);

	List<DropDownRes> getRepairType(String companyId);

	List<DropDownRes> geDocumentType(String companyId);

	List<DropDownRes> getBranch(String companyId);

	List<DropDownRes> getCompany(String companyId);

	List<DropDownRes> getCountry(String companyIds);
	
	List<DropDownRes> getCity(String companyIds);

	List<DropDownRes> getUserType(String companyId);
	
	String getItemCodeByItemValue(String itemValue,String itemType);

	String getbodyPartCodeByValue(String value);

	List<DropDownRes> getLossLocation(String companyId);
}
