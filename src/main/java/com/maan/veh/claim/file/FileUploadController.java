package com.maan.veh.claim.file;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maan.veh.claim.response.CommonRes;

@RestController
@RequestMapping("/document")
public class FileUploadController {

	private final StorageService storageService;

	@Autowired
	public FileUploadController(StorageService storageService) {
		this.storageService = storageService;
	}

	@PostMapping("/getByClaim")
	public ResponseEntity<CommonRes> listUploadedFiles(@RequestBody GetDocListReq req) {
		CommonRes res= new CommonRes();
		res = storageService.listUploadedFiles(req);

		return ResponseEntity.ok(res);
	}
	
	@PostMapping("/delete")
	public ResponseEntity<CommonRes> deleteFile(@RequestBody GetDocListReq req) {
		CommonRes res= new CommonRes();
		res = storageService.deleteFile(req.getClaimNo(),req.getDocumentRef());

		return ResponseEntity.ok(res);
	}

	@GetMapping("/download/files/{filename:.+}")
	@ResponseBody
	public ResponseEntity<Resource> serveFile(@PathVariable String filename) {

		Resource file = storageService.loadAsResource(filename);

		if (file == null)
			return ResponseEntity.notFound().build();

		return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
				"attachment; filename=\"" + file.getFilename() + "\"").body(file);
	}

	@PostMapping("/upload")
	public ResponseEntity<CommonRes> handleFileUpload(@RequestParam("File") MultipartFile file,@RequestParam("Req") String jsonString) {

	    	DocumentUploadDetailsReqRes req;
			try {
				req = new ObjectMapper().readValue(jsonString, DocumentUploadDetailsReqRes.class);
				
				 // Store the file
		    	CommonRes res = storageService.store(file,req);

		        return ResponseEntity.ok(res);
		        
			} catch (JsonMappingException e) {
				e.printStackTrace();
			} catch (JsonProcessingException e) {
				e.printStackTrace();
			}
			return ResponseEntity.badRequest().body(null);
	}

	@ExceptionHandler(StorageFileNotFoundException.class)
	public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
		return ResponseEntity.notFound().build();
	}

}
