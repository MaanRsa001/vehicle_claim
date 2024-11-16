package com.maan.veh.claim.serviceimpl;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.maan.veh.claim.dto.ClaimIntimationDTOAttachmentDetails;
import com.maan.veh.claim.dto.ClaimIntimationDTODocumentDetails;
import com.maan.veh.claim.dto.ClaimIntimationDTODriver;
import com.maan.veh.claim.dto.ClaimIntimationDTORequestMetaData;
import com.maan.veh.claim.dto.ClaimIntimationDTOThirdPartyInfo;
import com.maan.veh.claim.dto.ClaimTransactionRequestDTO;
import com.maan.veh.claim.dto.ClaimTransactionRequestDTOMetaData;
import com.maan.veh.claim.dto.FnolRequestDTO;
import com.maan.veh.claim.dto.FnolRequestDTOMetaData;
import com.maan.veh.claim.dto.SaveClaimRequestDTO;
import com.maan.veh.claim.dto.SaveSparePartsDTO;
import com.maan.veh.claim.entity.ApiTransactionLog;
import com.maan.veh.claim.entity.ClaimIntimationDetails;
import com.maan.veh.claim.entity.ClaimIntimationDetailsId;
import com.maan.veh.claim.entity.DamageSectionDetails;
import com.maan.veh.claim.entity.GarageWorkOrder;
import com.maan.veh.claim.external.ErrorDetail;
import com.maan.veh.claim.external.ErrorResponse;
import com.maan.veh.claim.external.ExternalApiResponse;
import com.maan.veh.claim.repository.ApiTransactionLogRepository;
import com.maan.veh.claim.repository.ClaimIntimationDetailsRepository;
import com.maan.veh.claim.repository.DamageSectionDetailsRepository;
import com.maan.veh.claim.repository.GarageWorkOrderRepository;
import com.maan.veh.claim.request.ClaimIntimationDocumentDetails;
import com.maan.veh.claim.request.ClaimIntimationRequestMetaData;
import com.maan.veh.claim.request.ClaimIntimationThirdPartyInfo;
import com.maan.veh.claim.request.ClaimListRequest;
import com.maan.veh.claim.request.ClaimListRequestDTO;
import com.maan.veh.claim.request.ClaimTransactionRequest;
import com.maan.veh.claim.request.FnolRequest;
import com.maan.veh.claim.request.LoginRequest;
import com.maan.veh.claim.request.SaveClaimRequest;
import com.maan.veh.claim.request.SaveSparePartsRequest;
import com.maan.veh.claim.request.SaveSparePartsRequestMetaData;
import com.maan.veh.claim.request.VehicleDamageDetailRequest;
import com.maan.veh.claim.response.ClaimIntimationResponse;
import com.maan.veh.claim.response.ClaimListResponse;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.ErrorList;
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
    
    @Value("${external.api.url.createfnol}")  
    private String externalApiUrlCreatefnol;
    
    @Value("${external.api.url.claimlisting}")  
    private String externalApiUrlClaimListing;
    
    @Value("${external.api.url.savespareparts}")  
    private String externalApiUrlSaveSpareparts;
    
    @Value("${external.api.url.getfnol}")  
    private String externalApiUrlGetfnol;
    
    @Value("${external.api.url.getfnolstatus}")
    private String externalApiUrlGetFnolStatus;
    
    @Value("${external.api.url.authenticate}")
    private String externalApiUrlAuthenticate;
    
    @Value("${auth.username}")
    private String apiusername;

    @Value("${auth.password}")
    private String apipassword;
    
    @Autowired
    private InputValidationUtil validation;

    @Override
    public CommonResponse createFnol(SaveClaimRequest requestPayload) {
        CommonResponse response = new CommonResponse();
        ApiTransactionLog log = new ApiTransactionLog();
        log.setRequestTime(LocalDateTime.now());
        log.setEntryDate(new Date());
        log.setEndpoint(externalApiUrlCreatefnol);

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
			Optional<ClaimIntimationDetails> optional = claimIntimationDetailsRepository.findByPolicyNo(requestPayload.getPolicyNo());
			if(optional.isPresent()){
				newData = optional.get();
			}
    // Set fields from requestPayload into newData
			newData.setPolicyNo(requestPayload.getPolicyNo());
			newData.setRequestOrigin("API");
			newData.setCurrentBranch(requestPayload.getRequestMetaData().getCurrentBranch());
			newData.setOriginBranch(requestPayload.getRequestMetaData().getOriginBranch());
			newData.setUserName(requestPayload.getRequestMetaData().getUserName());
			newData.setIpAddress(requestPayload.getRequestMetaData().getIpAddress());
			newData.setRequestGeneratedDateTime(new Date()); // Assuming this is the current date and time
			newData.setConsumerTrackingId(requestPayload.getRequestMetaData().getConsumerTrackingID());
			newData.setLanguageCode(requestPayload.getLanguageCode());
			newData.setInsuredId(requestPayload.getInsuredId());
			newData.setLossDate(requestPayload.getLossDate());
			newData.setIntimatedDate(requestPayload.getIntimatedDate());
			newData.setLossLocation(requestPayload.getLossLocation());
			newData.setNatureOfLoss(requestPayload.getNatureOfLoss());
			newData.setPoliceStation(requestPayload.getPoliceStation());
			newData.setPoliceReportNo(requestPayload.getPoliceReportNo());
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
    			Optional<ClaimIntimationDetails> optional = claimIntimationDetailsRepository.findByPolicyNo(requestPayload.getPolicyNo());
    			if(optional.isPresent()){
    				oldData = optional.get();
    				oldData.setFnolNo(externalApiResponse.getData().getFnolNo());
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
                    errorList.add(new ErrorResponse(error.getErrorCode(), error.getErrorField(), error.getErrorDescription()));
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
            response.setErrors(Collections.singletonList(new ErrorResponse("100", "General", e.getMessage()))); // General error
        } finally {
            log.setResponseTime(LocalDateTime.now());
            apiTransactionLogRepo.save(log);
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
            apiTransactionLogRepo.save(log);
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
            apiTransactionLogRepo.save(log);
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
	        apiTransactionLogRepo.save(log);
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
	    dto.setLossDate(isoDateFormat.format(request.getLossDate()));
	    dto.setIntimatedDate(isoDateFormat.format(request.getIntimatedDate()));
	    dto.setNatureOfLoss(request.getNatureOfLoss());
	    dto.setLossLocation(request.getLossLocation());
	    dto.setPoliceStation(request.getPoliceStation());
	    dto.setPoliceReportNo(request.getPoliceReportNo());
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
	        apiTransactionLogRepo.save(log);
	    }

	    return null; // Return null if authentication fails
	}
	
	public String saveOrUpdateClaimIntimation(SaveClaimRequest saveClaimRequestDTO) {
        try {
            // Check if a record already exists for the given policyNo
            Optional<ClaimIntimationDetails> existingRecord = claimIntimationDetailsRepository.findById(
                    new ClaimIntimationDetailsId(saveClaimRequestDTO.getPolicyNo())
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

            claimIntimationDetails.setLossDate(saveClaimRequestDTO.getLossDate());
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
	public CommonResponse getClaimByPolicy(String policyNo) {
	    CommonResponse response = new CommonResponse();
	    try {
	        // Fetch data by policy number
	        Optional<ClaimIntimationDetails> dataOptional = claimIntimationDetailsRepository.findByPolicyNo(policyNo);
	        
	        if (dataOptional.isPresent()) {
	            ClaimIntimationDetails data = dataOptional.get();
	            ClaimIntimationResponse formattedResponse = mapToClaimIntimationResponse(data);
	            
	            response.setResponse(formattedResponse);
	            response.setMessage("Data fetched successfully");
	            response.setIsError(false);
	        } else {
	            response.setMessage("No data found for policy number: " + policyNo);
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
	        List<ClaimIntimationDetails> dataList = claimIntimationDetailsRepository.findAll();
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

	        response.setLossDate(data.getLossDate());
	        response.setIntimatedDate(data.getIntimatedDate());
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
	            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
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
            response.setErrors(Collections.singletonList(new ErrorResponse("100", "General", e.getMessage()))); // General error
        } finally {
            log.setResponseTime(LocalDateTime.now());
            apiTransactionLogRepo.save(log);
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
	    log.setRequestTime(LocalDateTime.now());
	    log.setEntryDate(new Date());
	    log.setEndpoint(externalApiUrlSaveSpareparts);

	    try {
	        // Retrieve data from repositories
	    	GarageWorkOrder workOrder = garageWorkOrderRepo.findByClaimNoAndQuotationNo(request.getClaimNo(),request.getWorkOrderNo());
	        List<DamageSectionDetails> damageDetails = damageSectionDetailsRepo.findByClaimNo(request.getClaimNo());
	        
	        if (workOrder == null || damageDetails.isEmpty()) {
	            response.setMessage("No data found for the provided claim or work order number");
	            response.setIsError(true);
	            response.setErrors(Collections.singletonList(new ErrorResponse("404", "Data Not Found", "Data not found for the provided claim or work order number")));
	            return response;
	        }

	        // Map entities to request DTO
	        SaveSparePartsRequest dto = mapToSaveSparePartsRequest(workOrder, damageDetails);

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
	        ClaimListResponse externalApiResponse = objectMapper.readValue(apiResponse.getBody(), ClaimListResponse.class);

	        // Process response based on external API success status
	        if ("true".equalsIgnoreCase(externalApiResponse.getHasError())) {
	            response.setMessage(externalApiResponse.getMessage());
	            //response.setErrors(externalApiResponse.getErrors());
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
	        response.setErrors(Collections.singletonList(new ErrorResponse("100", "General", e.getMessage())));
	    } finally {
	        log.setResponseTime(LocalDateTime.now());
	        apiTransactionLogRepo.save(log);
	    }

	    return response;
	}
	
	private SaveSparePartsRequest mapToSaveSparePartsRequest(GarageWorkOrder workOrder, List<DamageSectionDetails> damageDetails) {
	    SaveSparePartsRequest request = new SaveSparePartsRequest();

	    try {
	    	SimpleDateFormat isoDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
	    	
			// Map fields from GarageWorkOrder to SaveSparePartsRequest
			request.setWorkOrderType(workOrder.getWorkOrderType());
			request.setWorkOrderNo(workOrder.getWorkOrderNo());
			request.setWorkOrderDate(isoDateFormat.format(workOrder.getWorkOrderDate())); 
			request.setAccForSettlementType(workOrder.getSettlementType());
			request.setAccForSettlement(workOrder.getSettlementToDesc());
			request.setSparePartsDealer(workOrder.getSparepartsDealerId());
			request.setGarageCode(workOrder.getGarageId());
			request.setGarageQuotationNo(workOrder.getQuotationNo());
			request.setDeliveryDate(isoDateFormat.format(workOrder.getDeliveryDate()));
			request.setDeliveredTo(workOrder.getGarageName());
			request.setSubrogation("Y".equalsIgnoreCase(workOrder.getSubrogationYn()));
			request.setJointOrder("Y".equalsIgnoreCase(workOrder.getJointOrderYn()));
			request.setTotalLoss(workOrder.getTotalLoss().toString());
			request.setTotalLossType(workOrder.getLossType());
			request.setRemarks(workOrder.getRemarks());
			
//			request.setReplacementCost(workOrder.getReplacementCost().toString());
//			request.setReplacementCostDeductible(workOrder.getReplacementCostDeductible().toString());
//			request.setSparePartDepreciation(workOrder.getSparePartDepreciation().toString());
//			request.setDiscountonSpareParts(workOrder.getDiscountonSpareParts().toString());
//			request.setTotalAmountReplacement(workOrder.getTotalAmountReplacement().toString());
//			request.setRepairLabour(workOrder.getRepairLabour().toString());
//			request.setRepairLabourDeductible(workOrder.getRepairLabourDeductible().toString());
//			request.setRepairLabourDiscountAmount(workOrder.getRepairLabourDiscountAmount().toString());
//			request.setTotalAmountRepairLabour(workOrder.getTotalAmountRepairLabour().toString());
//			request.setNetAmount(workOrder.getNetAmount().toString());
//			request.setUnkownAccidentDeduction(workOrder.getUnkownAccidentDeduction().toString());
//			request.setAmounttobeRecovered(workOrder.getAmounttobeRecovered().toString());
//			request.setTotalafterDeductions(workOrder.getTotalafterDeductions().toString());
//			request.setVatRatePer(workOrder.getVatRatePer().toString());
//			request.setVatRate(workOrder.getVatRate().toString());
//			request.setVatAmount(workOrder.getVatAmount().toString());
//			request.setTotalWithVAT(workOrder.getTotalWithVAT().toString());

			// Map DamageSectionDetails list to vehicleDamageDetails
			List<VehicleDamageDetailRequest> vehicleDamageDetails = damageDetails.stream().map(detail -> {
			    VehicleDamageDetailRequest damageRequest = new VehicleDamageDetailRequest();
			    damageRequest.setDamageDirection(detail.getDamageDirection());
			    damageRequest.setPartyType(detail.getDamagePart());
			    damageRequest.setReplaceOrRepair(detail.getRepairReplace());
			    damageRequest.setNoUnits(detail.getNoOfParts());
			    damageRequest.setUnitPrice(detail.getGaragePrice());
			    damageRequest.setReplacementCharge(detail.getReplaceCost());
			    damageRequest.setTotal(detail.getTotPrice());
			    return damageRequest;
			}).collect(Collectors.toList());
			request.setVehicleDamageDetails(vehicleDamageDetails);

			// Populate metadata (assumed to be populated elsewhere in your code)
			SaveSparePartsRequestMetaData metaData = new SaveSparePartsRequestMetaData();
			metaData.setRequestOrigin("API"); 
			metaData.setCurrentBranch("2222");
			metaData.setOriginBranch("2222");
			metaData.setUserName("09988877772");
			//metaData.setIpAddress(workOrder.getIpAddress());
			metaData.setRequestGeneratedDateTime(isoDateFormat.format(new Date()));
			//metaData.setConsumerTrackingID(UUID.randomUUID().toString()); // example, replace as necessary
			request.setRequestMetaData(metaData);
		} catch (Exception e) {
			e.printStackTrace();
		}

	    return request;
	}


	/**
	 * Configures SSL Trust Managers to trust all certificates.
	 * This should only be used in a development or testing environment.
	 */
	private void configureSSLTrustManagers() throws Exception {
	    TrustManager[] trustAllCerts = new TrustManager[]{
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
	}



}
