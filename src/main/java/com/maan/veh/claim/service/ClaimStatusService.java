package com.maan.veh.claim.service;

import java.util.List;

import com.maan.veh.claim.response.DropDownRes;

public interface ClaimStatusService {

	List<DropDownRes> getGarageStatus(String currentStatus);

	List<DropDownRes> getSurveyorStatus(String currentStatus);

	List<DropDownRes> getDealerStatus(String currentStatus);

	List<DropDownRes> getGridStatus(String usertype,String companyId,String flowId);

}
