package com.maan.veh.claim.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResponse {
	
	@JsonProperty("Message")
	private String message;
	
	@JsonProperty("Response")
	private Object response;
	
	@JsonProperty("Errors")
	private Object errors;
	
	@JsonProperty("IsError")
	private Boolean isError;
	
	@JsonProperty("ErrorMessage")
	private List<ErrorList> errorMessage;
	
	public CommonResponse(String message, Object response, Boolean isError) {
        this.message = message;
        this.response = response;
        this.isError = isError;
    }

}
