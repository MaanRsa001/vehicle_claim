package com.maan.veh.claim.file;

import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.maan.veh.claim.dto.DynamicLovRequestDto;
import com.maan.veh.claim.dto.FileUploadRequestDto;
import com.maan.veh.claim.dto.LovItem;
import com.maan.veh.claim.response.CommonRes;
import com.maan.veh.claim.response.CommonResponse;

public interface StorageService {

	void init();

	CommonRes store(MultipartFile file,DocumentUploadDetailsReqRes req);

	Stream<Path> loadAll();

	Path load(String filename);

	Resource loadAsResource(String filename);

	void deleteAll();

	CommonRes listUploadedFiles(GetDocListReq req);

	CommonResponse deleteFile(String claimNo, String documentRef, String loginId);

//	CommonRes uploadDocument(String filePath, FileUploadRequestDto dto);

	List<LovItem> getDynamicLov(DynamicLovRequestDto requestDto);

	CommonRes uploadDocument(String filePath, String claimNo, String garageId);



}
