package com.maan.veh.claim.serviceimpl;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.nio.file.Path;
import java.text.DateFormatSymbols;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maan.veh.claim.dto.ClaimIntimationDTOAttachmentDetails;
import com.maan.veh.claim.dto.ClaimIntimationDTODocumentDetails;
import com.maan.veh.claim.dto.ClaimIntimationDTODriver;
import com.maan.veh.claim.dto.ClaimIntimationDTORequestMetaData;
import com.maan.veh.claim.dto.ClaimIntimationDTOThirdPartyInfo;
import com.maan.veh.claim.dto.ClaimTransactionRequestDTO;
import com.maan.veh.claim.dto.ClaimTransactionRequestDTOMetaData;
import com.maan.veh.claim.dto.ClaimentCoverageRequestDTO;
import com.maan.veh.claim.dto.ClaimentCoverageResponseDTO;
import com.maan.veh.claim.dto.FnolRequestDTO;
import com.maan.veh.claim.dto.FnolRequestDTOMetaData;
import com.maan.veh.claim.dto.GarageClaimListDto;
import com.maan.veh.claim.dto.GarageClaimListResponseDTO;
import com.maan.veh.claim.dto.GetPolicyDetailsRequestDto;
import com.maan.veh.claim.dto.PolicyResponseDTO;
import com.maan.veh.claim.dto.SaveClaimRequestDTO;
import com.maan.veh.claim.dto.SaveSparePartsDTO;
import com.maan.veh.claim.entity.ApiIntegMaster;
import com.maan.veh.claim.entity.ApiTransactionLog;
import com.maan.veh.claim.entity.ClaimIntimationDetails;
import com.maan.veh.claim.entity.ClaimIntimationDetailsId;
import com.maan.veh.claim.entity.DamageSectionDetails;
import com.maan.veh.claim.entity.GarageWorkOrder;
import com.maan.veh.claim.entity.InsuredVehicleInfo;
import com.maan.veh.claim.entity.LoginMaster;
import com.maan.veh.claim.entity.SparePartsSaveDetails;
import com.maan.veh.claim.entity.VcSparePartsDetails;
import com.maan.veh.claim.file.BASE64DecodedMultipartFile;
import com.maan.veh.claim.repository.ApiIntegMasterRepository;
import com.maan.veh.claim.repository.ApiTransactionLogRepository;
import com.maan.veh.claim.repository.ClaimIntimationDetailsRepository;
import com.maan.veh.claim.repository.DamageSectionDetailsRepository;
import com.maan.veh.claim.repository.GarageWorkOrderRepository;
import com.maan.veh.claim.repository.InsuredVehicleInfoRepository;
import com.maan.veh.claim.repository.LoginMasterRepository;
import com.maan.veh.claim.repository.SparePartsSaveDetailsRepository;
import com.maan.veh.claim.repository.VcSparePartsDetailsRepository;
import com.maan.veh.claim.request.ClaimIntimationDocumentDetails;
import com.maan.veh.claim.request.ClaimIntimationRequestMetaData;
import com.maan.veh.claim.request.ClaimIntimationThirdPartyInfo;
import com.maan.veh.claim.request.ClaimListRequest;
import com.maan.veh.claim.request.ClaimListRequestDTO;
import com.maan.veh.claim.request.ClaimTransactionRequest;
import com.maan.veh.claim.request.ClaimentCoverageRequest;
import com.maan.veh.claim.request.ExternalVehicleGarageViewRequest;
import com.maan.veh.claim.request.FnolRequest;
import com.maan.veh.claim.request.GetClaimRequest;
import com.maan.veh.claim.request.LoginRequest;
import com.maan.veh.claim.request.SaveClaimRequest;
import com.maan.veh.claim.request.SaveSparePartsRequest;
import com.maan.veh.claim.request.SaveSparePartsRequestMetaData;
import com.maan.veh.claim.request.VehicleDamageDetailRequest;
import com.maan.veh.claim.response.ClaimIntimationResponse;
import com.maan.veh.claim.response.ClaimListResponse;
import com.maan.veh.claim.response.ClaimentCoverageResponse;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.ErrorDetail;
import com.maan.veh.claim.response.ErrorList;
import com.maan.veh.claim.response.ErrorResponse;
import com.maan.veh.claim.response.ExternalApiResponse;
import com.maan.veh.claim.response.GetAllQuoteResponse;
import com.maan.veh.claim.response.PolicyResponseUI;
import com.maan.veh.claim.service.ExternalApiService;

@Service
public class ExternalApiServiceImpl implements ExternalApiService {
	
	private static final Logger logger = Logger.getLogger(ExternalApiServiceImpl.class.getName());

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;
    
    @Autowired
    private ClaimIntimationDetailsRepository claimIntimationDetailsRepository;

    @Autowired
    private ApiTransactionLogRepository apiTransactionLogRepo;
    
    @Autowired
    private GarageWorkOrderRepository garageWorkOrderRepo;
    
    @Autowired
    private DamageSectionDetailsRepository damageSectionDetailsRepo;
    
    @Autowired
    private ApiIntegMasterRepository apiIntegMasterRepository;
    
    @Autowired
    private VcSparePartsDetailsRepository sparePartsDetailsRepo;
    
    @Autowired
    private InsuredVehicleInfoRepository insuredVehicleInfoRepo;
    
    @Value("${external.api.url.createfnol}")  
    private String externalApiUrlCreatefnol;
    
    @Value("${external.api.url.claimlisting}")  
    private String externalApiUrlClaimListing;
    
    @Value("${external.api.url.savespareparts}")  
    private String externalApiUrlSaveSpareparts;
    
    @Value("${external.api.url.garagelist}")  
    private String externalApiUrlGarageList;
    
    @Value("${external.api.url.getfnol}")  
    private String externalApiUrlGetfnol;
    
    @Value("${external.api.url.getfnolstatus}")
    private String externalApiUrlGetFnolStatus;
    
    @Value("${external.api.url.getClaimsDetailsByClaimNo}")
    private String checkClaimStatusApi;
    
    @Value("${external.api.url.listClaimantCoverages}")
    private String listClaimantCoverages;
    
    @Value("${external.api.url.authenticate}")
    private String externalApiUrlAuthenticate;
    
    @Value("${external.api.url.uploaddoc}")
    private String externalApiUrlUploadDoc;
    
    @Value("${auth.username}")
    private String apiusername;

    @Value("${auth.password}")
    private String apipassword;
    
    @Value("${common.path}")
	private Path rootLocation;
    
    @Autowired
    private InputValidationUtil validation;
    
    @Autowired
    private SparePartsSaveDetailsRepository SparePartsSaveDetailsRepo;
    
    @Autowired
	private LoginMasterRepository loginRepo;
    
    @Autowired
    private DropDownServiceImpl dropDownServiceImpl;
    
    @Autowired
    private InsuredVehicleInfoRepository repository;

    @Override
    public CommonResponse createFnol(SaveClaimRequest requestPayload) {
        CommonResponse response = new CommonResponse();
        ApiTransactionLog log = new ApiTransactionLog();
        log.setSno(apiTransactionLogRepo.findMaxSno() + 1);
        log.setRequestTime(LocalDateTime.now());
        log.setEntryDate(new Date());
        log.setEndpoint(externalApiUrlCreatefnol);
        String reportSeries = "";
        // Validate requestPayload
        List<ErrorList> validationErrors = validation.validateClaimIntemationDetails(requestPayload);
        if (!validationErrors.isEmpty()) {
            response.setErrors(validationErrors);
            response.setMessage("Validation failed");
            response.setResponse(Collections.emptyMap());
            response.setIsError(true);
            return response;
        }
        try {
			ClaimIntimationDetails newData = new ClaimIntimationDetails();
			//Optional<ClaimIntimationDetails> optional = claimIntimationDetailsRepository.findByPolicyNo(requestPayload.getPolicyNo());
			Optional<ClaimIntimationDetails> optional = claimIntimationDetailsRepository.findByPolicyNoAndPoliceReportNo(requestPayload.getPolicyNo(),requestPayload.getPoliceReportNo());
			if(optional.isPresent()){
				newData = optional.get();
			}
           // Set fields from requestPayload into newData
			newData.setPolicyNo(requestPayload.getPolicyNo());
			newData.setRequestOrigin("API");
			newData.setCurrentBranch(requestPayload.getRequestMetaData().getCurrentBranch());
			newData.setOriginBranch(requestPayload.getRequestMetaData().getOriginBranch());
			newData.setUserName(requestPayload.getUserName());
			newData.setIpAddress(requestPayload.getRequestMetaData().getIpAddress());
			newData.setRequestGeneratedDateTime(new Date()); // Assuming this is the current date and time
			newData.setConsumerTrackingId(requestPayload.getRequestMetaData().getConsumerTrackingID());
			newData.setLanguageCode(requestPayload.getLanguageCode());
			newData.setInsuredId(requestPayload.getInsuredId());
//			newData.setLossDate(requestPayload.getLossDate());
			try{
				// Combine LocalDate and LocalTime into LocalDateTime
				LocalDateTime combinedLossDateTime = LocalDateTime.of(requestPayload.getLossDate(), requestPayload.getLossTime());

				// Convert LocalDateTime to Date (if your entity requires Date instead of LocalDateTime)
				Date finalLossDateTime = Date.from(combinedLossDateTime.atZone(ZoneId.systemDefault()).toInstant());

				// Set the combined loss date and time in the entity
				newData.setLossDate(finalLossDateTime);

			}catch(Exception ex) {
            	
            }
			newData.setIntimatedDate(requestPayload.getIntimatedDate());
			newData.setLossLocation(requestPayload.getLossLocation());
			newData.setNatureOfLoss(requestPayload.getNatureOfLoss());
			newData.setPoliceStation(requestPayload.getPoliceStation());
			if(StringUtils.isBlank(requestPayload.getPoliceReportNo())) {
				reportSeries = "PNR-" + (claimIntimationDetailsRepository.count() + 1);
			}else {
				reportSeries = requestPayload.getPoliceReportNo();
			}
			
			newData.setPoliceReportNo(reportSeries);
			newData.setLossDescription(requestPayload.getLossDescription());
			newData.setAtFault(requestPayload.getAtFault());
			claimIntimationDetailsRepository.save(newData);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error while saving data");
		}
        try {
            // Extract JWT token from request
            String jwtToken = authenticateUserCall();
            
            // Create headers with JWT token
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Convert requestPayload to JSON and add headers
            SaveClaimRequestDTO dto = mapToSaveClaimRequestDTO(requestPayload);
            
            String requestBody = objectMapper.writeValueAsString(dto);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            log.setRequest(requestBody);
            logger.info(requestBody);
            // Configure SSL Trust Managers (if necessary)
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

            // Send request to external API with JWT in Authorization header
            ResponseEntity<String> apiResponse = restTemplate.postForEntity(log.getEndpoint(), entity, String.class);
            log.setResponse(apiResponse.getBody());
            log.setStatus("SUCCESS");
            
            // Parse the raw response into ExternalApiResponse object
            ExternalApiResponse externalApiResponse = objectMapper.readValue(apiResponse.getBody(), ExternalApiResponse.class);
            
          //saving fnol number
            try{
            	ClaimIntimationDetails oldData = new ClaimIntimationDetails();
    			
            	Optional<ClaimIntimationDetails> optional = claimIntimationDetailsRepository.findByPolicyNoAndPoliceReportNo(requestPayload.getPolicyNo(),reportSeries);
    			if(optional.isPresent()){
    				oldData = optional.get();
    				oldData.setFnolNo(externalApiResponse.getData().getFnolNo());
    				oldData.setClaimStatusCode(externalApiResponse.getData().getClaimStatusCode());
    				oldData.setClaimType(externalApiResponse.getData().getClaimType());
    				oldData.setFnolSgsId(externalApiResponse.getData().getFnolSgsId());
    				oldData.setClaimPartyId(externalApiResponse.getData().getClaimPartyId());
    				claimIntimationDetailsRepository.save(oldData);
    			}
    			
            }catch (Exception e) {
    			// TODO Auto-generated catch block
    			System.out.println("Error while saving data");
    		}

            if (externalApiResponse.isHasError()) {
                // Create custom error response
                List<ErrorResponse> errorList = new ArrayList<>();
                for (ErrorDetail error : externalApiResponse.getData().getErrorDetailsList()) {
                    errorList.add(new ErrorResponse("Azentio API "+externalApiUrlCreatefnol, error.getErrorField(), error.getErrorDescription()));
                }
                response.setErrors(errorList);
                response.setMessage(externalApiResponse.getMessage());
                response.setResponse(Collections.emptyMap());
                response.setIsError(true);
            } else {
                response.setMessage("Data saved successfully");
                response.setIsError(false);
                response.setResponse(externalApiResponse);
            }

        } catch (Exception e) {
            log.setStatus("FAILURE");
            log.setErrorMessage(e.getMessage());
            response.setMessage("Failed to save data");
            response.setIsError(true);
            response.setErrors(Collections.singletonList(new ErrorResponse("100", "API Error", e.getMessage()))); // API Error error
        } finally {
            log.setResponseTime(LocalDateTime.now());
            apiTransactionLogRepo.save(log);
            logger.info(externalApiUrlCreatefnol +" ==> "+ log);
        }

        return response;
    }


	@Override
	public CommonResponse findFNOL(FnolRequest requestPayload) {
		CommonResponse response = new CommonResponse();
        ApiTransactionLog log = new ApiTransactionLog();
        log.setRequestTime(LocalDateTime.now());
        log.setEntryDate(new Date());
        log.setEndpoint(externalApiUrlGetfnol);

        try {
            // Validate request payload
            List<ErrorList> errors = validation.validateFnolRequest(requestPayload);
            if (!errors.isEmpty()) {
                response.setErrors(errors);
                response.setMessage("Failed");
                response.setResponse(Collections.emptyMap());
                response.setIsError(true);
                return response;
            }
            
            // Extract JWT token from request
            String jwtToken = authenticateUserCall();
            
            // Create headers with JWT token
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Convert Map to JSON string
            FnolRequestDTO dto = mapToFnolRequestDTO(requestPayload);
            String requestBody = objectMapper.writeValueAsString(dto);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            log.setRequest(requestBody);
            logger.info(requestBody);
            // Send request to external API with JWT in Authorization header
            ResponseEntity<String> apiResponse = restTemplate.postForEntity(log.getEndpoint(), entity, String.class);
            log.setResponse(apiResponse.getBody());
            log.setStatus("SUCCESS");

            // Parse API response to Map
            response.setMessage("Data saved successfully");
            response.setIsError(false);
            response.setResponse(apiResponse.getBody());

        } catch (JsonProcessingException e) {
            log.setStatus("FAILURE");
            log.setErrorMessage(e.getMessage());
            response.setMessage("Failed to process JSON");
            response.setIsError(true);
        } catch (Exception e) {
            log.setStatus("FAILURE");
            log.setErrorMessage(e.getMessage());
            response.setMessage("Failed to save data");
            response.setIsError(true);
        } finally {
            log.setResponseTime(LocalDateTime.now());
            //apiTransactionLogRepo.save(log);
            logger.info(externalApiUrlGetfnol +" ==> "+ log);
        }

        return response;
	}

	@Override
    public CommonResponse getFnolStatus(ClaimTransactionRequest request) {
        CommonResponse response = new CommonResponse();
        ApiTransactionLog log = new ApiTransactionLog();
        log.setRequestTime(LocalDateTime.now());
        log.setEntryDate(new Date());
        log.setEndpoint(externalApiUrlGetFnolStatus);

        try {
            // Validate request
            List<ErrorList> errors = validation.validateClaimTransactionRequest(request);
            if (!errors.isEmpty()) {
                response.setErrors(errors);
                response.setMessage("Failed");
                response.setResponse(Collections.emptyMap());
                response.setIsError(true);
                return response;
            }
         // Extract JWT token from request
            String jwtToken = authenticateUserCall();
            
            // Create headers with JWT token
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
                 
            // Convert Map to JSON string
            ClaimTransactionRequestDTO dto = mapToClaimTransactionRequestDTO(request);
            String requestBody = objectMapper.writeValueAsString(dto);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            log.setRequest(requestBody);
            logger.info(requestBody);
            // Send request to external API with JWT in Authorization header
            ResponseEntity<String> apiResponse = restTemplate.postForEntity(log.getEndpoint(), entity, String.class);
            log.setResponse(apiResponse.getBody());
            log.setStatus("SUCCESS");

            // Parse API response to Map
            response.setMessage("Data retrieved successfully");
            response.setIsError(false);
            response.setResponse(apiResponse.getBody());

        } catch (JsonProcessingException e) {
            log.setStatus("FAILURE");
            log.setErrorMessage(e.getMessage());
            response.setMessage("Failed to process JSON");
            response.setIsError(true);
        } catch (Exception e) {
            log.setStatus("FAILURE");
            log.setErrorMessage(e.getMessage());
            response.setMessage("Failed to retrieve data");
            response.setIsError(true);
        } finally {
            log.setResponseTime(LocalDateTime.now());
            //apiTransactionLogRepo.save(log);
            logger.info(externalApiUrlGetFnolStatus +" ==> "+ log);
        }

        return response;
    }

	@Override
	public CommonResponse authenticateUser(LoginRequest request) {
	    CommonResponse response = new CommonResponse();
	    ApiTransactionLog log = new ApiTransactionLog();
	    log.setRequestTime(LocalDateTime.now());
	    log.setEntryDate(new Date());
	    log.setEndpoint(externalApiUrlAuthenticate);

	    // Validate LoginRequest
	    List<ErrorList> errors = validation.validateLoginRequest(request);
	    if (!errors.isEmpty()) {
	        response.setErrors(errors);
	        response.setMessage("Validation failed");
	        response.setResponse(Collections.emptyMap());
	        response.setIsError(true);
	        return response;
	    }

	    try {
	        // Manually map the LoginRequest fields to the required format
	        Map<String, String> formattedRequest = Map.of(
	            "username", request.getLoginId(),
	            "password", request.getPassword()
	        );
	        
	        // Convert Map to JSON string
	        String requestBody = objectMapper.writeValueAsString(formattedRequest);
	        log.setRequest(requestBody);
	        logger.info(requestBody);
	        // Set the headers, including Content-Type as application/json
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);  // Fix the Content-Type to application/json

	        // Create HttpEntity containing headers and the request body
	        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

	        // Send request to external authentication API using RestTemplate
	        ResponseEntity<String> apiResponse = restTemplate.postForEntity(log.getEndpoint(), entity, String.class);
	        
	        // Log the response
	        log.setResponse(apiResponse.getBody());
	        log.setStatus("SUCCESS");

	        // Parse API response and prepare CommonResponse
	        response.setMessage("Authentication successful");
	        response.setIsError(false);
	        response.setResponse(apiResponse.getBody());

	    } catch (HttpClientErrorException e) {
	        // Handle 4xx errors, possibly including 415
	        log.setStatus("FAILURE");
	        log.setErrorMessage(e.getMessage());
	        response.setMessage("Authentication failed: " + e.getStatusCode());
	        response.setIsError(true);
	        response.setResponse(e.getResponseBodyAsString());
	    } catch (Exception e) {
	        log.setStatus("FAILURE");
	        log.setErrorMessage(e.getMessage());
	        response.setMessage("Authentication failed");
	        response.setIsError(true);
	        response.setResponse(e.getMessage());
	    } finally {
	        log.setResponseTime(LocalDateTime.now());
	        ////apiTransactionLogRepo.save(log);
	        logger.info(externalApiUrlAuthenticate +" ==> "+ log);
	    }

	    return response;
	}

	
	public SaveClaimRequestDTO mapToSaveClaimRequestDTO(SaveClaimRequest request) {
	    if (request == null) {
	        return null;
	    }
	    
	    SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	    
	    SaveClaimRequestDTO dto = new SaveClaimRequestDTO();
	    
	    // Map simple fields
	    dto.setLanguageCode(request.getLanguageCode());
	    dto.setPolicyNo(request.getPolicyNo());
	    dto.setInsuredId(request.getInsuredId());
//	    dto.setLossDate(isoDateFormat.format(request.getLossDate()));
	    try{
			// Combine LocalDate and LocalTime into LocalDateTime
			LocalDateTime combinedLossDateTime = LocalDateTime.of(request.getLossDate(), request.getLossTime());

			// Convert LocalDateTime to Date (if your entity requires Date instead of LocalDateTime)
			Date finalLossDateTime = Date.from(combinedLossDateTime.atZone(ZoneId.systemDefault()).toInstant());

			// Set the combined loss date and time in the entity
			dto.setLossDate(isoDateFormat.format(finalLossDateTime));

		}catch(Exception ex) {
        	
        }
	    dto.setIntimatedDate(isoDateFormat.format(request.getIntimatedDate()));
	    dto.setNatureOfLoss(request.getNatureOfLoss());
	    dto.setLossLocation(request.getLossLocation());
//	    dto.setPoliceStation(request.getPoliceStation());
//	    dto.setPoliceReportNo(request.getPoliceReportNo());
	    dto.setLossDescription(request.getLossDescription());
	    dto.setAtFault(request.getAtFault());
	    dto.setPolicyPeriod(request.getPolicyPeriod());
	    dto.setContactPersonPhoneNo(request.getContactPersonPhoneNo());
	    dto.setContactPersonPhoneCode(request.getContactPersonPhoneCode());
	    dto.setPolicyReferenceNo(request.getPolicyReferenceNo());
	    dto.setPolicyICReferenceNo(request.getPolicyICReferenceNo());
	    dto.setClaimRequestReference(request.getClaimRequestReference());
	    dto.setClaimCategory(request.getClaimCategory());
	    dto.setCreatedUser(request.getCreatedUser());
	    dto.setClaimType(request.getClaimType());
	    dto.setAccidentNumber(request.getAccidentNumber());
	    dto.setIsThirdPartyInvolved(request.getIsThirdPartyInvolved());

	    // Map complex fields
	    if (request.getRequestMetaData() != null) {
	        ClaimIntimationDTORequestMetaData metaData = new ClaimIntimationDTORequestMetaData();
	        metaData.setConsumerTrackingID(request.getRequestMetaData().getConsumerTrackingID());
	        metaData.setCurrentBranch(request.getRequestMetaData().getCurrentBranch());
	        metaData.setIpAddress(request.getRequestMetaData().getIpAddress());
	        metaData.setOriginBranch(request.getRequestMetaData().getOriginBranch());
	        metaData.setRequestData(request.getRequestMetaData().getRequestData());
	        metaData.setRequestGeneratedDateTime(isoDateFormat.format(new Date()));
	        metaData.setRequestId(request.getRequestMetaData().getRequestId());
	        metaData.setRequestOrigin(request.getRequestMetaData().getRequestOrigin());
	        metaData.setRequestReference(request.getRequestMetaData().getRequestReference());
	        metaData.setRequestedService(request.getRequestMetaData().getRequestedService());
	        metaData.setResponseData(request.getRequestMetaData().getResponseData());
	        metaData.setSourceCode(request.getRequestMetaData().getSourceCode());
	        metaData.setUserName(request.getRequestMetaData().getUserName());
	        dto.setRequestMetaData(metaData);
	    }

	    if (request.getDriver() != null) {
	    	ClaimIntimationDTODriver driverDTO = new ClaimIntimationDTODriver();
	        driverDTO.setEmiratesId(request.getDriver().getEmiratesId());
	        driverDTO.setLicenseNumber(request.getDriver().getLicenseNumber());
	        driverDTO.setDob(request.getDriver().getDob());
	        dto.setDriver(driverDTO);
	    }

	    if (request.getAttachmentDetails() != null) {
	    	ClaimIntimationDTOAttachmentDetails attachmentDetailsDTO = new ClaimIntimationDTOAttachmentDetails();
	        List<ClaimIntimationDTODocumentDetails> documentDetailsDTOList = new ArrayList<>();

	        for (ClaimIntimationDocumentDetails document : request.getAttachmentDetails().getDocumentDetails()) {
	        	ClaimIntimationDTODocumentDetails documentDetailsDTO = new ClaimIntimationDTODocumentDetails();
	            documentDetailsDTO.setDocumentData(document.getDocumentData());
	            documentDetailsDTO.setDocumentFormat(document.getDocumentFormat());
	            documentDetailsDTO.setDocumentId(document.getDocumentId());
	            documentDetailsDTO.setDocumentName(document.getDocumentName());
	            documentDetailsDTO.setDocumentRefNo(document.getDocumentRefNo());
	            documentDetailsDTO.setDocumentType(document.getDocumentType());
	            documentDetailsDTO.setDocumentURL(document.getDocumentURL());
	            
	            documentDetailsDTOList.add(documentDetailsDTO);
	        }

	        attachmentDetailsDTO.setDocumentDetails(documentDetailsDTOList);
	        dto.setAttachmentDetails(attachmentDetailsDTO);
	    }


	    if (request.getThirdPartyInfo() != null) {
	        List<ClaimIntimationDTOThirdPartyInfo> thirdPartyInfoList = new ArrayList<>();
	        for (ClaimIntimationThirdPartyInfo thirdParty : request.getThirdPartyInfo()) {
	        	ClaimIntimationDTOThirdPartyInfo thirdPartyInfoDTO = new ClaimIntimationDTOThirdPartyInfo();
	            thirdPartyInfoDTO.setTpDriverLiability(thirdParty.getTpDriverLiability());
	            thirdPartyInfoDTO.setTpDriverLicenceNo(thirdParty.getTpDriverLicenceNo());
	            thirdPartyInfoDTO.setTpDriverName(thirdParty.getTpDriverName());
	            thirdPartyInfoDTO.setTpDriverNationalityCode(thirdParty.getTpDriverNationalityCode());
	            thirdPartyInfoDTO.setTpDriverTrafficNo(thirdParty.getTpDriverTrafficNo());
	            thirdPartyInfoDTO.setTpMobileNumber(thirdParty.getTpMobileNumber());
	            thirdPartyInfoDTO.setTpVehicleCurrentInsurer(thirdParty.getTpVehicleCurrentInsurer());
	            thirdPartyInfoDTO.setTpVehicleMake(thirdParty.getTpVehicleMake());
	            thirdPartyInfoDTO.setTpVehicleMakeCode(thirdParty.getTpVehicleMakeCode());
	            thirdPartyInfoDTO.setTpVehicleModel(thirdParty.getTpVehicleModel());
	            thirdPartyInfoDTO.setTpVehicleModelCode(thirdParty.getTpVehicleModelCode());
	            thirdPartyInfoDTO.setTpVehiclePlateCode(thirdParty.getTpVehiclePlateCode());
	            thirdPartyInfoDTO.setTpVehiclePlateNo(thirdParty.getTpVehiclePlateNo());
	            thirdPartyInfoDTO.setTpVehiclePlateTypeCode(thirdParty.getTpVehiclePlateTypeCode());
	            thirdPartyInfoDTO.setThirdPartyReference(thirdParty.getThirdPartyReference());
	            thirdPartyInfoDTO.setThirdPartyType(thirdParty.getThirdPartyType());
	            thirdPartyInfoList.add(thirdPartyInfoDTO);
	        }
	        dto.setThirdPartyInfo(thirdPartyInfoList);
	    }

	    return dto;
	}
	
	public FnolRequestDTO mapToFnolRequestDTO(FnolRequest request) {
	    FnolRequestDTO dto = new FnolRequestDTO();

	    // Map simple fields
	    dto.setCustomerId(request.getCustomerId());
	    dto.setPolicyNo(request.getPolicyNo());
	    dto.setFnolNo(request.getFnolNo());
	    dto.setLossDate(request.getLossDate());

	    // Map RequestMetaData
	    if (request.getRequestMetaData() != null) {
	        FnolRequestDTOMetaData requestMetaDataDTO = new FnolRequestDTOMetaData();
	        requestMetaDataDTO.setConsumerTrackingID(request.getRequestMetaData().getConsumerTrackingID());
	        requestMetaDataDTO.setCurrentBranch(request.getRequestMetaData().getCurrentBranch());
	        requestMetaDataDTO.setIpAddress(request.getRequestMetaData().getIpAddress());
	        requestMetaDataDTO.setOriginBranch(request.getRequestMetaData().getOriginBranch());
	        requestMetaDataDTO.setRequestData(request.getRequestMetaData().getRequestData());
	        requestMetaDataDTO.setRequestGeneratedDateTime(request.getRequestMetaData().getRequestGeneratedDateTime());
	        requestMetaDataDTO.setRequestId(request.getRequestMetaData().getRequestId());
	        requestMetaDataDTO.setRequestOrigin(request.getRequestMetaData().getRequestOrigin());
	        requestMetaDataDTO.setRequestReference(request.getRequestMetaData().getRequestReference());
	        requestMetaDataDTO.setRequestedService(request.getRequestMetaData().getRequestedService());
	        requestMetaDataDTO.setResponseData(request.getRequestMetaData().getResponseData());
	        requestMetaDataDTO.setSourceCode(request.getRequestMetaData().getSourceCode());
	        requestMetaDataDTO.setUserName(request.getRequestMetaData().getUserName());

	        dto.setRequestMetaData(requestMetaDataDTO);
	    }

	    return dto;
	}

	public ClaimTransactionRequestDTO mapToClaimTransactionRequestDTO(ClaimTransactionRequest request) {
	    if (request == null) {
	        return null;
	    }
	    
	    ClaimTransactionRequestDTO dto = new ClaimTransactionRequestDTO();
	    
	    dto.setClaimsTpReferenceNo(request.getClaimsTpReferenceNo());
	    dto.setFnolNo(request.getFnolNo());
	    dto.setTpPolicyReferenceNo(request.getTpPolicyReferenceNo());
	    dto.setTransactionRefNo(request.getTransactionRefNo());

	    // Map RequestMetaData
	    if (request.getRequestMetaData() != null) {
	        ClaimTransactionRequestDTOMetaData metaDataDTO = new ClaimTransactionRequestDTOMetaData();
	        metaDataDTO.setConsumerTrackingID(request.getRequestMetaData().getConsumerTrackingID());
	        metaDataDTO.setCurrentBranch(request.getRequestMetaData().getCurrentBranch());
	        metaDataDTO.setIpAddress(request.getRequestMetaData().getIpAddress());
	        metaDataDTO.setOriginBranch(request.getRequestMetaData().getOriginBranch());
	        metaDataDTO.setRequestData(request.getRequestMetaData().getRequestData());
	        metaDataDTO.setRequestGeneratedDateTime(request.getRequestMetaData().getRequestGeneratedDateTime());
	        metaDataDTO.setRequestId(request.getRequestMetaData().getRequestId());
	        metaDataDTO.setRequestOrigin(request.getRequestMetaData().getRequestOrigin());
	        metaDataDTO.setRequestReference(request.getRequestMetaData().getRequestReference());
	        metaDataDTO.setRequestedService(request.getRequestMetaData().getRequestedService());
	        metaDataDTO.setResponseData(request.getRequestMetaData().getResponseData());
	        metaDataDTO.setSourceCode(request.getRequestMetaData().getSourceCode());
	        metaDataDTO.setUserName(request.getRequestMetaData().getUserName());
	        
	        dto.setRequestMetaData(metaDataDTO);
	    }
	    
	    return dto;
	}
	

	public String authenticateUserCall() {
	    ApiTransactionLog log = new ApiTransactionLog();
	    log.setRequestTime(LocalDateTime.now());
	    log.setEntryDate(new Date());
	    log.setEndpoint(externalApiUrlAuthenticate);

	    // Load username and password from properties
	    String username = apiusername;
	    String password = apipassword;

	    // Create the request map
	    Map<String, String> formattedRequest = Map.of(
	        "username", username,
	        "password", password
	    );

	    try {
	        // Convert Map to JSON string
	        String requestBody = objectMapper.writeValueAsString(formattedRequest);
	        log.setRequest(requestBody);
	        logger.info(requestBody);
	        // Set the headers, including Content-Type as application/json
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);  // Fix the Content-Type to application/json

	        // Create HttpEntity containing headers and the request body
	        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
	        
	     // Configure SSL Trust Managers (if necessary)
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
	        
	        
	        // Send request to external authentication API
	        ResponseEntity<String> apiResponse = restTemplate.postForEntity(log.getEndpoint(), entity, String.class);
	        log.setResponse(apiResponse.getBody());
	        log.setStatus("SUCCESS");

	        // Extract JWT token from response
	        Map<String, String> responseMap = objectMapper.readValue(apiResponse.getBody(), Map.class);
	        String jwtToken = responseMap.get("jwt");

	        return jwtToken; // Return the JWT token

	    } catch (Exception e) {
	        log.setStatus("FAILURE");
	        log.setErrorMessage(e.getMessage());
	    } finally {
	        log.setResponseTime(LocalDateTime.now());
	        //apiTransactionLogRepo.save(log);
	        logger.info(externalApiUrlAuthenticate +" ==> "+ log);
	    }

	    return null; // Return null if authentication fails
	}
	
	public String saveOrUpdateClaimIntimation(SaveClaimRequest saveClaimRequestDTO) {
        try {
            // Check if a record already exists for the given policyNo
            Optional<ClaimIntimationDetails> existingRecord = claimIntimationDetailsRepository.findById(
                    new ClaimIntimationDetailsId(saveClaimRequestDTO.getPolicyNo(),saveClaimRequestDTO.getPoliceReportNo())
            );

            ClaimIntimationDetails claimIntimationDetails;

            if (existingRecord.isPresent()) {
                // Update the existing record
                claimIntimationDetails = existingRecord.get();
                logger.info("Updating existing record for policyNo: " + saveClaimRequestDTO.getPolicyNo());
            } else {
                // Insert new data
                claimIntimationDetails = new ClaimIntimationDetails();
                claimIntimationDetails.setPolicyNo(saveClaimRequestDTO.getPolicyNo());
                logger.info("Inserting new record for policyNo: " + saveClaimRequestDTO.getPolicyNo());
            }

            // Map the fields from SaveClaimRequestDTO to ClaimIntimationDetails entity
            claimIntimationDetails.setRequestOrigin(saveClaimRequestDTO.getRequestMetaData().getRequestOrigin());
            claimIntimationDetails.setCurrentBranch(saveClaimRequestDTO.getRequestMetaData().getCurrentBranch());
            claimIntimationDetails.setOriginBranch(saveClaimRequestDTO.getRequestMetaData().getOriginBranch());
            claimIntimationDetails.setUserName(saveClaimRequestDTO.getRequestMetaData().getUserName());
            claimIntimationDetails.setIpAddress(saveClaimRequestDTO.getRequestMetaData().getIpAddress());

            claimIntimationDetails.setRequestGeneratedDateTime(saveClaimRequestDTO.getRequestMetaData().getRequestGeneratedDateTime());
  
            claimIntimationDetails.setConsumerTrackingId(saveClaimRequestDTO.getRequestMetaData().getConsumerTrackingID());
            claimIntimationDetails.setLanguageCode(saveClaimRequestDTO.getLanguageCode());
            claimIntimationDetails.setInsuredId(saveClaimRequestDTO.getInsuredId());

//            claimIntimationDetails.setLossDate(saveClaimRequestDTO.getLossDate());
            try{
            	// Combine LocalDate and LocalTime into LocalDateTime
            	LocalDateTime combinedLossDateTime = LocalDateTime.of(saveClaimRequestDTO.getLossDate(), saveClaimRequestDTO.getLossTime());

            	// Convert LocalDateTime to Date (if your entity requires Date instead of LocalDateTime)
            	Date finalLossDateTime = Date.from(combinedLossDateTime.atZone(ZoneId.systemDefault()).toInstant());

            	// Set the combined loss date and time in the entity
            	claimIntimationDetails.setLossDate(finalLossDateTime);

            }catch(Exception ex) {
            	
            }
            claimIntimationDetails.setIntimatedDate(saveClaimRequestDTO.getIntimatedDate());

            claimIntimationDetails.setLossLocation(saveClaimRequestDTO.getLossLocation());
            claimIntimationDetails.setNatureOfLoss(saveClaimRequestDTO.getNatureOfLoss());
            claimIntimationDetails.setPoliceStation(saveClaimRequestDTO.getPoliceStation());
            claimIntimationDetails.setPoliceReportNo(saveClaimRequestDTO.getPoliceReportNo());
            claimIntimationDetails.setLossDescription(saveClaimRequestDTO.getLossDescription());
            claimIntimationDetails.setAtFault(saveClaimRequestDTO.getAtFault());

            // Save or update the record in the database
            claimIntimationDetailsRepository.save(claimIntimationDetails);
            return "Success: Data saved/updated for policyNo: " + saveClaimRequestDTO.getPolicyNo();

        } catch (Exception e) {
            logger.severe("Error saving/updating data for policyNo: " + saveClaimRequestDTO.getPolicyNo() + " - " + e.getMessage());
            return "Error: Unable to save/update data for policyNo: " + saveClaimRequestDTO.getPolicyNo();
        }
    }

	@Override
	public CommonResponse getClaimByPolicy(GetClaimRequest req) {
	    CommonResponse response = new CommonResponse();
	    try {
	        // Fetch data by policy number
	        Optional<ClaimIntimationDetails> dataOptional = claimIntimationDetailsRepository.findByPolicyNoAndPoliceReportNo(req.getPolicyNo(),req.getPoliceReportNo());
	        
	        if (dataOptional.isPresent()) {
	            ClaimIntimationDetails data = dataOptional.get();
	            ClaimIntimationResponse formattedResponse = mapToClaimIntimationResponse(data);
	            
	            response.setResponse(formattedResponse);
	            response.setMessage("Data fetched successfully");
	            response.setIsError(false);
	        } else {
	            response.setMessage("No data found for policy number: " + req.getPolicyNo());
	            response.setIsError(true);
	        }
	    } catch (Exception e) {
	        response.setMessage("Failed to fetch data");
	        response.setIsError(true);
	    }
	    return response;
	}

	@Override
	public CommonResponse getAllClaims() {
	    CommonResponse response = new CommonResponse();
	    try {
	        List<ClaimIntimationDetails> dataList = claimIntimationDetailsRepository.findAllByOrderByRequestGeneratedDateTimeDesc();
	        List<ClaimIntimationResponse> responseList = dataList.stream()
	                .map(this::mapToClaimIntimationResponse)
	                .collect(Collectors.toList());
	        
	        response.setResponse(responseList);
	        response.setMessage("Data fetched successfully");
	        response.setIsError(false);
	    } catch (Exception e) {
	        response.setMessage("Failed to fetch data");
	        response.setIsError(true);
	    }
	    return response;
	}

	private ClaimIntimationResponse mapToClaimIntimationResponse(ClaimIntimationDetails data) {
	    ClaimIntimationResponse response = new ClaimIntimationResponse();

	    try {
	        // Set fields from ClaimIntimationDetails entity to ClaimIntimationResponse
	        response.setLanguageCode(data.getLanguageCode());
	        response.setPolicyNo(data.getPolicyNo());
	        response.setFnolNo(data.getFnolNo());
	        response.setInsuredId(data.getInsuredId());
	        response.setUserName(data.getUserName());

			SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
			SimpleDateFormat timeFormat = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);
			timeFormat.setDateFormatSymbols(new DateFormatSymbols(Locale.ENGLISH) {
			    @Override
			    public String[] getAmPmStrings() {
			        return new String[]{"AM", "PM"}; // Ensure uppercase AM/PM
			    }
			});

			// Convert and set the Loss Date
			Date lossDate = data.getLossDate();
			if (lossDate != null) {
				response.setLossDate(dateFormat.format(lossDate));
				response.setLossTime(timeFormat.format(lossDate)); // Set time part
			}

			// Convert and set the Intimated Date
			Date intimatedDate = data.getIntimatedDate();
			if (intimatedDate != null) {
				response.setIntimatedDate(dateFormat.format(intimatedDate));
			}
//	        response.setLossDate(data.getLossDate());
//	        response.setIntimatedDate(data.getIntimatedDate());
	        response.setNatureOfLoss(data.getNatureOfLoss());
	        response.setLossLocation(data.getLossLocation());
	        response.setPoliceStation(data.getPoliceStation());
	        response.setPoliceReportNo(data.getPoliceReportNo());
	        response.setLossDescription(data.getLossDescription());
	        response.setAtFault(data.getAtFault());
	        response.setPolicyPeriod("Not available");  // Map other fields as needed

	        // Set nested objects like requestMetaData, driver, attachmentDetails
	        response.setRequestMetaData(mapToRequestMetaData(data));
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return response;
	}

	// Example of mapping nested objects (you need to complete as per your structure)
	private ClaimIntimationRequestMetaData mapToRequestMetaData(ClaimIntimationDetails data) {
	    ClaimIntimationRequestMetaData metaData = new ClaimIntimationRequestMetaData();

	    try {
	        // Map fields from ClaimIntimationDetails to ClaimIntimationRequestMetaData
	        metaData.setConsumerTrackingID(data.getConsumerTrackingId());
	        metaData.setCurrentBranch(data.getCurrentBranch());
	        metaData.setIpAddress(data.getIpAddress());
	        metaData.setOriginBranch(data.getOriginBranch()); 

	        // Format requestGeneratedDateTime similarly
	        if (data.getRequestGeneratedDateTime() != null) {
	            metaData.setRequestGeneratedDateTime(data.getRequestGeneratedDateTime());
	        }
	        
	        metaData.setRequestOrigin(data.getRequestOrigin());
	    } catch (Exception e) {
	        e.printStackTrace();
	    } 

	    return metaData;
	}


	@Override
	public CommonResponse getClaimListing(ClaimListRequest requestPayload) {
		CommonResponse response = new CommonResponse();
        ApiTransactionLog log = new ApiTransactionLog();
        log.setRequestTime(LocalDateTime.now());
        log.setEntryDate(new Date());
        log.setEndpoint(externalApiUrlClaimListing);

        try {
            // Extract JWT token from request
            String jwtToken = authenticateUserCall();
            
            // Create headers with JWT token
            HttpHeaders headers = new HttpHeaders();
            headers.set("Authorization", "Bearer " + jwtToken);
            headers.setContentType(MediaType.APPLICATION_JSON);
            
            // Convert requestPayload to JSON and add headers
            ClaimListRequestDTO dto = mapToClaimListingDTO(requestPayload);
            
            String requestBody = objectMapper.writeValueAsString(dto);
            HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
            log.setRequest(requestBody);
            logger.info(requestBody);
            // Configure SSL Trust Managers (if necessary)
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

            // Send request to external API with JWT in Authorization header
            ResponseEntity<String> apiResponse = restTemplate.postForEntity(log.getEndpoint(), entity, String.class);
            log.setResponse(apiResponse.getBody());
            log.setStatus("SUCCESS");

            // Parse the raw response into ExternalApiResponse object
            ClaimListResponse externalApiResponse = objectMapper.readValue(apiResponse.getBody(), ClaimListResponse.class);

            if ("true".equalsIgnoreCase(externalApiResponse.getHasError())) {
                // Create custom error response
                List<ErrorResponse> errorList = new ArrayList<>();
//                for (ErrorDetail error : externalApiResponse.getData().getErrorDetailsList()) {
//                    errorList.add(new ErrorResponse(error.getErrorCode(), error.getErrorField(), error.getErrorDescription()));
//                }
                response.setErrors(errorList);
                response.setMessage(externalApiResponse.getMessage());
                response.setResponse(externalApiResponse);
                response.setIsError(true);
            } else {
                response.setMessage("Data saved successfully");
                response.setIsError(false);
                response.setResponse(externalApiResponse);
            }

        } catch (Exception e) {
            log.setStatus("FAILURE");
            log.setErrorMessage(e.getMessage());
            response.setMessage("Failed to save data");
            response.setIsError(true);
            response.setErrors(Collections.singletonList(new ErrorResponse("100", "API Error", e.getMessage()))); // API Error error
        } finally {
            log.setResponseTime(LocalDateTime.now());
            //apiTransactionLogRepo.save(log);
            logger.info(externalApiUrlClaimListing +" ==> "+ log);
        }

        return response;
	}


	private ClaimListRequestDTO mapToClaimListingDTO(ClaimListRequest requestPayload) {
	    if (requestPayload == null) {
	        return null;
	    }

	    ClaimIntimationDTORequestMetaData dtoRequestMetaData = new ClaimIntimationDTORequestMetaData();

	    try {
	        ClaimIntimationRequestMetaData requestMetaData = requestPayload.getRequestMetaData();

	        if (requestMetaData != null) {
	            try {
	                dtoRequestMetaData.setConsumerTrackingID(requestMetaData.getConsumerTrackingID());
	                dtoRequestMetaData.setCurrentBranch(requestMetaData.getCurrentBranch());
	                dtoRequestMetaData.setIpAddress(requestMetaData.getIpAddress());
	                dtoRequestMetaData.setOriginBranch(requestMetaData.getOriginBranch());
	                dtoRequestMetaData.setRequestData(requestMetaData.getRequestData());
	                
	                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	                dtoRequestMetaData.setRequestGeneratedDateTime(
	                    requestMetaData.getRequestGeneratedDateTime() != null 
	                        ? dateFormat.format(requestMetaData.getRequestGeneratedDateTime()) 
	                        : null
	                );
	                
	                dtoRequestMetaData.setRequestId(requestMetaData.getRequestId());
	                dtoRequestMetaData.setRequestOrigin(requestMetaData.getRequestOrigin());
	                dtoRequestMetaData.setRequestReference(requestMetaData.getRequestReference());
	                dtoRequestMetaData.setRequestedService(requestMetaData.getRequestedService());
	                dtoRequestMetaData.setResponseData(requestMetaData.getResponseData());
	                dtoRequestMetaData.setSourceCode(requestMetaData.getSourceCode());
	                dtoRequestMetaData.setUserName(requestMetaData.getUserName());
	            } catch (Exception e) {
	                System.err.println("Error mapping requestMetaData: " + e.getMessage());
	                e.printStackTrace();
	            }
	        }
	    } catch (Exception e) {
	        System.err.println("Error accessing requestMetaData from requestPayload: " + e.getMessage());
	        e.printStackTrace();
	    }

	    String claimNotificationFromDate = null;
	    String claimNotificationToDate = null;
	    String claimLossFromDate = null;
	    String claimLossToDate = null;

	    try {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	        claimNotificationFromDate = requestPayload.getClaimNotificationFromDate() != null 
	            ? dateFormat.format(requestPayload.getClaimNotificationFromDate()) : "";

	        claimNotificationToDate = requestPayload.getClaimNotificationToDate() != null 
	            ? dateFormat.format(requestPayload.getClaimNotificationToDate()) : "";

	        claimLossFromDate = requestPayload.getClaimLossFromDate() != null 
	            ? dateFormat.format(requestPayload.getClaimLossFromDate()) : "";

	        claimLossToDate = requestPayload.getClaimLossToDate() != null 
	            ? dateFormat.format(requestPayload.getClaimLossToDate()) : "";

	    } catch (Exception e) {
	        System.err.println("Error converting date fields: " + e.getMessage());
	        e.printStackTrace();
	    }

	    return new ClaimListRequestDTO(
	        requestPayload.getLobCode(),
	        requestPayload.getProdCode(),
	        requestPayload.getPolicyNumber(),
	        requestPayload.getClaimNotificationNumber(),
	        requestPayload.getCreatedBy(),
	        claimNotificationFromDate,
	        claimNotificationToDate,
	        claimLossFromDate,
	        claimLossToDate,
	        dtoRequestMetaData
	    );
	}


	@Override
	public CommonResponse saveSpareParts(SaveSparePartsDTO request) {

	    CommonResponse response = new CommonResponse();
	    ApiTransactionLog log = new ApiTransactionLog();
	    log.setSno(apiTransactionLogRepo.findMaxSno() + 1);
	    log.setRequestTime(LocalDateTime.now());
	    log.setEntryDate(new Date());
	    log.setEndpoint(externalApiUrlSaveSpareparts);

	    try {
	        // Retrieve data from repositories
	    	GarageWorkOrder workOrder = garageWorkOrderRepo.findByClaimNoAndQuotationNo(request.getClaimNo(),request.getQuotationNo());
	    	LoginMaster loginMaster = loginRepo.findByLoginId(workOrder.getGarageId());
	    	Optional<InsuredVehicleInfo> optional = insuredVehicleInfoRepo.findByClaimNoAndGarageId(request.getClaimNo(),workOrder.getGarageId());
	        List<DamageSectionDetails> damageDetails = damageSectionDetailsRepo.findByClaimNoAndQuotationNo(request.getClaimNo(),request.getQuotationNo());
	        SparePartsSaveDetails partsSaveDetails = SparePartsSaveDetailsRepo.findByClaimNoAndGarageCode(request.getClaimNo(),loginMaster.getCoreAppCode());
	        if (workOrder == null || damageDetails.isEmpty()) {
	            response.setMessage("No data found for the provided claim or work order number");
	            response.setIsError(true);
	            response.setErrors(Collections.singletonList(new ErrorResponse("404", "Data Not Found", "Data not found for the provided claim or work order number")));
	            return response;
	        }else if("Y".equalsIgnoreCase(partsSaveDetails.getSavedStatus())||"ESB".equalsIgnoreCase(partsSaveDetails.getSavedStatus())) {
	        	//response.setMessage("No data found for the provided claim or work order number");
	        	System.out.println("saved  status ==>"+partsSaveDetails.getSavedStatus());
	            response.setIsError(true);
	            ClaimListResponse externalRes = new ClaimListResponse();
	            externalRes.setMessage("spareparts details already saved");
	            response.setResponse(externalRes);
	            //response.setErrors(Collections.singletonList(new ErrorResponse("404", "Data Not Found", "Data not found for the provided claim or work order number")));
	            return response;
	        }

	        // Map entities to request DTO
	        //SaveSparePartsRequest dto = mapToSaveSparePartsRequest(workOrder, damageDetails);
	        SaveSparePartsRequest dto = mapToSaveSparePartsRequestV1(partsSaveDetails, damageDetails,optional.get());
	        
	        // Authenticate and retrieve JWT token
	        String jwtToken = authenticateUserCall();

	        // Create headers and add JWT token
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization", "Bearer " + jwtToken);
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        // Convert DTO to JSON for request body and add headers
	        String requestBody = objectMapper.writeValueAsString(dto);
	        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
	        log.setRequest(requestBody);
	        logger.info(requestBody);
	     // Configure SSL Trust Managers (if necessary)
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
	        
	        // Send request to external API
	        ResponseEntity<String> apiResponse = restTemplate.postForEntity(log.getEndpoint(), entity, String.class);
	        log.setResponse(apiResponse.getBody());
	        log.setStatus("SUCCESS");

	        // Parse response into ExternalApiResponse object
	        ClaimListResponse externalApiResponse = objectMapper.readValue(apiResponse.getBody(), ClaimListResponse.class);

	        // Process response based on external API success status
	        if ("true".equalsIgnoreCase(externalApiResponse.getHasError())) {
	            response.setMessage(externalApiResponse.getMessage());
	            //response.setErrors(externalApiResponse.getErrors());
	            response.setResponse(externalApiResponse);
	            response.setIsError(true);
	            partsSaveDetails.setSavedStatus("GPC");
	            SparePartsSaveDetailsRepo.save(partsSaveDetails);
	        } else {
	            response.setMessage("Data saved successfully");
	            response.setIsError(false);
	            response.setResponse(externalApiResponse);
	            partsSaveDetails.setSavedStatus("ESB");
	            SparePartsSaveDetailsRepo.save(partsSaveDetails);
	        }

	    } catch (Exception e) {
	        log.setStatus("FAILURE");
	        log.setErrorMessage(e.getMessage());
	        response.setMessage("Failed to save data");
	        response.setIsError(true);
	        response.setErrors(Collections.singletonList(new ErrorResponse("100", "API Error", e.getMessage())));
	        //partsSaveDetails.setSavedStatus("N");
	    } finally {
	        log.setResponseTime(LocalDateTime.now());
	        if(StringUtils.isNotBlank(log.getRequest())){
	        	apiTransactionLogRepo.save(log);
	        	logger.info(externalApiUrlSaveSpareparts +" ==> "+ log);
	        }
	    }

	    return response;
	}
	
	
	private SaveSparePartsRequest mapToSaveSparePartsRequestV1(SparePartsSaveDetails partsSaveDetails,
			List<DamageSectionDetails> damageDetails, InsuredVehicleInfo insuredInfo) {
		SaveSparePartsRequest request = new SaveSparePartsRequest();

	    try {
	    	SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	    	// Map fields from GarageWorkOrder to SaveSparePartsRequest
			request.setWorkOrderType(partsSaveDetails.getWorkOrderType());
			request.setWorkOrderNo(partsSaveDetails.getWorkOrderNo());
			request.setWorkOrderDate(isoDateFormat.format(partsSaveDetails.getWorkOrderDate())); 
//			request.setAccForSettlementType(partsSaveDetails.getAccountSettlementType());
			request.setAccForSettlementType("");
			request.setAccForSettlement(partsSaveDetails.getAccountSettlementName());
			request.setSparePartsDealer(partsSaveDetails.getSparePartsDealer());
			request.setGarageCode(partsSaveDetails.getGarageCode());
			request.setGarageQuotationNo(partsSaveDetails.getQuotationNo());
			request.setGarageQuotationNo(partsSaveDetails.getWorkOrderNo());
			request.setDeliveryDate(isoDateFormat.format(partsSaveDetails.getDeliveryDate()));
			request.setDeliveredTo(partsSaveDetails.getDeliveredTo());
			request.setDeliveredId(partsSaveDetails.getGarageCode());
			request.setSubrogation(partsSaveDetails.getSubrogation());
			request.setJointOrder(partsSaveDetails.getJointOrder());
			request.setTotalLoss(partsSaveDetails.getTotalLoss().toString());
			request.setTotalLoss("N");
			request.setTotalLossType("");
			request.setRemarks(partsSaveDetails.getRemarks());
			request.setLpoId(partsSaveDetails.getLpoId());
			request.setClaimNo(partsSaveDetails.getClaimNo());
			request.setVehId(insuredInfo.getVehId());
			request.setClcpId(insuredInfo.getClcpId());

			List<VehicleDamageDetailRequest> vehicleDamageDetails = new ArrayList<>();

			for (DamageSectionDetails detail : damageDetails) {
			    VehicleDamageDetailRequest damageRequest = new VehicleDamageDetailRequest();
			    String directionCode = dropDownServiceImpl.getItemCodeByItemValue(detail.getDamageDirection(),"DAMAGE_DIRECTION");
			    String partCode = dropDownServiceImpl.getbodyPartCodeByValue(detail.getDamagePart());
			    VcSparePartsDetails spare = sparePartsDetailsRepo.findByClaimNumberAndQuotationNoAndDamageSnoAndGarageId(partsSaveDetails.getClaimNo(),partsSaveDetails.getQuotationNo(),String.valueOf(detail.getDamageSno()),detail.getGarageLoginId());
			   if(spare!=null) {
				   damageRequest.setDiscount(spare.getDiscountPercentage()!=null ?spare.getDiscountPercentage().toString():"0");
				    damageRequest.setDiscountAmt(spare.getDiscountAmount()!=null ?spare.getDiscountAmount().toString():"0");
				    damageRequest.setDamageTyp(spare.getDamageType());
				    damageRequest.setDeprect(spare.getDepreciation()!=null ?spare.getDepreciation().toString():"0");
				    damageRequest.setDeprectTyp(spare.getDepreciationType());
				    damageRequest.setOriginalDisc(spare.getOriginalDiscount()!=null ?spare.getOriginalDiscount().toString():"");
				    damageRequest.setPartAccident("");
				    damageRequest.setReffStatus(spare.getReferralStatus()!=null ?spare.getReferralStatus().toString():"A");
				    damageRequest.setRemarks(spare.getRemarks());
				    damageRequest.setRepairLabour(spare.getRepairLabour()!=null ?spare.getRepairLabour().toString():"0");
				    damageRequest.setRepairLabourDeduct(spare.getRepairLabourDeductible()!=null ?spare.getRepairLabourDeductible().toString():"0");
				    damageRequest.setRepairLabourDisc(spare.getRepairLabourDiscount()!=null ?spare.getRepairLabourDiscount().toString():"0");
				    damageRequest.setRepairLabourDiscAmt(spare.getRepairLabourDiscountAmount()!=null ?spare.getRepairLabourDiscountAmount().toString():"0");
				    damageRequest.setReplaceCostDed(spare.getReplacementCostDeductible()!=null ?spare.getReplacementCostDeductible().toString():"0");
				    damageRequest.setSparePartTyp(spare.getSparePartType()!=null ?spare.getSparePartType().toString():"0");
				    damageRequest.setTotalAmtRepLab(spare.getTotalAmountRepairLabour()!=null ?spare.getTotalAmountRepairLabour().toString():"0");
				    damageRequest.setTotal(spare.getTotalCost()!=null ?spare.getTotalCost().toString():"0");
			   }
			   else {
				   damageRequest.setDiscount("0");
				    damageRequest.setDiscountAmt("0");
				    damageRequest.setDamageTyp("");
				    damageRequest.setDeprect("0");
				    damageRequest.setDeprectTyp("");
				    damageRequest.setOriginalDisc("");
				    damageRequest.setPartAccident("");
				    damageRequest.setReffStatus("A");
				    damageRequest.setRemarks("");
				    damageRequest.setRepairLabour("0");
				    damageRequest.setRepairLabourDeduct("0");
				    damageRequest.setRepairLabourDisc("0");
				    damageRequest.setRepairLabourDiscAmt("0");
				    damageRequest.setReplaceCostDed("0");
				    damageRequest.setSparePartTyp("0");
				    damageRequest.setTotalAmtRepLab("0");
				    damageRequest.setTotal("0");
			   }
			    BigDecimal total = BigDecimal.ZERO;
			    damageRequest.setDamageDirection(directionCode);
			    damageRequest.setPartyType(partCode);
			    damageRequest.setReplaceOrRepair(detail.getRepairReplace());
			    
			    damageRequest.setDeductiblePer("0.00");

			    if("REPLACE".equalsIgnoreCase(detail.getRepairReplace())) {
			    	damageRequest.setUnitPrice(detail.getGaragePrice()!=null?detail.getGaragePrice().toString():"");
			    	BigDecimal unitPrice = detail.getGaragePrice() != null ? detail.getGaragePrice() : BigDecimal.ZERO;
				    int noOfParts = detail.getNoOfParts() > 0 ? detail.getNoOfParts() : 0;
				    BigDecimal replacementCharge = detail.getReplaceCost() != null ? detail.getReplaceCost() : BigDecimal.ZERO;

				    try {
				        total = unitPrice.multiply(BigDecimal.valueOf(noOfParts)).add(replacementCharge);
				    } catch (Exception e) {
				        // Log the error and default total to zero
				        System.err.println("Error calculating total: " + e.getMessage());
				        total = BigDecimal.ZERO;
				    }
				    damageRequest.setNoUnits(detail.getNoOfParts()!=null ?detail.getNoOfParts().toString():"0");
//				    damageRequest.setTotal(String.valueOf(total));
			    }else {
				   
			    	damageRequest.setUnitPrice("");
			    	 BigDecimal replacementCharge = detail.getReplaceCost() != null ? detail.getReplaceCost() : BigDecimal.ZERO;
			    	total = replacementCharge;
			    	BigDecimal dedudct = detail.getLabourCostDeduct() != null ? detail.getLabourCostDeduct() : BigDecimal.ZERO;
			    	total = replacementCharge.subtract(dedudct);
			    	damageRequest.setNoUnits("");
			    	damageRequest.setTotal(partsSaveDetails.getRepairLabour() != null ? partsSaveDetails.getRepairLabour().toString() : "0");
			    }
			    
			    
			    damageRequest.setReplacementCharge(detail.getReplaceCost()!=null ?detail.getReplaceCost().toString():"0");
			    

			    vehicleDamageDetails.add(damageRequest);
			}

			request.setVehicleDamageDetails(vehicleDamageDetails);


			// Populate metadata (assumed to be populated elsewhere in your code)
			SaveSparePartsRequestMetaData metaData = new SaveSparePartsRequestMetaData();
			metaData.setRequestOrigin("API"); 
			metaData.setCurrentBranch("2222");
			metaData.setOriginBranch("2222");
			metaData.setUserName("09988877772");
//			metaData.setIpAddress(workOrder.getIpAddress());
			metaData.setRequestGeneratedDateTime(isoDateFormat.format(new Date()));
//			metaData.setConsumerTrackingID(UUID.randomUUID().toString()); // example, replace as necessary
			request.setRequestMetaData(metaData);
			
			request.setReplacementCost(partsSaveDetails.getReplacementCost() != null ? partsSaveDetails.getReplacementCost().toString() : "0");
	
			request.setReplacementCostDeductible(partsSaveDetails.getReplacementCostDeductible() != null ? partsSaveDetails.getReplacementCostDeductible().toString() : "0");
			request.setSparePartDepreciation(partsSaveDetails.getSparePartDepreciation() != null ? partsSaveDetails.getSparePartDepreciation().toString() : "0");
			request.setDiscountonSpareParts(partsSaveDetails.getDiscountOnSpareParts() != null ? partsSaveDetails.getDiscountOnSpareParts().toString() : "0");
			request.setTotalAmountReplacement(partsSaveDetails.getTotalAmountReplacement() != null ? partsSaveDetails.getTotalAmountReplacement().toString() : "0");
			request.setRepairLabour(partsSaveDetails.getRepairLabour() != null ? partsSaveDetails.getRepairLabour().toString() : "0");
			request.setRepairLabourDeductible(partsSaveDetails.getRepairLabourDeductible() != null ? partsSaveDetails.getRepairLabourDeductible().toString() : "0");
			request.setRepairLabourDiscountAmount(partsSaveDetails.getRepairLabourDiscountAmount() != null ? partsSaveDetails.getRepairLabourDiscountAmount().toString() : "0");
			request.setTotalAmountRepairLabour(partsSaveDetails.getTotalAmountRepairLabour() != null ? partsSaveDetails.getTotalAmountRepairLabour().toString() : "0");
			request.setNetAmount(partsSaveDetails.getNetAmount() != null ? partsSaveDetails.getNetAmount().toString() : "0");
			request.setUnkownAccidentDeduction(partsSaveDetails.getUnknownAccidentDeduction() != null ? partsSaveDetails.getUnknownAccidentDeduction().toString() : "0");
			request.setAmounttobeRecovered(partsSaveDetails.getAmountToBeRecovered() != null ? partsSaveDetails.getAmountToBeRecovered().toString() : "0");
			request.setTotalafterDeductions(partsSaveDetails.getTotalAfterDeductions() != null ? partsSaveDetails.getTotalAfterDeductions().toString() : "0");
			request.setVatRatePer(partsSaveDetails.getVatRatePercentage() != null ? partsSaveDetails.getVatRatePercentage().toString() : "0");
			request.setVatRate(partsSaveDetails.getVatRate() != null ? partsSaveDetails.getVatRate().toString() : "0");
			request.setVatAmount(partsSaveDetails.getVatAmount() != null ? partsSaveDetails.getVatAmount().toString() : "0");
			request.setTotalWithVAT(partsSaveDetails.getTotalWithVat() != null ? partsSaveDetails.getTotalWithVat().toString() : "0");

			
		} catch (Exception e) {
			e.printStackTrace();
		}

	    return request;
	}

	@Override
	public CommonResponse getSavedSpareParts(SaveSparePartsDTO requestPayload) {
	    CommonResponse comResponse = new CommonResponse();
	    
	    try {
	        // Fetch insured vehicle info based on company and surveyor ID
	        List<InsuredVehicleInfo> insuredList = repository.findByCompanyIdAndSurveyorId(
	                Integer.valueOf(requestPayload.getCompanyId()), requestPayload.getSurveyorLoginId());

	        // Extract claim numbers from the insured vehicle list
	        List<String> claimNumbers = insuredList.stream()
	                .map(InsuredVehicleInfo::getClaimNo)
	                .collect(Collectors.toList());

	        // Retrieve saved spare parts details
	        List<SparePartsSaveDetails> spareSavedList = SparePartsSaveDetailsRepo.findByClaimNoIn(claimNumbers);

	        // Check if there are saved spare parts
	        if (!spareSavedList.isEmpty()) {
	            List<GetAllQuoteResponse> responseList = new ArrayList<>();

	            for (SparePartsSaveDetails spareSaved : spareSavedList) {
	                GetAllQuoteResponse response = new GetAllQuoteResponse();
	                response.setClaimNo(spareSaved.getClaimNo());
	                response.setWorkOrderNo(spareSaved.getWorkOrderNo());
	                response.setWorkOrderType(spareSaved.getWorkOrderType());
	                response.setWorkOrderDate(spareSaved.getWorkOrderDate());
	                response.setSettlementType(spareSaved.getAccountSettlementType());
	                response.setSettlementTo(spareSaved.getAccountSettlementName());
	                response.setGarageId(String.valueOf(spareSaved.getGarageCode()));
	                response.setQuotationNo(spareSaved.getQuotationNo());
	                response.setDeliveryDate(spareSaved.getDeliveryDate());
	                response.setJointOrderYn(spareSaved.getJointOrder());
	                response.setSubrogationYn(spareSaved.getSubrogation());
	                response.setTotalLoss(String.valueOf(spareSaved.getTotalLoss()));
	                response.setLossType(spareSaved.getTotalLossType());
	                response.setRemarks(spareSaved.getRemarks());
	                response.setSavedStatus(spareSaved.getSavedStatus());
	                response.setSparepartsDealerId(Optional.ofNullable(spareSaved.getSparePartsDealer()).map(String::valueOf).orElse(""));

	                // Financial details with default values
	                response.setReplacementCost(Optional.ofNullable(spareSaved.getReplacementCost()).map(String::valueOf).orElse("0.00"));
	                response.setReplacementCostDeductible(Optional.ofNullable(spareSaved.getReplacementCostDeductible()).map(String::valueOf).orElse("0.00"));
	                response.setSparePartDepreciation(Optional.ofNullable(spareSaved.getSparePartDepreciation()).map(String::valueOf).orElse("0.00"));
	                response.setDiscountOnSpareParts(Optional.ofNullable(spareSaved.getDiscountOnSpareParts()).map(String::valueOf).orElse("0.00"));
	                response.setTotalAmountReplacement(Optional.ofNullable(spareSaved.getTotalAmountReplacement()).map(String::valueOf).orElse("0.00"));
	                response.setRepairLabour(Optional.ofNullable(spareSaved.getRepairLabour()).map(String::valueOf).orElse("0.00"));
	                response.setRepairLabourDeductible(Optional.ofNullable(spareSaved.getRepairLabourDeductible()).map(String::valueOf).orElse("0.00"));
	                response.setRepairLabourDiscountAmount(Optional.ofNullable(spareSaved.getRepairLabourDiscountAmount()).map(String::valueOf).orElse("0.00"));
	                response.setTotalAmountRepairLabour(Optional.ofNullable(spareSaved.getTotalAmountRepairLabour()).map(String::valueOf).orElse("0.00"));
	                response.setNetAmount(Optional.ofNullable(spareSaved.getNetAmount()).map(String::valueOf).orElse("0.00"));
	                response.setUnknownAccidentDeduction(Optional.ofNullable(spareSaved.getUnknownAccidentDeduction()).map(String::valueOf).orElse("0.00"));
	                response.setAmountToBeRecovered(Optional.ofNullable(spareSaved.getAmountToBeRecovered()).map(String::valueOf).orElse("0.00"));
	                response.setTotalAfterDeductions(Optional.ofNullable(spareSaved.getTotalAfterDeductions()).map(String::valueOf).orElse("0.00"));
	                response.setVatRatePer(Optional.ofNullable(spareSaved.getVatRatePercentage()).map(String::valueOf).orElse("0.00"));
	                response.setVatRate(Optional.ofNullable(spareSaved.getVatRate()).map(String::valueOf).orElse("0.00"));
	                response.setVatAmount(Optional.ofNullable(spareSaved.getVatAmount()).map(String::valueOf).orElse("0.00"));
	                response.setTotalWithVAT(Optional.ofNullable(spareSaved.getTotalWithVat()).map(String::valueOf).orElse("0.00"));

	                responseList.add(response);
	            }

	            comResponse.setErrors(Collections.emptyList());
	            comResponse.setMessage("Success");
	            comResponse.setResponse(responseList);
	        } else {
	            comResponse.setErrors(Collections.emptyList());
	            comResponse.setMessage("Failed");
	            comResponse.setResponse(Collections.emptyList());
	        }
	    } catch (Exception e) {
	        // Proper logging instead of just printing the stack trace
	        System.err.println("Error fetching saved spare parts: " + e.getMessage());
	        e.printStackTrace();
	        
	        comResponse.setErrors(Collections.singletonList("An unexpected error occurred."));
	        comResponse.setMessage("Error");
	        comResponse.setResponse(Collections.emptyList());
	    }

	    return comResponse;
	}

	
	@Override
	public CommonResponse getSavedGarageSpareParts(SaveSparePartsDTO requestPayload) {
		CommonResponse comResponse = new CommonResponse(); 
        try {
        	LoginMaster loginMaster = loginRepo.findByLoginId(requestPayload.getGarageLoginId());
        	//List<SparePartsSaveDetails> spareSavedList = SparePartsSaveDetailsRepo.findByGarageCode(loginMaster.getCoreAppCode());
        	List<SparePartsSaveDetails> spareSavedList = SparePartsSaveDetailsRepo.findByGarageCodeOrderByEntryDateDesc(loginMaster.getCoreAppCode());
			
			if(spareSavedList != null && spareSavedList.size()>0) {
		    	List<GetAllQuoteResponse> res = new ArrayList<>();
				for(SparePartsSaveDetails spareSaved : spareSavedList ) {
					 GetAllQuoteResponse response = new GetAllQuoteResponse();
			         response.setClaimNo(spareSaved.getClaimNo());
			         response.setWorkOrderNo(spareSaved.getWorkOrderNo());
			         response.setWorkOrderType(spareSaved.getWorkOrderType());
			         response.setWorkOrderDate(spareSaved.getWorkOrderDate());
			         response.setSettlementType(spareSaved.getAccountSettlementType());
			         response.setSettlementTo(spareSaved.getAccountSettlementName());
			         response.setGarageId(spareSaved.getGarageCode().toString());
			         response.setQuotationNo(spareSaved.getQuotationNo());
			         response.setDeliveryDate(spareSaved.getDeliveryDate());
			         response.setJointOrderYn(spareSaved.getJointOrder());
			         response.setSubrogationYn(spareSaved.getSubrogation());
			         response.setTotalLoss(spareSaved.getTotalLoss().toString());
			         response.setLossType(spareSaved.getTotalLossType());
			         response.setRemarks(spareSaved.getRemarks());
			         response.setSavedStatus(spareSaved.getSavedStatus());
			         response.setSparepartsDealerId(Optional.ofNullable(spareSaved.getSparePartsDealer()).map(String ::valueOf).orElse(""));	
			         
						response.setReplacementCost(
								spareSaved.getReplacementCost() != null ? spareSaved.getReplacementCost().toString()
										: "0.00");
						response.setReplacementCostDeductible(spareSaved.getReplacementCostDeductible() != null
								? spareSaved.getReplacementCostDeductible().toString()
								: "0.00");
						response.setSparePartDepreciation(spareSaved.getSparePartDepreciation() != null
								? spareSaved.getSparePartDepreciation().toString()
								: "0.00");
						response.setDiscountOnSpareParts(spareSaved.getDiscountOnSpareParts() != null
								? spareSaved.getDiscountOnSpareParts().toString()
								: "0.00");
						response.setTotalAmountReplacement(spareSaved.getTotalAmountReplacement() != null
								? spareSaved.getTotalAmountReplacement().toString()
								: "0.00");
						response.setRepairLabour(
								spareSaved.getRepairLabour() != null ? spareSaved.getRepairLabour().toString()
										: "0.00");
						response.setRepairLabourDeductible(spareSaved.getRepairLabourDeductible() != null
								? spareSaved.getRepairLabourDeductible().toString()
								: "0.00");
						response.setRepairLabourDiscountAmount(spareSaved.getRepairLabourDiscountAmount() != null
								? spareSaved.getRepairLabourDiscountAmount().toString()
								: "0.00");
						response.setTotalAmountRepairLabour(spareSaved.getTotalAmountRepairLabour() != null
								? spareSaved.getTotalAmountRepairLabour().toString()
								: "0.00");
						response.setNetAmount(
								spareSaved.getNetAmount() != null ? spareSaved.getNetAmount().toString() : "0.00");
						response.setUnknownAccidentDeduction(spareSaved.getUnknownAccidentDeduction() != null
								? spareSaved.getUnknownAccidentDeduction().toString()
								: "0.00");
						response.setAmountToBeRecovered(spareSaved.getAmountToBeRecovered() != null
								? spareSaved.getAmountToBeRecovered().toString()
								: "0.00");
						response.setTotalAfterDeductions(spareSaved.getTotalAfterDeductions() != null
								? spareSaved.getTotalAfterDeductions().toString()
								: "0.00");
						response.setVatRatePer(
								spareSaved.getVatRatePercentage() != null ? spareSaved.getVatRatePercentage().toString()
										: "0.00");
						response.setVatRate(
								spareSaved.getVatRate() != null ? spareSaved.getVatRate().toString() : "0.00");
						response.setVatAmount(
								spareSaved.getVatAmount() != null ? spareSaved.getVatAmount().toString() : "0.00");
						response.setTotalWithVAT(
								spareSaved.getTotalWithVat() != null ? spareSaved.getTotalWithVat().toString()
										: "0.00");

			         
			         
			         res.add(response);
				}
				
				comResponse.setErrors(Collections.emptyList());
				comResponse.setMessage("Success");
				comResponse.setResponse(res);
			
			}else {
				
				comResponse.setErrors(Collections.emptyList());
				comResponse.setMessage("Failed");
				comResponse.setResponse(Collections.emptyList());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
                   
         return comResponse;
	}
	
	public CommonResponse getGarageClaimList(ExternalVehicleGarageViewRequest request) {

	    CommonResponse response = new CommonResponse();
	    ApiTransactionLog log = new ApiTransactionLog();;
	    log.setRequestTime(LocalDateTime.now());
	    log.setEntryDate(new Date());
	    log.setEndpoint(externalApiUrlGarageList);

	    try {

	    	GarageClaimListDto dto = new GarageClaimListDto();
	    	dto.setCategoryId(request.getCategoryId());
	    	dto.setPartyId(request.getPartyId());
	    	dto.setProdId(request.getProdId());
	        
	        // Authenticate and retrieve JWT token
	        String jwtToken = authenticateUserCall();

	        // Create headers and add JWT token
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization", "Bearer " + jwtToken);
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        // Convert DTO to JSON for request body and add headers
	        String requestBody = objectMapper.writeValueAsString(dto);
	        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
	        log.setRequest(requestBody);
	        
	     // Configure SSL Trust Managers (if necessary)
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
	        
	        // Send request to external API
	        ResponseEntity<String> apiResponse = restTemplate.postForEntity(log.getEndpoint(), entity, String.class);
	        log.setResponse(apiResponse.getBody());
	        log.setStatus("SUCCESS");

	        // Parse response into ExternalApiResponse object
	        GarageClaimListResponseDTO externalApiResponse = objectMapper.readValue(apiResponse.getBody(), GarageClaimListResponseDTO.class);

	        // Process response based on external API success status
	        if (externalApiResponse.isHasError()) {
	            response.setMessage(externalApiResponse.getMessage());
	            //response.setResponse(externalApiResponse);
	            response.setIsError(true);
	        } else {
	            response.setMessage(externalApiResponse.getMessage());
	            response.setIsError(false);
	            response.setResponse(externalApiResponse.getDataset());
	        }

	    } catch (Exception e) {
	        log.setStatus("FAILURE");
	        log.setErrorMessage(e.getMessage());
	        response.setMessage("Failed to get data");
	        response.setIsError(true);
	        response.setErrors(Collections.singletonList(new ErrorResponse("100", "API Error", e.getMessage())));
	    } finally {
	        log.setResponseTime(LocalDateTime.now());
	        //apiTransactionLogRepo.save(log);
	        logger.info(externalApiUrlGarageList +" ==> "+ log);
	    }

	    return response;
	}

	@Override
	public CommonResponse listClaimantCoverages(ClaimentCoverageRequest request) {
		CommonResponse response = new CommonResponse();
	    ApiTransactionLog log = new ApiTransactionLog();
	    log.setRequestTime(LocalDateTime.now());
	    log.setEntryDate(new Date());
	    log.setEndpoint(listClaimantCoverages);

	    try {

	    	ClaimentCoverageRequestDTO dto = new ClaimentCoverageRequestDTO();
	    	Optional<InsuredVehicleInfo> optional = insuredVehicleInfoRepo.findByClaimNoAndGarageId(request.getClaimNo(),request.getGarageId());
			if (optional.isPresent()) {
				InsuredVehicleInfo insuredInfo = optional.get();
				dto.setPolicyNo(insuredInfo.getPolicyNo());
				dto.setRiskId(insuredInfo.getVehId());
				dto.setSgsId(insuredInfo.getFnolSgsId());
				dto.setClcpClfSgsId(insuredInfo.getClcpId());
			}
	    	
	        // Authenticate and retrieve JWT token
	        String jwtToken = authenticateUserCall();

	        // Create headers and add JWT token
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization", "Bearer " + jwtToken);
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        // Convert DTO to JSON for request body and add headers
	        String requestBody = objectMapper.writeValueAsString(dto);
	        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
	        log.setRequest(requestBody);
	        
	     // Configure SSL Trust Managers (if necessary)
            TrustManager[] trustAllCerts = new TrustManager[] {
                new X509TrustManager() {
                    public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                        return null;
                    }

                    public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }

                    public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
                    }
                }
            };

            SSLContext sc = SSLContext.getInstance("SSL");
            sc.init(null, trustAllCerts, new java.security.SecureRandom());
            HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
            HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);
	        
	        // Send request to external API
	        ResponseEntity<String> apiResponse = restTemplate.postForEntity(log.getEndpoint(), entity, String.class);
	        log.setResponse(apiResponse.getBody());
	        log.setStatus("SUCCESS");

	        // Parse response into ExternalApiResponse object
	        ClaimentCoverageResponseDTO externalApiResponse = objectMapper.readValue(apiResponse.getBody(), ClaimentCoverageResponseDTO.class);

	        // Initialize ClaimentCoverageResponse to map the data
	        ClaimentCoverageResponse res = new ClaimentCoverageResponse();

	        if (externalApiResponse.isHasError()) {
	            response.setMessage(externalApiResponse.getMessage());
	            response.setIsError(true);

	            // Map error-specific fields if needed
	            res.setHasError(externalApiResponse.isHasError());
	            res.setStatus(externalApiResponse.getStatus());
	            res.setMessage(externalApiResponse.getMessage());
	        } else {
	            // Map all fields from DTO to the response
	            res.setHasError(externalApiResponse.isHasError());
	            res.setStatus(externalApiResponse.getStatus());
	            res.setData(externalApiResponse.getData());

	            // Map Dataset
	            ClaimentCoverageResponse.Dataset dataset = new ClaimentCoverageResponse.Dataset();
	            dataset.setClaimantList(externalApiResponse.getDataset().getClaimantList());
	            dataset.setCoveragesList(externalApiResponse.getDataset().getCoveragesList());
	            res.setDataset(dataset);

	            // Set success message
	            response.setMessage(externalApiResponse.getMessage());
	            response.setIsError(false);
	            response.setResponse(res);
	        }


	    } catch (Exception e) {
	        log.setStatus("FAILURE");
	        log.setErrorMessage(e.getMessage());
	        response.setMessage("Failed to get data");
	        response.setIsError(true);
	        response.setErrors(Collections.singletonList(new ErrorResponse("100", "API Error", e.getMessage())));
	    } finally {
	        log.setResponseTime(LocalDateTime.now());
	        //apiTransactionLogRepo.save(log);
	        logger.info(listClaimantCoverages +" ==> "+ log);
	    }

	    return response;

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


	@Override
	public CommonResponse getPolicyDetails(GetClaimRequest req) {
	    CommonResponse response = new CommonResponse();
	    ApiTransactionLog log = new ApiTransactionLog();
	    log.setSno(apiTransactionLogRepo.findMaxSno() + 1);
	    log.setRequestTime(LocalDateTime.now());
	    log.setEntryDate(new Date());

	    try {
	        // Fetch company ID from request payload
	        String companyId = String.valueOf(req.getCompanyId());

	        // Fetch API URL from the database
	        String apiType = "GET_POLICY";
	        Optional<ApiIntegMaster> apiConfig = apiIntegMasterRepository
	                .findByCompanyIdAndApiTypeAndStatus(companyId, apiType, "Y");

	        if (apiConfig.isEmpty() || apiConfig.get().getApiUrl() == null) {
	            response.setMessage("API URL not found for company: " + companyId);
	            response.setIsError(true);
	            return response;
	        }

	        String externalApiUrl = apiConfig.get().getApiUrl();
	        log.setEndpoint(externalApiUrl);

	        // Extract JWT token from request
	        String jwtToken = authenticateUserCall();

	        // Create headers with JWT token
	        HttpHeaders headers = new HttpHeaders();
	        headers.set("Authorization", "Bearer " + jwtToken);
	        headers.setContentType(MediaType.APPLICATION_JSON);

	        // Convert requestPayload to JSON and add headers
	        GetPolicyDetailsRequestDto dto = mapToGetPolicyDetailsRequestDto(req);
	        String requestBody = objectMapper.writeValueAsString(dto);
	        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);
	        log.setRequest(requestBody);
	        logger.info(requestBody);

	        // Send request to external API with JWT in Authorization header
	        ResponseEntity<String> apiResponse = restTemplate.postForEntity(log.getEndpoint(), entity, String.class);
	        log.setResponse(apiResponse.getBody());
	        log.setStatus("SUCCESS");

	        // Parse the raw response into ExternalApiResponse object
	        PolicyResponseDTO externalApiResponse = objectMapper.readValue(apiResponse.getBody(), PolicyResponseDTO.class);

	        if (externalApiResponse.isHasError()) {
	            response.setMessage(externalApiResponse.getMessage());
	            response.setResponse(Collections.emptyMap());
	            response.setIsError(true);
	        } else {
	            // ✅ Convert API response to UI-friendly response
	            PolicyResponseUI policyResponseUI = mapToUIResponse(externalApiResponse);
	            response.setMessage("Data saved successfully");
	            response.setIsError(false);
	            response.setResponse(policyResponseUI);
	        }

	    } catch (Exception e) {
	        log.setStatus("FAILURE");
	        log.setErrorMessage(e.getMessage());
	        response.setMessage("Failed to save data");
	        response.setIsError(true);
	        response.setErrors(Collections.singletonList(new ErrorResponse("100", "API Error", e.getMessage())));
	    } finally {
	        log.setResponseTime(LocalDateTime.now());
	        apiTransactionLogRepo.save(log);
	        logger.info(log.getEndpoint() + " ==> " + log);
	    }

	    return response;
	}



	private GetPolicyDetailsRequestDto mapToGetPolicyDetailsRequestDto(GetClaimRequest request) {
	    SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

	    GetPolicyDetailsRequestDto dto = new GetPolicyDetailsRequestDto();

	    // Hardcoded values
	    dto.setPolicyNo(request.getPolicyNo());
	    dto.setProductId("1100");
	    dto.setTransactionType("P");
	    dto.setCobCode("10");

	    GetPolicyDetailsRequestDto.RequestMetaDataDTO metaData = new GetPolicyDetailsRequestDto.RequestMetaDataDTO();

	    metaData.setRequestOrigin("API");
	    metaData.setCurrentBranch("2222");
	    metaData.setOriginBranch("2222");
	    metaData.setUserName("eb7372@eagle");
	    metaData.setIpAddress("");
	    metaData.setRequestGeneratedDateTime(isoDateFormat.format(new Date()));
	    metaData.setConsumerTrackingID("101");

	    dto.setRequestMetaData(metaData);

	    return dto;
	}


	private PolicyResponseUI mapToUIResponse(PolicyResponseDTO externalApiResponse) {
	    PolicyResponseUI policyResponseUI = new PolicyResponseUI();
	    policyResponseUI.setPolicyNumber(externalApiResponse.getData().getPolicyNumber());
	    policyResponseUI.setInsuredId(externalApiResponse.getData().getInsuredId());

	    // ✅ Format Dates from `yyyy-MM-dd'T'HH:mm:ss` to `dd/MM/yyyy`
	    DateTimeFormatter inputFormatter = new DateTimeFormatterBuilder()
	            .appendPattern("yyyy-MM-dd'T'HH:mm")  // Handles "yyyy-MM-dd'T'HH:mm"
	            .optionalStart()
	            .appendPattern(":ss")  // Optionally handle seconds if present
	            .optionalEnd()
	            .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0) // Defaults seconds to 0 if missing
	            .toFormatter();
	    //DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
	    DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

	    try {
			LocalDate policyFromDate = LocalDate.parse(externalApiResponse.getData().getPolicyFromDate(), inputFormatter);
			LocalDate policyToDate = LocalDate.parse(externalApiResponse.getData().getPolicyToDate(), inputFormatter);

			policyResponseUI.setPolicyFromDate(policyFromDate.format(outputFormatter));
			policyResponseUI.setPolicyToDate(policyToDate.format(outputFormatter));
		} catch (Exception e) {

		}

	    // ✅ Map Customer Details
	    PolicyResponseUI.CustomerDetailsUI customerDetails = new PolicyResponseUI.CustomerDetailsUI();
	    List<PolicyResponseUI.PolicyHolderUI> policyHolders = externalApiResponse.getData()
	            .getCustomerDetails()
	            .getPolicyHolder()
	            .stream()
	            .map(holder -> {
	                PolicyResponseUI.PolicyHolderUI uiHolder = new PolicyResponseUI.PolicyHolderUI();
	                uiHolder.setEngFullName(holder.getEngFullName());
	                policyResponseUI.setEngFullName(holder.getEngFullName());
	                return uiHolder;
	            })
	            .collect(Collectors.toList());

	    customerDetails.setPolicyHolder(policyHolders);
	    policyResponseUI.setCustomerDetails(customerDetails);

	    return policyResponseUI;
	}




}
