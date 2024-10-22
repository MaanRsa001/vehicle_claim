package com.maan.veh.claim.service;

import java.util.List;

import com.maan.veh.claim.response.DropDownRes;

public interface ClaimDropDownService {

	List<DropDownRes> getPoliceStation();

	List<DropDownRes> getLossLocation();

	List<DropDownRes> getNatureOfLoss();

}
