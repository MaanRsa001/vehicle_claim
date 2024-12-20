package com.maan.veh.claim.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetAllLoginRequest {
	
	@JsonProperty("CompanyId")
	private String companyId;
	
	@JsonProperty("LoginId")
	private String loginId;
}
