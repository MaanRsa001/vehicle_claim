package com.maan.veh.claim.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ProductDropDownRes {

	@JsonProperty("ProductId")
	private String productId ;
	
	@JsonProperty("OldProductName")
	private String oldProductName ;
	
	@JsonProperty("ProductName")
	private String newProductName;
	

	@JsonProperty("ProductIconId")
	private String productIconId ;
	
	@JsonProperty("ProductIconName")
	private String productIconName;
	
	@JsonProperty("PackageYn")
	private String packageYn;
	
	@JsonProperty("DisplayOrder")
	private Integer displayOrder;
	
}
