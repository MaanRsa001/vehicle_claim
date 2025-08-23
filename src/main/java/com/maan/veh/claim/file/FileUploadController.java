package com.maan.veh.claim.file;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maan.veh.claim.dto.DynamicLovRequestDto;
import com.maan.veh.claim.dto.LovItem;
import com.maan.veh.claim.repository.InsuredVehicleInfoRepository;
import com.maan.veh.claim.response.CommonRes;
import com.maan.veh.claim.response.CommonResponse;

@RestController
@RequestMapping("/document")
public class FileUploadController {

	private final StorageService storageService;
	
	@Autowired
	private InsuredVehicleInfoRepository repository;

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
	public ResponseEntity<CommonResponse> deleteFile(@RequestBody GetDocListReq req) {
		CommonResponse res= new CommonResponse();
		res = storageService.deleteFile(req.getClaimNo(),req.getDocumentRef(),req.getLoginId());

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
	

	@PostMapping(value = "/uploadss", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<CommonRes> uploadDocument(
	        @RequestPart("file") MultipartFile file,
	        @RequestParam("claimNo") String claimNo,
	        @RequestParam("garageId") String garageId) {

	    try {
	        // Save file temporarily
	        String filePath = System.getProperty("java.io.tmpdir") + "/" + file.getOriginalFilename();
	        file.transferTo(new File(filePath));

	        // Delegate to service
	        CommonRes apiResponse = storageService.uploadDocument(filePath, claimNo, garageId);
	        return ResponseEntity.ok(apiResponse);

	    } catch (Exception e) {
	        CommonRes response = new CommonRes();
	        response.setMessage("Document upload failed: " + e.getMessage());
	        response.setIsError(true);
	        response.setErroCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
	    }
	}



    @PostMapping("/get-values")
    public ResponseEntity<List<LovItem>> getValues(
             @RequestBody DynamicLovRequestDto requestDto) throws Exception  {

    	List<LovItem> response = storageService.getDynamicLov(requestDto);
        return ResponseEntity.ok(response);
    }


}
