package com.maan.veh.claim.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.maan.veh.claim.error.Error;
import lombok.Data;

@Data
public class CommonRes {

	@JsonProperty("Message")
	private String message;

	@JsonProperty("IsError")	
	private Boolean isError;
	
	@JsonProperty("ErrorMessage")
	private List<Error> errorMessage;

	//Dynamic
	@JsonProperty("Result")
	private Object commonResponse;
	
	@JsonProperty("ErroCode")
	private int erroCode;

	

/*	@JsonProperty("AdditionalData")
	private DefaultAllResponse defaultValue; */
}
