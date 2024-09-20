package com.maan.veh.claim.serviceimpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
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
import com.maan.veh.claim.entity.ApiTransactionLog;
import com.maan.veh.claim.repository.ApiTransactionLogRepository;
import com.maan.veh.claim.request.ClaimIntimationDocumentDetails;
import com.maan.veh.claim.request.ClaimIntimationThirdPartyInfo;
import com.maan.veh.claim.request.ClaimTransactionRequest;
import com.maan.veh.claim.request.FnolRequest;
import com.maan.veh.claim.request.LoginRequest;
import com.maan.veh.claim.request.SaveClaimRequest;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.ErrorList;
import com.maan.veh.claim.service.ExternalApiService;

@Service
public class ExternalApiServiceImpl implements ExternalApiService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ApiTransactionLogRepository apiTransactionLogRepo;
    
    @Value("${external.api.url.createfnol}")  
    private String externalApiUrlCreatefnol;
    
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
    public CommonResponse saveClaimIntimation(SaveClaimRequest requestPayload) {
        CommonResponse response = new CommonResponse();
        ApiTransactionLog log = new ApiTransactionLog();
        log.setRequestTime(LocalDateTime.now());
        log.setEntryDate(new Date());
        log.setEndpoint(externalApiUrlCreatefnol);

        // Validate requestPayload
        List<ErrorList> errors = validation.validateClaimIntemationDetails(requestPayload);
        if (!errors.isEmpty()) {
            response.setErrors(errors);
            response.setMessage("Validation failed");
            response.setResponse(Collections.emptyMap());
            response.setIsError(true);
            return response;
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

            // Send request to external API with JWT in Authorization header
            ResponseEntity<String> apiResponse = restTemplate.postForEntity(log.getEndpoint(), entity, String.class);
            log.setResponse(apiResponse.getBody());
            log.setStatus("SUCCESS");

            response.setMessage("Data saved successfully");
            response.setIsError(false);
            response.setResponse(apiResponse.getBody());

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

	        // Send request to external authentication API
	        ResponseEntity<String> apiResponse = restTemplate.postForEntity(log.getEndpoint(), requestBody, String.class);
	        log.setResponse(apiResponse.getBody());
	        log.setStatus("SUCCESS");

	        // Parse API response to Map
	        response.setMessage("Authentication successful");
	        response.setIsError(false);
	        response.setResponse(apiResponse.getBody());

	    } catch (Exception e) {
	        log.setStatus("FAILURE");
	        log.setErrorMessage(e.getMessage());
	        response.setMessage("Authentication failed");
	        response.setIsError(true);
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

	    SaveClaimRequestDTO dto = new SaveClaimRequestDTO();
	    
	    // Map simple fields
	    dto.setLanguageCode(request.getLanguageCode());
	    dto.setPolicyNo(request.getPolicyNo());
	    dto.setInsuredId(request.getInsuredId());
	    dto.setLossDate(request.getLossDate());
	    dto.setIntimatedDate(request.getIntimatedDate());
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
	        metaData.setRequestGeneratedDateTime(request.getRequestMetaData().getRequestGeneratedDateTime());
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

	        // Send request to external authentication API
	        ResponseEntity<String> apiResponse = restTemplate.postForEntity(log.getEndpoint(), requestBody, String.class);
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


}
