package com.maan.veh.claim.service;

import com.maan.veh.claim.dto.GarageLoginMasterDTO;
import com.maan.veh.claim.request.GetAllLoginRequest;
import com.maan.veh.claim.request.LoginRequest;
import com.maan.veh.claim.response.CommonResponse;

public interface LoginService {

	CommonResponse isValidUser(LoginRequest req);

	CommonResponse logout(LoginRequest req);

	CommonResponse createLogin(GarageLoginMasterDTO req);

	CommonResponse getAllLogin(GetAllLoginRequest req);

	CommonResponse getLoginDetails(GetAllLoginRequest req);

}
