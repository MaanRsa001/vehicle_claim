package com.maan.veh.claim.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class CountryMasterId implements Serializable{
	
	 private static final long serialVersionUID = 1L;
	 
		private String companyId;
		private String countryId;
		private String countryShortCode;
		private Integer amendId;
	}
