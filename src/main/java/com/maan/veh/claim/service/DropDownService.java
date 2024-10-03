package com.maan.veh.claim.service;

import java.util.List;

import com.maan.veh.claim.response.DropDownRes;

public interface DropDownService {

	List<DropDownRes> getDamageDirection();

	List<DropDownRes> getDamageDropdown();

	List<DropDownRes> getWorkOrderType();

	List<DropDownRes> getSettlementType();

	List<DropDownRes> getLossType();

	List<DropDownRes> getDamageType();

	List<DropDownRes> getLosstype();

	List<DropDownRes> getbodyPart();

	List<DropDownRes> getVatPercentage();

	List<DropDownRes> getAccountForSettlement();

	List<DropDownRes> getRepairReplace();

	List<DropDownRes> getGarageLoginId();

	List<DropDownRes> getStatus();

	List<DropDownRes> getDealerLoginId();
}
