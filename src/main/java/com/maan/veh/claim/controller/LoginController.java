package com.maan.veh.claim.controller;

import java.util.Collections;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.veh.claim.dto.GarageLoginMasterDTO;
import com.maan.veh.claim.error.Error;
import com.maan.veh.claim.request.ChangePasswordReq;
import com.maan.veh.claim.request.GetAllLoginRequest;
import com.maan.veh.claim.request.GetCoreAppCodeRequest;
import com.maan.veh.claim.request.LoginRequest;
import com.maan.veh.claim.response.CommonLoginRes;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.ErrorList;
import com.maan.veh.claim.response.SuccessRes;
import com.maan.veh.claim.service.LoginService;
import com.maan.veh.claim.serviceimpl.InputValidationUtil;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/login")
public class LoginController {

	@Autowired
	LoginService service;
	
	@Autowired
	private InputValidationUtil validation;
	
	
	@PostMapping("/isValidUser")
	public CommonResponse isValidUser(@RequestBody LoginRequest req,HttpServletRequest http) {
		CommonResponse response = new CommonResponse();
		List<ErrorList> errors = validation.isValidUser(req);
		if(errors.size()>0){
			//response.setErrors(errors);
			response.setIsError(true);
			//response.setMessage("User Not Found");
			response.setErrorMessage(errors);
			response.setResponse(null);
			
			return response;
		}else {
			http.getSession().removeAttribute(req.getLoginId());
			return service.isValidUser(req);		
		}
				
	}
	
	@PostMapping("/api/changepassword")
	public ResponseEntity<CommonLoginRes> getChangePassword(@RequestBody ChangePasswordReq req) throws Exception {

		CommonLoginRes data = new CommonLoginRes();
		
		// Validation
		List<Error> valid = validation.LoginChangePasswordValidation(req);
		if(valid!= null && valid.size()!=0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(valid);
			data.setMessage("Failed");
			
			return new ResponseEntity<CommonLoginRes>(data, HttpStatus.OK);
		}
		else {
			// Save 
			String res = service.LoginChangePassword(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			
			if( res !=null && StringUtils.isNotBlank(res)  ) {
				return new ResponseEntity<CommonLoginRes>(data, HttpStatus.CREATED);
			}
			else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
	
	}
	
	@PostMapping("/api/forgotpassword")
	public  ResponseEntity<CommonLoginRes> getForgotPwd(@RequestBody ChangePasswordReq req){
		CommonLoginRes data = new CommonLoginRes(); 
		List<Error> valid = validation.forgetPwdValidation(req);
		if(valid!= null && valid.size()!=0) {
			data.setCommonResponse(null);
			data.setIsError(true);
			data.setErrorMessage(valid);
			data.setMessage("Failed");
			
			return new ResponseEntity<CommonLoginRes>(data, HttpStatus.OK);
		}
		else {
			// Save 
			SuccessRes res = service.LoginForgetPassword(req);
			data.setCommonResponse(res);
			data.setIsError(false);
			data.setErrorMessage(Collections.emptyList());
			data.setMessage("Success");
			
			if( res !=null  ) {
				return new ResponseEntity<CommonLoginRes>(data, HttpStatus.CREATED);
			}
			else {
				return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
			}
		}
		
	}
	
	@PostMapping("/logout")
	public CommonResponse logoutUser(@RequestBody LoginRequest req) {
			return service.logout(req);		
				
	}
	
	@PostMapping("/create/garage")
	public CommonResponse createGarageLogin(@RequestBody GarageLoginMasterDTO req) {
			return service.createLogin(req);		
				
	}
	
	@PostMapping("/create/surveyor")
	public CommonResponse createSurveyorLogin(@RequestBody GarageLoginMasterDTO req) {
			return service.createLogin(req);		
				
	}
	
	@PostMapping("/create/dealer")
	public CommonResponse createDealerLogin(@RequestBody GarageLoginMasterDTO req) {
			return service.createLogin(req);		
				
	}
	
	@PostMapping("/create/admin")
	public CommonResponse createAdminLogin(@RequestBody GarageLoginMasterDTO req) {
			return service.createLogin(req);		
				
	}
	
	@PostMapping("/getAll/login")
	public CommonResponse getAllLogin(@RequestBody GetAllLoginRequest req) {
			return service.getAllLogin(req);		
				
	}
	
	@PostMapping("/getLoginDetails")
	public CommonResponse getLoginDetails(@RequestBody GetAllLoginRequest req) {
			return service.getLoginDetails(req);		
				
	}
	
	@PostMapping("/getCoreAppCode")
	public CommonResponse getCoreAppCode(@RequestBody GetCoreAppCodeRequest req) {
			return service.getCoreAppCode(req);		
				
	}
	
}
