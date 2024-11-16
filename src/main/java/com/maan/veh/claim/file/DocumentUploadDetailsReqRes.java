package com.maan.veh.claim.file;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DocumentUploadDetailsReqRes {

	@JsonProperty("ClaimNo")
	private String claimNo;
	
	@JsonProperty("UserType")
	private String userType;
	
	@JsonProperty("DocumentRef")
	private String documentRef;
	
	@JsonProperty("DocTypeId")
	private String docTypeId;
	
	@JsonProperty("DocDesc")
	private String docDesc;
	
	@JsonProperty("CompanyId")
	private String companyId;
	
	@JsonProperty("FilePathName")
	private String filePathName;
	
	@JsonProperty("FileName")
	private String fileName;

	@JsonProperty("UploadType")
	private String uploadType;

	@JsonProperty("CommonFilePath")
	private String commonFilePath;
	
	@JsonProperty("ErrorRes")
	private String errorRes;
	
	@JsonProperty("UploadedBy")
	private String uploadedBy;
	
	@JsonProperty("ImgUrl")
	private String imgUrl;
	
	@JsonProperty("FileType")
	private String fileType;

}
