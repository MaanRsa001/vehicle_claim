package com.maan.veh.claim.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.maan.veh.claim.request.LoginRequest;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.ErrorList;
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
			response.setErrors(errors);
			response.setIsError(true);
			response.setMessage("User Not Found");
			response.setResponse(Collections.EMPTY_LIST);
			return response;
		}else {
			http.getSession().removeAttribute(req.getLoginId());
			return service.isValidUser(req);		
		}
				
	}
	@PostMapping("/logout")
	public CommonResponse logoutUser(@RequestBody LoginRequest req) {
			return service.logout(req);		
				
	}
	
}
