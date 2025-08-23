package com.maan.veh.claim.file;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maan.veh.claim.dto.DynamicLovRequestDto;
import com.maan.veh.claim.dto.FileUploadRequestDto;
import com.maan.veh.claim.dto.LovItem;
import com.maan.veh.claim.entity.ApiTransactionLog;
import com.maan.veh.claim.entity.InsuredVehicleInfo;
import com.maan.veh.claim.entity.VcDocumentUploadDetails;
import com.maan.veh.claim.error.Error;
import com.maan.veh.claim.repository.ApiTransactionLogRepository;
import com.maan.veh.claim.repository.InsuredVehicleInfoRepository;
import com.maan.veh.claim.repository.VcDocumentUploadDetailsRepository;
import com.maan.veh.claim.response.CommonRes;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.ErrorList;
import com.maan.veh.claim.serviceimpl.InputValidationUtil;

@Service
public class FileSystemStorageService implements StorageService {
	
	@Value("${common.path}")
	private Path rootLocation;
	
	@Autowired
	private InsuredVehicleInfoRepository repository;
	
	  @Autowired
	    private RestTemplate restTemplate;

	    @Autowired
	    private ObjectMapper objectMapper;
	    

	    @Autowired
	    private ApiTransactionLogRepository apiTransactionLogRepo;

	    
	    private static final Logger logger = LoggerFactory.getLogger(FileSystemStorageService.class);
	    
	@Autowired
	private VcDocumentUploadDetailsRepository documentUploadDetailsRepo;
	
	@Autowired
    private InputValidationUtil validation;
	
	   @Value("${external.api.url.authenticate}")
	    private String externalApiUrlAuthenticate;

	    @Value("${auth.username}")
	    private String apiusername;

	    @Value("${auth.password}")
	    private String apipassword;
	
	    @Value("${external.api.url.externalApiUrlUploadDoc}")
	    private String externalApiUrlUploadDoc;
	    
	    @Value("${external.api.url.externalApiUrlLov}")
	    private String externalApiUrlLov;
	    
//	    String url = "https://140.245.210.145/btapi/getDynamicLovValues";
	    
//	  private final String externalApiUrlUploadDoc = "https://140.245.210.145/btapi/attachment/uploadFileDocuments";

	@Override
	public CommonRes store(MultipartFile file, DocumentUploadDetailsReqRes req) {
	    CommonRes response = new CommonRes();  // Initialize the response object
	    
	   // Validate requestPayload
        List<ErrorList> validationErrors = validation.validateDocumentUploadDetails(req);
        if (!validationErrors.isEmpty()) {
            response.setErrorMessage(validationErrors);
            response.setMessage("Validation failed");
            response.setCommonResponse(Collections.emptyMap());
            response.setIsError(true);
            return response;
        }
	    
	    try {
	        if (file.isEmpty()) {
	            throw new StorageException("Failed to store empty file.");
	        }

	        Path destinationFile = this.rootLocation.resolve(
	            Paths.get(file.getOriginalFilename()))
	            .normalize().toAbsolutePath();

	        if (!destinationFile.getParent().equals(this.rootLocation.toAbsolutePath())) {
	            // Security check
	            throw new StorageException("Cannot store file outside current directory.");
	        }

	        try (InputStream inputStream = file.getInputStream()) {
	            Files.copy(inputStream, destinationFile, StandardCopyOption.REPLACE_EXISTING);
	        }
	        
	        // Get the file type (extension)
	        String originalFileName = file.getOriginalFilename();
	        String fileType = "";
	        if (originalFileName != null && originalFileName.contains(".")) {
	            fileType = originalFileName.substring(originalFileName.lastIndexOf(".") + 1);
	        }
	        
	        List<VcDocumentUploadDetails> all = documentUploadDetailsRepo.findAllByOrderByDocumentRefDesc();
	        Long id = 0L;
	        if(all != null && all.size()>0) {
	        	id = all.get(0).getDocumentRef() + 1;
	        }

	        // Create and populate VcDocumentUploadDetails entity with values from the request
	        VcDocumentUploadDetails data = new VcDocumentUploadDetails();
	        data.setDocumentRef(id);
	        data.setClaimNo(req.getClaimNo());
	        data.setCompanyId(Integer.parseInt(req.getCompanyId()));
	        data.setDocTypeId(req.getDocTypeId() != null ? req.getDocTypeId() : null);
	        data.setDocName(req.getDocDesc());
	        data.setFilePathName(destinationFile.toString());
	        data.setUploadedTime(new Date());
	        data.setDescription(req.getDocDesc());
	        data.setFileName(file.getOriginalFilename());
	        data.setUploadType(req.getUploadType());
	        data.setCommonFilePath(req.getCommonFilePath());
	        data.setErrorRes(req.getErrorRes());
	        data.setUserType(req.getUserType());
	        data.setUploadedBy(req.getUploadedBy());
	        data.setRemarks("Uploaded successfully");
	        data.setFileType(fileType);

	        documentUploadDetailsRepo.save(data);

	        // Set success response details
	        response.setMessage("Document uploaded successfully: " + file.getOriginalFilename());
	        response.setIsError(false);
	        response.setCommonResponse(data);  // Optional: include uploaded document details in response
	        response.setErroCode(0);

	    } catch (Exception e) {
	        // Log and set error response details
	        e.printStackTrace();
	        
	        response.setMessage("Failed to store file: " + e.getMessage());
	        response.setIsError(true);

	        // Optional: add detailed error message information
	        Error error = new Error();
	        error.setCode("500");  // Example error code
	        error.setMessage(""+e.getMessage());
	        //response.setErrorMessage(List.of(error));  // Include a list with the error details

	        response.setErroCode(500);  // Standard error code for failed operation
	    }

	    return response;
	}


	@Override
	public Stream<Path> loadAll() {
		try {
			return Files.walk(this.rootLocation, 1)
				.filter(path -> !path.equals(this.rootLocation))
				.map(this.rootLocation::relativize);
		}
		catch (IOException e) {
			throw new StorageException("Failed to read stored files", e);
		}

	}

	@Override
	public Path load(String filename) {
		return rootLocation.resolve(filename);
	}

	@Override
	public Resource loadAsResource(String filename) {
		try {
			Path file = load(filename);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				return resource;
			}
			else {
				throw new StorageFileNotFoundException(
						"Could not read file: " + filename);

			}
		}
		catch (MalformedURLException e) {
			throw new StorageFileNotFoundException("Could not read file: " + filename, e);
		}
	}

	@Override
	public void deleteAll() {
		FileSystemUtils.deleteRecursively(rootLocation.toFile());
	}

	@Override
	public void init() {
		try {
			Files.createDirectories(rootLocation);
		}
		catch (IOException e) {
			throw new StorageException("Could not initialize storage", e);
		}
	}

	@Override
	public CommonRes listUploadedFiles(GetDocListReq req) {
	    CommonRes response = new CommonRes();  // Initialize the response object

	    try {
	        List<DocumentUploadDetailsReqRes> resList = new ArrayList<>();

	        // Fetch documents by claim number
	        List<VcDocumentUploadDetails> docList = documentUploadDetailsRepo.findByClaimNo(req.getClaimNo());
	        
	        if("Dealer".equalsIgnoreCase(req.getUserType())) {
	        	docList = docList.stream().filter(doc -> doc.getUploadedBy().equalsIgnoreCase(req.getLoginId()) 
                        || "Surveyor".equalsIgnoreCase(doc.getUserType()))
            .collect(Collectors.toList());
	        }else{
	        	docList = docList.stream().filter(doc -> doc.getUploadedBy().equalsIgnoreCase(req.getGarageLoginId()) 
                        || "Surveyor".equalsIgnoreCase(doc.getUserType()))
            .collect(Collectors.toList());

	        }

	        // Map each VcDocumentUploadDetails to DocumentUploadDetailsReqRes
	        for (VcDocumentUploadDetails doc : docList) {
	            DocumentUploadDetailsReqRes docRes = DocumentUploadDetailsReqRes.builder()
	                .claimNo(doc.getClaimNo())
	                .documentRef(String.valueOf(doc.getDocumentRef()))
	                .docTypeId(doc.getDocTypeId() != null ? String.valueOf(doc.getDocTypeId()) : null)
	                .docDesc(doc.getDescription())
	                .companyId(String.valueOf(doc.getCompanyId()))
	                .filePathName(doc.getFilePathName())
	                .fileName(doc.getFileName())
	                .uploadType(doc.getUploadType())
	                .commonFilePath(doc.getCommonFilePath())
	                .errorRes(doc.getErrorRes())
	                .imgUrl(getImageUrl(doc.getFilePathName()))
	                .uploadedBy(doc.getUploadedBy())
	                .fileType(doc.getFileType())
	                .build();

	            resList.add(docRes);
	        }

	        // Set success response details
	        response.setMessage("Data retrieved successfully");
	        response.setIsError(false);
	        response.setCommonResponse(resList);  
	        response.setErroCode(0);

	    } catch (Exception e) {
	        // Log and set error response details
	        e.printStackTrace();

	        response.setMessage("Failed to retrieve files: " + e.getMessage());
	        response.setIsError(true);

	        // Add error details to errorMessage list
	        Error error = new Error();
	        error.setCode("500");  // Example error code
	        error.setMessage(e.getMessage());
	        //response.setErrorMessage(List.of(error));  // Include a list with the error details

	        response.setErroCode(500);  // Standard error code for failed operation
	    }

	    return response;
	}
	
	public String getImageUrl(String path){

		File file = new File(path);
		String doc;
		if (StringUtils.isNotBlank(path) && new File(path).exists()) {
			byte[] array;
			try {
				array = FileUtils.readFileToByteArray(new File(path));
				

				MultipartFile baseM = new BASE64DecodedMultipartFile(array, file.getName());
				String contenttype = baseM.getContentType();
				String prefix = "data:" + contenttype + ";base64,";

				
				String imgurlen = Base64Utils.encodeToString(array);
				doc = prefix + imgurlen;
				return doc;
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		} else {
			System.out.println("File Is Not Found");
		}
		return null;
	}
	
	@Override
	public CommonResponse deleteFile(String claimNo, String documentRef, String loginId) {
		CommonResponse response = new CommonResponse();  // Initialize the response object
		
		List<ErrorList> errors = new ArrayList<>();

	    try {
	        // Find document by claimNo and documentRef
	        Optional<VcDocumentUploadDetails> documentOpt = documentUploadDetailsRepo.findByClaimNoAndDocumentRef(claimNo, Long.valueOf(documentRef));

	        if (documentOpt.isPresent()) {
	            VcDocumentUploadDetails document = documentOpt.get();

	            // Get the file path from the database record
	            String filePath = document.getFilePathName();
	            File file = new File(filePath);

	            // Delete the file from storage
	            if (file.exists() && file.delete() && document.getUploadedBy().equalsIgnoreCase(loginId)) {
	                // Delete the record from the database
	                documentUploadDetailsRepo.delete(document);

	                // Set success response details
	                response.setMessage("File and record deleted successfully for documentRef: " + documentRef);
	                response.setIsError(false);
	                response.setErrors(0);
	            } else {
	                // Handle file not found or deletion failure
	                response.setMessage("File could not be found or deleted for documentRef: " + documentRef);
	                response.setIsError(true);
	                errors.add(new ErrorList("100", "Delete", "File could not be found or deleted for documentRef: " + documentRef));
	                response.setErrors(errors);  // Not found error code
	            }
	        } else {
	            // Handle case where document was not found in the database
	            response.setMessage("Document not found for claimNo: " + claimNo + ", documentRef: " + documentRef);
	            response.setIsError(true);
	            errors.add(new ErrorList("100", "Document", "File could not be found "));
	            response.setErrors(errors);  // Not found error code
	        }
	    } catch (Exception e) {
	        // Log and set error response details
	        e.printStackTrace();

	        response.setMessage("Failed to delete file: " + e.getMessage());
	        response.setIsError(true);

	        // Optional: add detailed error message information
	        Error error = new Error();
	        error.setCode("500");  // Example error code
	        error.setMessage(e.getMessage());
	        errors.add(new ErrorList("100", "Delete", "File could not be found or deleted for documentRef: " + documentRef));
	        response.setErrors(errors);  // Standard error code for failed operation
	    }

	    return response;
	}


	@Override
	public CommonRes uploadDocument(String filePath, String claimNo, String garageId) {
	    ApiTransactionLog log = new ApiTransactionLog();
	    log.setSno(apiTransactionLogRepo.findMaxSno() + 1);
	    log.setRequestTime(LocalDateTime.now());
	    log.setEntryDate(new Date());
	    log.setEndpoint(externalApiUrlUploadDoc);

	    CommonRes response = new CommonRes();

	    try {
	    	
	    	 Optional<InsuredVehicleInfo> optionalVeh = repository.findByClaimNoAndGarageId(claimNo, garageId);
	    	  InsuredVehicleInfo insuredVeh = optionalVeh.orElseThrow(() -> 
	          new RuntimeException("Insured vehicle not found"));


	        MultipartFile file = getImageFile(filePath);
	        if (file == null || file.isEmpty()) {	
	            throw new RuntimeException("File not found or empty.");
	        }
	        FileUploadRequestDto dto = buildFileUploadDto(insuredVeh,file);
	        
	        // Authenticate
	        String jwtToken = authenticateUserCall();


	        // Prepare request body JSON
	        String requestBodyJson = objectMapper.writeValueAsString(dto);
	        log.setRequest(requestBodyJson);

	        System.out.println("Output:"+requestBodyJson);
	        // Multipart form data
	        LinkedMultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
	        body.add("file", new MultipartInputStreamFileResource(file.getInputStream(), file.getOriginalFilename()));

	        HttpHeaders jsonHeaders = new HttpHeaders();
	        jsonHeaders.set("Authorization", "Bearer " + jwtToken);
	        jsonHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
	        HttpEntity<String> jsonPart = new HttpEntity<>(requestBodyJson, jsonHeaders);
	        
	        body.add("fileReqest", jsonPart);

	        HttpEntity<LinkedMultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, jsonHeaders	);


	        // Call external API
	        ResponseEntity<String> apiResponse = restTemplate.exchange(
	        		externalApiUrlUploadDoc, HttpMethod.POST, requestEntity, String.class);

	        System.out.println("Output:"+apiResponse.getBody());
	        log.setResponse(apiResponse.getBody());
	        log.setStatus("SUCCESS");
	        
	     // Save document details to local system
	        VcDocumentUploadDetails docDetails = new VcDocumentUploadDetails();
	        docDetails.setClaimNo(claimNo);
	        docDetails.setCompanyId(insuredVeh.getCompanyId()); // Ensure this exists in InsuredVehicleInfo

	        // Generate next documentRef
	        Long nextDocRef = documentUploadDetailsRepo.findMaxDocumentRef(claimNo, insuredVeh.getCompanyId()) + 1;
	        docDetails.setDocumentRef(nextDocRef);

	        docDetails.setDocName(file.getOriginalFilename());
	        docDetails.setFileName(file.getOriginalFilename());
	        docDetails.setFileType(FilenameUtils.getExtension(file.getOriginalFilename()));
//	        docDetails.setDocTypeId();
	        docDetails.setFilePathName(filePath);
	        docDetails.setCommonFilePath(filePath); // Use actual common path if needed
	        docDetails.setUploadedTime(new Date());
	        docDetails.setUploadedBy("PORTAL");
	        docDetails.setUserType("PORTAL_USER");
	        docDetails.setUploadType("CI");
	        docDetails.setRemarks("Uploaded to external system");
	        docDetails.setDescription("Document uploaded via API");

	        documentUploadDetailsRepo.save(docDetails);

	        response.setIsError(false);
	        response.setMessage("SUCCESS");
	        response.setCommonResponse(apiResponse.getBody());

	    } catch (Exception e) {
	        log.setStatus("FAILURE");
	        log.setErrorMessage(e.getMessage());

	        response.setIsError(true);
	        response.setMessage("FAILURE");
	        response.setCommonResponse(e.getMessage());

	        logger.error("Error while uploading document", e);
	    } finally {
	        log.setResponseTime(LocalDateTime.now());
	        apiTransactionLogRepo.save(log);
	        logger.info(log.getEndpoint() + " ==> " + log);
	    }
	    return response;
	}


	private FileUploadRequestDto buildFileUploadDto(InsuredVehicleInfo insuredVeh, MultipartFile file) throws IOException {
	    FileUploadRequestDto dto = new FileUploadRequestDto();

	    dto.setSgsId(Integer.valueOf(insuredVeh.getFnolSgsId()));
	    dto.setAmndVersionId("0");
	    dto.setProductId(insuredVeh.getProductId());
	    dto.setTransactionType("CLM");
	    dto.setPartyId(insuredVeh.getClcpId());
	    dto.setPartyName(insuredVeh.getInsuredName());
	    dto.setPartyType("");
	    dto.setCreatedBy("BASE>.U");
	    dto.setDocumentTransactionType("CI");
	    dto.setAttachmentRefNo(null);

	    FileUploadRequestDto.AttachmentDetails attachmentDetails = new FileUploadRequestDto.AttachmentDetails();
	    List<FileUploadRequestDto.DocumentDetails> documentDetailsList = new ArrayList<>();

	    FileUploadRequestDto.DocumentDetails documentDetails = new FileUploadRequestDto.DocumentDetails();
	    documentDetails.setDocumentId(documentDetails.getDocumentId()); 
	    documentDetails.setDocumentName(file.getOriginalFilename());
	    documentDetails.setDocumentType(FilenameUtils.getExtension(file.getOriginalFilename()));
	    String base64Data = Base64.getEncoder().encodeToString(file.getBytes());
	    documentDetails.setDocumentData("");
	    documentDetails.setDocumentFormat(file.getContentType());
	    documentDetails.setDocumentURL(null);
	    documentDetails.setDocumentRefNo("");

	    documentDetailsList.add(documentDetails);
	    attachmentDetails.setDocumentDetails(documentDetailsList);
	    dto.setAttachmentDetails(attachmentDetails);

	    return dto;
	}


	    public String authenticateUserCall() {
	        ApiTransactionLog log = new ApiTransactionLog();
	        log.setRequestTime(LocalDateTime.now());
	        log.setEntryDate(new Date());
	        log.setEndpoint(externalApiUrlAuthenticate);

	        try {
	            Map<String, String> requestMap = Map.of(
	                    "username", apiusername,
	                    "password", apipassword
	            );

	            String requestBody = objectMapper.writeValueAsString(requestMap);
	            log.setRequest(requestBody);

	            HttpHeaders headers = new HttpHeaders();
	            headers.setContentType(MediaType.APPLICATION_JSON);

	            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

	            // SSL bypass
	            TrustManager[] trustAllCerts = new TrustManager[]{
	                new X509TrustManager() {
	                    public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
	                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
	                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {}
	                }
	            };
	            SSLContext sc = SSLContext.getInstance("SSL");
	            sc.init(null, trustAllCerts, new java.security.SecureRandom());
	            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

	            ResponseEntity<String> apiResponse = restTemplate.postForEntity(
	                    log.getEndpoint(), entity, String.class);

	            log.setResponse(apiResponse.getBody());
	            log.setStatus("SUCCESS");

	            Map<String, String> responseMap = objectMapper.readValue(apiResponse.getBody(), Map.class);
	            return responseMap.get("jwt");

	        } catch (Exception e) {
	            log.setStatus("FAILURE");
	            log.setErrorMessage(e.getMessage());
	            logger.error("Authentication failed", e);
	        } finally {
	            log.setResponseTime(LocalDateTime.now());
	            // apiTransactionLogRepo.save(log); // optional
	            logger.info(externalApiUrlAuthenticate + " ==> " + log);
	        }
	        return null;
	    }
	    
	public MultipartFile getImageFile(String path) {
		
		try {
			File file = new File(path);
			if (StringUtils.isNotBlank(path) && file.exists()) {
				byte[] array = FileUtils.readFileToByteArray(file);
				return new BASE64DecodedMultipartFile(array, file.getName());
			} else {
				System.out.println("File Not Found");
				return null;
			}
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public class MultipartInputStreamFileResource extends InputStreamResource {
	    private final String filename;

	    public MultipartInputStreamFileResource(InputStream inputStream, String filename) {
	        super(inputStream);
	        this.filename = filename;
	    }

	    @Override
	    public String getFilename() {
	        return this.filename;
	    }

	    @Override
	    public long contentLength() throws IOException {
	        return -1; // we don’t know the exact length
	    }
	}



	@Override
	public List<LovItem> getDynamicLov(DynamicLovRequestDto requestDto) {
	    ApiTransactionLog log = new ApiTransactionLog();
	    log.setSno(apiTransactionLogRepo.findMaxSno() + 1);
	    log.setRequestTime(LocalDateTime.now());
	    log.setEntryDate(new Date());

	    List<LovItem> lovItems = Collections.emptyList();

	    try {
	        
	        log.setEndpoint(externalApiUrlLov);
	        
	        InsuredVehicleInfo insuredVeh = new InsuredVehicleInfo();

			Optional<InsuredVehicleInfo> optionalInsuredVeh = repository.findByClaimNoAndGarageId(requestDto.getClaimNo(),
					requestDto.getGarageId());
			if (optionalInsuredVeh.isPresent()) {
				insuredVeh = optionalInsuredVeh.get();
			}
	        Map<String, String> requestBodyMap = new HashMap<>();
			requestBodyMap.put("idType", "UPL_DOC_LIST");
			requestBodyMap.put("companyId", "001");
			requestBodyMap.put("productId", insuredVeh.getProductId());
			requestBodyMap.put("moduleType", "02");

	        // Prepare headers
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        // If JWT is required:
	        // String jwtToken = authenticateUserCall();
	        // headers.set("Authorization", "Bearer " + jwtToken);

	        // Convert DTO to JSON
	        String requestBody = objectMapper.writeValueAsString(requestBodyMap);
	        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
	        log.setRequest(requestBody);

	        logger.info("Request to Dynamic LOV API: " + requestBody);

	        // SSL setup if necessary
	        TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
	            public java.security.cert.X509Certificate[] getAcceptedIssuers() { return null; }
	            public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
	            public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) { }
	        }};
	        SSLContext sc = SSLContext.getInstance("SSL");
	        sc.init(null, trustAllCerts, new java.security.SecureRandom());
	        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
	        HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

	        // Call external API
	        ResponseEntity<String> apiResponse = restTemplate.postForEntity(externalApiUrlLov, entity, String.class);
	        log.setResponse(apiResponse.getBody());
	        log.setStatus("SUCCESS");
	        System.out.println("output LOV API Response: " + apiResponse.getBody());
	        // Parse response into CommonRes
	        JsonNode rootNode = objectMapper.readTree(apiResponse.getBody());
	        JsonNode dataNode = rootNode.get("data"); // 'data' field contains the LOV list

	        if (dataNode != null && dataNode.isArray()) {
	            lovItems = objectMapper.convertValue(dataNode, new TypeReference<List<LovItem>>() {});
	        }

	    } catch (Exception e) {
	        log.setStatus("FAILURE");
	        log.setErrorMessage(e.getMessage());
	        logger.error("Error calling Dynamic LOV API", e);
	        throw new RuntimeException("Failed to fetch Dynamic LOV: " + e.getMessage());
	    } finally {
	        log.setResponseTime(LocalDateTime.now());
	        if (StringUtils.isNotBlank(log.getRequest())) {
	            apiTransactionLogRepo.save(log);
	            logger.info(log.getEndpoint() + " ==> " + log);
	        }
	    }

	    return lovItems;
	}


	

	}



