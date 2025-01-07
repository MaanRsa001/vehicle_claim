package com.maan.veh.claim.dto;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class GetAllCoreAppCodeResponseDto {
	
	@JsonProperty("hasError")
    private boolean hasError;

    @JsonProperty("status")
    private int status;

    @JsonProperty("data")
    private List<Dataset> data; 

    @JsonProperty("message")
    private String message;

    @Data
    public static class Dataset {
	
	@JsonProperty("partyId")
	private String partyId;
	
	@JsonProperty("partyName")
	private String partyName;
	
	@JsonProperty("category")
	private String category;
	
    }
}
