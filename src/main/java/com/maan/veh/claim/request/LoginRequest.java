package com.maan.veh.claim.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class LoginRequest {
	
	@JsonProperty("LoginId")
	private String loginId;
	
	@JsonProperty("Password")
	private String password;
	
}
