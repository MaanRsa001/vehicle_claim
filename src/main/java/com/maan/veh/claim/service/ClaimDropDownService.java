package com.maan.veh.claim.service;

import java.util.List;

import com.maan.veh.claim.response.DropDownRes;

public interface ClaimDropDownService {

	List<DropDownRes> getPoliceStation(String companyId);

	List<DropDownRes> getLossLocation(String companyId);

	List<DropDownRes> getNatureOfLoss(String companyId);

}
