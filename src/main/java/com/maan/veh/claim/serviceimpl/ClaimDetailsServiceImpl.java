package com.maan.veh.claim.serviceimpl;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.veh.claim.entity.ClaimDetails;
import com.maan.veh.claim.repository.ClaimDetailsRepository;
import com.maan.veh.claim.request.ClaimDetailsSaveRequest;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.ErrorList;
import com.maan.veh.claim.service.ClaimDetailsService;

@Service
public class ClaimDetailsServiceImpl implements ClaimDetailsService {

	@Autowired
    private ClaimDetailsRepository repository;
	
	@Autowired
    private InputValidationUtil validation;

    @Override
    public CommonResponse saveClaimDetails(ClaimDetailsSaveRequest request) {
        CommonResponse response = new CommonResponse();
        try {
            // Validate request data
            List<ErrorList> errors = validation.validateClaimDetails(request);
            if (!errors.isEmpty()) {
                response.setErrors(errors);
                response.setMessage("Failed");
                response.setResponse(Collections.emptyMap());
                response.setIsError(true);
                return response;
            }

         // Check if claimReferenceNo is null
            if (request.getClaimReferenceNo() == null) {
                // Generate a new claim reference number
                String newClaimReferenceNo = generateNewClaimReferenceNo(request.getClaimNo());
                
                // Create a new ClaimDetails entity
                ClaimDetails claimDetails = new ClaimDetails();
                claimDetails.setClaimReferenceNo(newClaimReferenceNo);
                claimDetails.setClaimNo(request.getClaimNo());
                claimDetails.setPolicyNo(request.getPolicyNo());
                claimDetails.setInsuredId(request.getInsuredId());
                claimDetails.setLossDate(request.getLossDate());
                claimDetails.setIntimatedDate(request.getIntimatedDate());
                claimDetails.setLossLocation(request.getLossLocation());
                claimDetails.setPoliceStation(request.getPoliceStation());
                claimDetails.setPoliceReportNo(request.getPoliceReportNo());
                claimDetails.setLossDescription(request.getLossDescription());
                claimDetails.setAtFault(request.getAtFault());
                claimDetails.setPolicyPeriod(request.getPolicyPeriod());
                claimDetails.setContactPersonPhoneNo(request.getContactPersonPhoneNo());
                claimDetails.setContactPersonPhoneCode(request.getContactPersonPhoneCode());
                claimDetails.setPolicyReferenceNo(request.getPolicyReferenceNo());
                claimDetails.setPolicyICReferenceNo(request.getPolicyICReferenceNo());
                claimDetails.setClaimRequestReference(request.getClaimRequestReference());
                claimDetails.setClaimCategory(request.getClaimCategory());
                claimDetails.setCreatedUser(request.getCreatedUser());
                claimDetails.setClaimType(request.getClaimType());
                claimDetails.setAccidentNumber(request.getAccidentNumber());
                claimDetails.setIsThirdPartyInvolved(request.getIsThirdPartyInvolved());
                claimDetails.setDriverEmiratesId(request.getDriverEmiratesId());
                claimDetails.setDriverLicenseNumber(request.getDriverLicenseNumber());
                claimDetails.setDriverDob(request.getDriverDob());

                // Save the new claim details
                repository.save(claimDetails);
            } else {
                // Fetch existing claim details
                ClaimDetails existingClaimDetails = repository.findByClaimReferenceNo(request.getClaimReferenceNo());
                
                if (existingClaimDetails != null) {
                    // Update the existing claim details
                    existingClaimDetails.setClaimNo(request.getClaimNo());
                    existingClaimDetails.setPolicyNo(request.getPolicyNo());
                    existingClaimDetails.setInsuredId(request.getInsuredId());
                    existingClaimDetails.setLossDate(request.getLossDate());
                    existingClaimDetails.setIntimatedDate(request.getIntimatedDate());
                    existingClaimDetails.setLossLocation(request.getLossLocation());
                    existingClaimDetails.setPoliceStation(request.getPoliceStation());
                    existingClaimDetails.setPoliceReportNo(request.getPoliceReportNo());
                    existingClaimDetails.setLossDescription(request.getLossDescription());
                    existingClaimDetails.setAtFault(request.getAtFault());
                    existingClaimDetails.setPolicyPeriod(request.getPolicyPeriod());
                    existingClaimDetails.setContactPersonPhoneNo(request.getContactPersonPhoneNo());
                    existingClaimDetails.setContactPersonPhoneCode(request.getContactPersonPhoneCode());
                    existingClaimDetails.setPolicyReferenceNo(request.getPolicyReferenceNo());
                    existingClaimDetails.setPolicyICReferenceNo(request.getPolicyICReferenceNo());
                    existingClaimDetails.setClaimRequestReference(request.getClaimRequestReference());
                    existingClaimDetails.setClaimCategory(request.getClaimCategory());
                    existingClaimDetails.setCreatedUser(request.getCreatedUser());
                    existingClaimDetails.setClaimType(request.getClaimType());
                    existingClaimDetails.setAccidentNumber(request.getAccidentNumber());
                    existingClaimDetails.setIsThirdPartyInvolved(request.getIsThirdPartyInvolved());
                    existingClaimDetails.setDriverEmiratesId(request.getDriverEmiratesId());
                    existingClaimDetails.setDriverLicenseNumber(request.getDriverLicenseNumber());
                    existingClaimDetails.setDriverDob(request.getDriverDob());

                    // Save the updated claim details
                    repository.save(existingClaimDetails);
                } else {
                    // Handle case where existing claim is not found
                    throw new RuntimeException("Claim details not found for reference number: " + request.getClaimReferenceNo());
                }
            }


            response.setErrors(Collections.emptyList());
            response.setMessage("Success");
            response.setResponse(Collections.singletonMap("claimReferenceNo", request.getClaimReferenceNo()));
            response.setIsError(false);
        } catch (Exception e) {
            e.printStackTrace();
            response.setErrors(Collections.emptyList());
            response.setMessage("Failed");
            response.setResponse(Collections.singletonMap("error", e.getMessage()));
            response.setIsError(true);
        }
        return response;
    }
    
    public String generateNewClaimReferenceNo(String claimNo) {
        Long totalRecords = repository.count();
        
        Long nextNumber = totalRecords + 1;

        String formattedNumber = String.format("%04d", nextNumber); 

        // Generate the new claim reference number
        return claimNo + formattedNumber;
    }
}
