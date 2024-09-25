package com.maan.veh.claim.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@ToString
@XmlRootElement
@JsonDeserialize
@AllArgsConstructor
@NoArgsConstructor
public class DropDownRes {

	@JsonProperty("Code")
	private String code;
	@JsonProperty("CodeDesc")
	private String codeDesc;
	
}
