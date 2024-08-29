package com.maan.veh.claim.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorList {
	
	@JsonProperty("Code")
	private String code;
	
	@JsonProperty("Field")
	private String field;
	
	@JsonProperty("Message")
	private String message;
	
}
