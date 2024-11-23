package com.maan.veh.claim.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordReq {

	@JsonProperty("OldPassword")
	private String oldpassword;
	
	@JsonProperty("LoginId")
	private String loginId;

	@JsonProperty("NewPassword")
	private String newPassword;

	@JsonProperty("Type")
	private String type;
	
	@JsonProperty("EmailId")
	private String emailId;
}
