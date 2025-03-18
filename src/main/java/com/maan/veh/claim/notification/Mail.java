package com.maan.veh.claim.notification;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class Mail implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private List<String> tomails;
	private List<String> ccmails;
	private List<String> files;
	private String subject;
	private String mailbody;
	private String mailbodyContenttype;
	private Long notifNo;
	private JobCredentials master;
}
