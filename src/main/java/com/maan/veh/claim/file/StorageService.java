package com.maan.veh.claim.file;

import java.nio.file.Path;
import java.util.stream.Stream;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.maan.veh.claim.response.CommonRes;

public interface StorageService {

	void init();

	CommonRes store(MultipartFile file,DocumentUploadDetailsReqRes req);

	Stream<Path> loadAll();

	Path load(String filename);

	Resource loadAsResource(String filename);

	void deleteAll();

	CommonRes listUploadedFiles(GetDocListReq req);

	CommonRes deleteFile(String claimNo, String documentRef);

}
