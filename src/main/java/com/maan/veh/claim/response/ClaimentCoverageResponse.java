package com.maan.veh.claim.response;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimentCoverageResponse {

	@JsonProperty("HasError")
	private boolean HasError;

	@JsonProperty("Status")
	private int Status;

	@JsonProperty("Data")
	private Object Data; // Replace `Object` with a specific type if needed.

	@JsonProperty("Dataset")
	private Dataset Dataset;

	@JsonProperty("Message")
	private String Message;

	@Data
	public static class Dataset {

		@JsonProperty("ClaimantList")
		private List<String> ClaimantList;

		@JsonProperty("CoveragesList")
		private List<String> CoveragesList;
	}
}
