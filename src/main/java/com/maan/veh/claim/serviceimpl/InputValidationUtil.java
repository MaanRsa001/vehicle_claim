package com.maan.veh.claim.serviceimpl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maan.veh.claim.auth.passwordEnc;
import com.maan.veh.claim.entity.LoginMaster;
import com.maan.veh.claim.repository.LoginMasterRepository;
import com.maan.veh.claim.request.ClaimDetailsSaveRequest;
import com.maan.veh.claim.request.ClaimIntimationAttachmentDetails;
import com.maan.veh.claim.request.ClaimIntimationDocumentDetails;
import com.maan.veh.claim.request.ClaimIntimationDriver;
import com.maan.veh.claim.request.ClaimIntimationRequestMetaData;
import com.maan.veh.claim.request.ClaimIntimationThirdPartyInfo;
import com.maan.veh.claim.request.ClaimTransactionRequest;
import com.maan.veh.claim.request.ClaimTransactionRequestMetaData;
import com.maan.veh.claim.request.DamageSectionDetailsSaveReq;
import com.maan.veh.claim.request.DealerSectionDetailsSaveReq;
import com.maan.veh.claim.request.FnolRequest;
import com.maan.veh.claim.request.FnolRequestMetaData;
import com.maan.veh.claim.request.GarageSectionDetailsSaveReq;
import com.maan.veh.claim.request.LoginRequest;
import com.maan.veh.claim.request.SaveClaimRequest;
import com.maan.veh.claim.request.TotalAmountDetailsRequest;
import com.maan.veh.claim.response.ErrorList;
import com.maan.veh.claim.response.GarageWorkOrderSaveReq;

@Component
public class InputValidationUtil {
	
	@Autowired
	private LoginMasterRepository loginRepo;
	
	private static SimpleDateFormat DD_MM_YYYY = new SimpleDateFormat("dd/MM/yyyy");
	
	
	public List<ErrorList> isValidUser(LoginRequest req) {
		List<ErrorList> list = new ArrayList<ErrorList>();
		if(StringUtils.isBlank(req.getLoginId()))
			list.add(new ErrorList("100","LoginId","Please enter loginId"));	
		
		if(StringUtils.isBlank(req.getPassword()))
			list.add(new ErrorList("100","LoginId","Please enter password"));
		
		if(StringUtils.isNotBlank(req.getLoginId()) && StringUtils.isNotBlank(req.getPassword())) {
			String loginId = req.getLoginId();
			String password =new passwordEnc().crypt(req.getPassword().trim());
			LoginMaster lmaster =loginRepo.findByLoginIdIgnoreCaseAndPassword(loginId, password);
			if(lmaster==null){
				list.add(new ErrorList("100","LoginId","Please enter valid username/password"));
			}else {
				LoginMaster data =loginRepo.findByLoginIdIgnoreCaseAndPasswordAndStatus(loginId, password, "Y");
				if(data==null)
					list.add(new ErrorList("100","LoginId","Your account has been locked due to invalid password attempts."));

			}
			
		}
		
		return list;
		// TODO Auto-generated method stub
		
	}
	
	public List<ErrorList> validateWorkOrder(GarageWorkOrderSaveReq req){
		List<ErrorList> list = new ArrayList<ErrorList>();
		
		// Validate and set claimNo
        if (StringUtils.isBlank(req.getClaimNo())) {
            list.add(new ErrorList("100","Claim number","Claim number cannot be blank"));
        }

        // Validate and set workOrderNo
        if (StringUtils.isBlank(req.getWorkOrderNo())) {
            list.add(new ErrorList("100","WorkOrderNo","Work order number cannot be blank"));

        }

        // Validate and set workOrderType
        if (StringUtils.isBlank(req.getWorkOrderType())) {
            list.add(new ErrorList("100","WorkOrderType","Work order type cannot be blank"));
        }

        // Validate and set settlementType
        if (StringUtils.isBlank(req.getSettlementType())) {
            list.add(new ErrorList("100","SettlementType","Settlement type cannot be blank"));

        }

        // Validate and set settlementTo
        if (StringUtils.isBlank(req.getSettlementTo())) {
            list.add(new ErrorList("100","SettlementTo","Settlement to cannot be blank"));

        }

//        // Validate and set garageName
//        if (StringUtils.isBlank(req.getGarageName())) {
//            list.add(new ErrorList("100","GarageName","Garage name cannot be blank"));
//
//        }

        // Validate and set location
        if (StringUtils.isBlank(req.getLocation())) {
            list.add(new ErrorList("100","Location","Location cannot be blank"));

        }

        // Validate and set repairType
        if (StringUtils.isBlank(req.getRepairType())) {
            list.add(new ErrorList("100","RepairType","Repair type cannot be blank"));

        }

        if (StringUtils.isNotBlank(req.getSparepartsDealerId())) {
			// Validate and set quotationNo
			if (StringUtils.isBlank(req.getQuotationNo())) {
				list.add(new ErrorList("100", "QuotationNo", "Quotation number cannot be blank"));

			} 
		}
		// Validate and set lossType
        if (StringUtils.isBlank(req.getLossType())) {
            list.add(new ErrorList("100","Loss type","Loss type cannot be blank"));

        }

        // Validate and set createdBy
        if (StringUtils.isBlank(req.getCreatedBy())) {
            list.add(new ErrorList("100","CreatedBy","Created by cannot be blank"));

        }
        
        try {
            if (!StringUtils.isBlank(req.getWorkOrderDate())) {
                DD_MM_YYYY.parse(req.getWorkOrderDate());
            }
        } catch (ParseException e) {
            list.add(new ErrorList("100", "WorkOrderDate", "Work order date is invalid or not in the correct format (dd/MM/yyyy)"));
        }
        
        try {
            if (!StringUtils.isBlank(req.getDeliveryDate())) {
                DD_MM_YYYY.parse(req.getDeliveryDate());
            }
        } catch (ParseException e) {
            list.add(new ErrorList("100", "Delivery Date", "Delivery Date is invalid or not in the correct format (dd/MM/yyyy)"));
        }
        
        try {
            if (!StringUtils.isBlank(req.getTotalLoss())) {
                new BigDecimal(req.getTotalLoss());
            }
        } catch (NumberFormatException e) {
            list.add(new ErrorList("100", "TotalLoss", "Total loss must be a valid number"));
        }

       
		
		return list;
	}

	public List<ErrorList> validateDamageDetails(List<DamageSectionDetailsSaveReq> reqList) {
	    List<ErrorList> list = new ArrayList<>();
	    int line = 1;
	    
	    for (DamageSectionDetailsSaveReq req : reqList) {
	        
	        if (StringUtils.isBlank(req.getClaimNo())) {
	            list.add(new ErrorList("100", "ClaimNo", "Claim number cannot be blank in line number: " + line));
	        }
	        if (StringUtils.isBlank(req.getQuotationNo())) {
	            list.add(new ErrorList("101", "QuotationNo", "Quotation number cannot be blank in line number: " + line));
	        }
	        if (StringUtils.isBlank(req.getRepairReplace())) {
	            list.add(new ErrorList("104", "RepairReplace", "Repair/Replace field cannot be blank in line number: " + line));
	        }
	        if (StringUtils.isBlank(req.getSurveyorId())) {
	            list.add(new ErrorList("110", "SurveyorId", "Surveyor ID cannot be blank in line number: " + line));
	        }
	        if (StringUtils.isBlank(req.getGarageDealer())) {
	            list.add(new ErrorList("111", "GarageDealer", "Garage or Dealer cannot be blank in line number: " + line));
	        }

	        // Conditional validation for "Replace"
	        if ("Replace".equalsIgnoreCase(req.getRepairReplace())) {
	            if (StringUtils.isBlank(req.getReplaceCostDeductPercentage())) {
	                list.add(new ErrorList("112", "ReplaceCostDeductPercentage", "Replace cost deduction percentage cannot be blank in line number: " + line));
	            }
	            if (StringUtils.isBlank(req.getSparepartDeprectionPercentage())) {
	                list.add(new ErrorList("113", "SparepartDeprectionPercentage", "Spare part depreciation percentage cannot be blank in line number: " + line));
	            }
	            if (StringUtils.isBlank(req.getDiscountSparepartPercentage())) {
	                list.add(new ErrorList("114", "DiscountSparepartPercentage", "Discount on spare parts percentage cannot be blank in line number: " + line));
	            }
	        } else { // For non-Replace cases
	            if (StringUtils.isBlank(req.getLabourCostDeductPercentage())) {
	                list.add(new ErrorList("117", "LabourCostDeductPercentage", "Labour cost deduction percentage cannot be blank in line number: " + line));
	            }
	            if (StringUtils.isBlank(req.getLabourDiscPercentage())) {
	                list.add(new ErrorList("118", "LabourDiscPercentage", "Labour discount percentage cannot be blank in line number: " + line));
	            }
	        }

	        // Validation for numeric formats
	        if (StringUtils.isNotBlank(req.getReplaceCostDeductPercentage())) {
	            try {
	                new BigDecimal(req.getReplaceCostDeductPercentage());
	            } catch (NumberFormatException e) {
	                list.add(new ErrorList("112", "ReplaceCostDeductPercentage", "Invalid format for ReplaceCostDeductPercentage in line number: " + line));
	            }
	        }

	        if (StringUtils.isNotBlank(req.getSparepartDeprectionPercentage())) {
	            try {
	                new BigDecimal(req.getSparepartDeprectionPercentage());
	            } catch (NumberFormatException e) {
	                list.add(new ErrorList("113", "SparepartDeprectionPercentage", "Invalid format for SparepartDeprectionPercentage in line number: " + line));
	            }
	        }

	        if (StringUtils.isNotBlank(req.getDiscountSparepartPercentage())) {
	            try {
	                new BigDecimal(req.getDiscountSparepartPercentage());
	            } catch (NumberFormatException e) {
	                list.add(new ErrorList("114", "DiscountSparepartPercentage", "Invalid format for DiscountSparepartPercentage in line number: " + line));
	            }
	        }

	        if (StringUtils.isNotBlank(req.getLabourCostDeductPercentage())) {
	            try {
	                new BigDecimal(req.getLabourCostDeductPercentage());
	            } catch (NumberFormatException e) {
	                list.add(new ErrorList("117", "LabourCostDeductPercentage", "Invalid format for LabourCostDeductPercentage in line number: " + line));
	            }
	        }

	        if (StringUtils.isNotBlank(req.getLabourDiscPercentage())) {
	            try {
	                new BigDecimal(req.getLabourDiscPercentage());
	            } catch (NumberFormatException e) {
	                list.add(new ErrorList("118", "LabourDiscPercentage", "Invalid format for LabourDiscPercentage in line number: " + line));
	            }
	        }
	        
	        line++;
	    }
	    return list;
	}



	public List<ErrorList> validateGarageDamageDetails(List<GarageSectionDetailsSaveReq> reqList) {
		List<ErrorList> list = new ArrayList<>();
	    int line = 1;
	    for (GarageSectionDetailsSaveReq req : reqList) {
	    	if(StringUtils.isNotBlank(req.getDamageSno())) {
	    		line = Integer.valueOf(req.getDamageSno());
			}
	    	
			if (StringUtils.isBlank(req.getClaimNo())) {
				list.add(new ErrorList("100", "ClaimNo", "Claim number cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getQuotationNo())) {
				list.add(new ErrorList("101", "QuotationNO", "Quotation number cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getDamageDirection())) {
				list.add(new ErrorList("102", "DamageDirection", "Damage direction cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getDamagePart())) {
				list.add(new ErrorList("103", "DamagePart", "Damage part cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getRepairReplace())) {
				list.add(new ErrorList("104", "RepairReplace", "Repair/Replace field cannot be blank in line number : "+line));
			}
			if ("Replace".equalsIgnoreCase(req.getRepairReplace())) {
				if (StringUtils.isBlank(req.getNoOfUnits())) {
					list.add(new ErrorList("105", "NoOfUnits", "Number of Units cannot be blank in line number : "+line));
				}
				if (StringUtils.isBlank(req.getReplacementCharge())) {
					list.add(new ErrorList("111", "ReplacementCharge", "Replacement Charge cannot be blank in line number : "+line));
				}
				
			}
			
			if (StringUtils.isBlank(req.getUnitPrice())) {
				list.add(new ErrorList("106", "UnitPrice", "Unit price cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getGarageLoginId())) {
				list.add(new ErrorList("108", "GarageLoginId", "Garage login ID cannot be blank in line number : "+line));
			}
			
			if (StringUtils.isBlank(req.getDamageSno())) {
				list.add(new ErrorList("102", "DamageSno", "Damage Sno cannot be blank in line number : "+line));
			}
		    // Validate number formats and handle exceptions
	        if (StringUtils.isNotBlank(req.getNoOfUnits())) {
	            try {
	                Integer.valueOf(req.getNoOfUnits());
	            } catch (NumberFormatException e) {
	                list.add(new ErrorList("105", "NoOfUnits", "Invalid number format for NoOfUnits in line number : " + line));
	            }
	        }

	        if (StringUtils.isNotBlank(req.getUnitPrice())) {
	            try {
	                new BigDecimal(req.getUnitPrice());
	            } catch (NumberFormatException e) {
	                list.add(new ErrorList("106", "UnitPrice", "Invalid format for UnitPrice in line number : " + line));
	            }
	        }

	        if (StringUtils.isNotBlank(req.getReplacementCharge())) {
	            try {
	                new BigDecimal(req.getReplacementCharge());
	            } catch (NumberFormatException e) {
	                list.add(new ErrorList("111", "ReplacementCharge", "Invalid format for ReplacementCharge in line number : " + line));
	            }
	        }

	        if (StringUtils.isNotBlank(req.getTotalPrice())) {
	            try {
	                new BigDecimal(req.getTotalPrice());
	            } catch (NumberFormatException e) {
	                list.add(new ErrorList("120", "TotalPrice", "Invalid format for TotalPrice in line number : " + line));
	            }
	        }
	        
			line++;
		}
		return list;
	}

	public List<ErrorList> validateDealerDamageDetails(List<DealerSectionDetailsSaveReq> reqList) {
		List<ErrorList> list = new ArrayList<>();
		
	    int line = 1;
	    for (DealerSectionDetailsSaveReq req : reqList) {
	    	if(StringUtils.isNotBlank(req.getDamageSno())) {
	    		line = Integer.valueOf(req.getDamageSno());
			}
			if (StringUtils.isBlank(req.getClaimNo())) {
				list.add(new ErrorList("100", "ClaimNo", "Claim number cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getQuotationNo())) {
				list.add(new ErrorList("101", "QuotationNO", "Quotation number cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getDamageSno())) {
				list.add(new ErrorList("102", "DamageSno", "Damage Sno cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getUnitPrice())) {
				list.add(new ErrorList("106", "UnitPrice", "Unit price cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getDealerLoginId())) {
				list.add(new ErrorList("108", "DealerLoginId", "Dealer login ID cannot be blank in line number : "+line));
			}

	        if (StringUtils.isNotBlank(req.getUnitPrice())) {
	            try {
	                new BigDecimal(req.getUnitPrice());
	            } catch (NumberFormatException e) {
	                list.add(new ErrorList("106", "UnitPrice", "Invalid format for UnitPrice in line number : " + line));
	            }
	        }
	        
			line++;
		}
		return list;
	}

	public List<ErrorList> validateTotalAmountDetails(TotalAmountDetailsRequest req) {
		List<ErrorList> list = new ArrayList<>();

		if (StringUtils.isBlank(req.getClaimNo())) {
			list.add(new ErrorList("100", "ClaimNo", "Claim number cannot be blank "));
		}

		return list;
	}
	
	public List<ErrorList> validateClaimDetails(ClaimDetailsSaveRequest request) {
	    List<ErrorList> errors = new ArrayList<>();

	    // Validate and set ClaimReferenceNo
	    if (StringUtils.isBlank(request.getClaimReferenceNo())) {
	        errors.add(new ErrorList("100", "ClaimReferenceNo", "Claim reference number cannot be blank"));
	    }

	    // Validate and set ClaimNo
	    if (StringUtils.isBlank(request.getClaimNo())) {
	        errors.add(new ErrorList("100", "ClaimNo", "Claim number cannot be blank"));
	    }

	    // Validate and set PolicyNo
	    if (StringUtils.isBlank(request.getPolicyNo())) {
	        errors.add(new ErrorList("100", "PolicyNo", "Policy number cannot be blank"));
	    }

	    // Validate and set InsuredId
	    if (StringUtils.isBlank(request.getInsuredId())) {
	        errors.add(new ErrorList("100", "InsuredId", "Insured ID cannot be blank"));
	    }

	    // Validate and set LossLocation
	    if (StringUtils.isBlank(request.getLossLocation())) {
	        errors.add(new ErrorList("100", "LossLocation", "Loss location cannot be blank"));
	    }

	    // Validate and set PoliceStation
	    if (StringUtils.isBlank(request.getPoliceStation())) {
	        errors.add(new ErrorList("100", "PoliceStation", "Police station cannot be blank"));
	    }

	    // Validate and set PoliceReportNo
	    if (StringUtils.isBlank(request.getPoliceReportNo())) {
	        errors.add(new ErrorList("100", "PoliceReportNo", "Police report number cannot be blank"));
	    }

	    // Validate and set LossDescription
	    if (StringUtils.isBlank(request.getLossDescription())) {
	        errors.add(new ErrorList("100", "LossDescription", "Loss description cannot be blank"));
	    }

	    // Validate and set AtFault
	    if (StringUtils.isBlank(request.getAtFault())) {
	        errors.add(new ErrorList("100", "AtFault", "At fault cannot be blank"));
	    }

	    // Validate and set PolicyPeriod
	    if (StringUtils.isBlank(request.getPolicyPeriod())) {
	        errors.add(new ErrorList("100", "PolicyPeriod", "Policy period cannot be blank"));
	    }

	    // Validate and set ContactPersonPhoneNo
	    if (StringUtils.isBlank(request.getContactPersonPhoneNo())) {
	        errors.add(new ErrorList("100", "ContactPersonPhoneNo", "Contact person phone number cannot be blank"));
	    }

	    // Validate and set ContactPersonPhoneCode
	    if (StringUtils.isBlank(request.getContactPersonPhoneCode())) {
	        errors.add(new ErrorList("100", "ContactPersonPhoneCode", "Contact person phone code cannot be blank"));
	    }

	    // Validate and set PolicyReferenceNo
	    if (StringUtils.isBlank(request.getPolicyReferenceNo())) {
	        errors.add(new ErrorList("100", "PolicyReferenceNo", "Policy reference number cannot be blank"));
	    }

	    // Validate and set PolicyICReferenceNo
	    if (StringUtils.isBlank(request.getPolicyICReferenceNo())) {
	        errors.add(new ErrorList("100", "PolicyICReferenceNo", "Policy IC reference number cannot be blank"));
	    }

	    // Validate and set ClaimRequestReference
	    if (StringUtils.isBlank(request.getClaimRequestReference())) {
	        errors.add(new ErrorList("100", "ClaimRequestReference", "Claim request reference cannot be blank"));
	    }

	    // Validate and set ClaimCategory
	    if (StringUtils.isBlank(request.getClaimCategory())) {
	        errors.add(new ErrorList("100", "ClaimCategory", "Claim category cannot be blank"));
	    }

	    // Validate and set CreatedUser
	    if (StringUtils.isBlank(request.getCreatedUser())) {
	        errors.add(new ErrorList("100", "CreatedUser", "Created user cannot be blank"));
	    }

	    // Validate ClaimType (cannot be null)
	    if (request.getClaimType() == null) {
	        errors.add(new ErrorList("100", "ClaimType", "Claim type cannot be null"));
	    }

	    // Validate and set AccidentNumber
	    if (StringUtils.isBlank(request.getAccidentNumber())) {
	        errors.add(new ErrorList("100", "AccidentNumber", "Accident number cannot be blank"));
	    }

	    // Validate IsThirdPartyInvolved (cannot be null)
	    if (request.getIsThirdPartyInvolved() == null) {
	        errors.add(new ErrorList("100", "IsThirdPartyInvolved", "Third party involvement cannot be null"));
	    }

	    // Validate Driver details
	    if (StringUtils.isBlank(request.getDriverEmiratesId())) {
	        errors.add(new ErrorList("100", "DriverEmiratesId", "Driver Emirates ID cannot be blank"));
	    }
	    if (StringUtils.isBlank(request.getDriverLicenseNumber())) {
	        errors.add(new ErrorList("100", "DriverLicenseNumber", "Driver license number cannot be blank"));
	    }
	    if (request.getDriverDob() == null) {
	        errors.add(new ErrorList("100", "DriverDob", "Driver date of birth cannot be null"));
	    }

	    return errors;
	}

//	public List<ErrorList> validateClaimIntemationDetails(SaveClaimRequest request) {
//		 List<ErrorList> errors = new ArrayList<>();
//
//	        // Validate RequestMetaData fields
//		    ClaimIntimationRequestMetaData metaData = request.getRequestMetaData();
//	        if (metaData != null) {
//	            // Validate ConsumerTrackingID
//	            if (StringUtils.isBlank(metaData.getConsumerTrackingID())) {
//	                errors.add(new ErrorList("100", "RequestMetaData.ConsumerTrackingID", "ConsumerTrackingID cannot be blank"));
//	            }
//	            
//	            // Validate CurrentBranch
//	            if (StringUtils.isBlank(metaData.getCurrentBranch())) {
//	                errors.add(new ErrorList("100", "RequestMetaData.CurrentBranch", "CurrentBranch cannot be blank"));
//	            }
//	            
//	            // Validate IpAddress
//	            if (StringUtils.isBlank(metaData.getIpAddress())) {
//	                errors.add(new ErrorList("100", "RequestMetaData.IpAddress", "IpAddress cannot be blank"));
//	            }
//	            
//	            // Validate OriginBranch
//	            if (StringUtils.isBlank(metaData.getOriginBranch())) {
//	                errors.add(new ErrorList("100", "RequestMetaData.OriginBranch", "OriginBranch cannot be blank"));
//	            }
//	            
//	            // Validate RequestData
//	            if (StringUtils.isBlank(metaData.getRequestData())) {
//	                errors.add(new ErrorList("100", "RequestMetaData.RequestData", "RequestData cannot be blank"));
//	            }
//	            
//	            // Validate RequestGeneratedDateTime
//	            if (StringUtils.isBlank(metaData.getRequestGeneratedDateTime())) {
//	                errors.add(new ErrorList("100", "RequestMetaData.RequestGeneratedDateTime", "RequestGeneratedDateTime cannot be blank"));
//	            }
//	            
//	            // Validate RequestId
//	            if (StringUtils.isBlank(metaData.getRequestId())) {
//	                errors.add(new ErrorList("100", "RequestMetaData.RequestId", "RequestId cannot be blank"));
//	            }
//	            
//	            // Validate RequestOrigin
//	            if (StringUtils.isBlank(metaData.getRequestOrigin())) {
//	                errors.add(new ErrorList("100", "RequestMetaData.RequestOrigin", "RequestOrigin cannot be blank"));
//	            }
//	            
//	            // Validate RequestReference
//	            if (StringUtils.isBlank(metaData.getRequestReference())) {
//	                errors.add(new ErrorList("100", "RequestMetaData.RequestReference", "RequestReference cannot be blank"));
//	            }
//	            
//	            // Validate RequestedService
//	            if (StringUtils.isBlank(metaData.getRequestedService())) {
//	                errors.add(new ErrorList("100", "RequestMetaData.RequestedService", "RequestedService cannot be blank"));
//	            }
//	            
//	            // Validate ResponseData (optional validation if needed)
//	            // Validate SourceCode
//	            if (StringUtils.isBlank(metaData.getSourceCode())) {
//	                errors.add(new ErrorList("100", "RequestMetaData.SourceCode", "SourceCode cannot be blank"));
//	            }
//
//	            // Validate UserName
//	            if (StringUtils.isBlank(metaData.getUserName())) {
//	                errors.add(new ErrorList("100", "RequestMetaData.UserName", "UserName cannot be blank"));
//	            }
//	        } else {
//	            errors.add(new ErrorList("100", "RequestMetaData", "RequestMetaData cannot be null"));
//	        }
//
//
//	        // Validate SaveClaimRequest fields
//	        if (StringUtils.isBlank(request.getLanguageCode())) {
//	            errors.add(new ErrorList("100", "LanguageCode", "LanguageCode cannot be blank"));
//	        }
//	        if (StringUtils.isBlank(request.getPolicyNo())) {
//	            errors.add(new ErrorList("100", "PolicyNo", "PolicyNo cannot be blank"));
//	        }
//	        if (StringUtils.isBlank(request.getInsuredId())) {
//	            errors.add(new ErrorList("100", "InsuredId", "InsuredId cannot be blank"));
//	        }
//	        if (StringUtils.isBlank(request.getLossDate())) {
//	            errors.add(new ErrorList("100", "LossDate", "LossDate cannot be blank"));
//	        }
//	        if (StringUtils.isBlank(request.getIntimatedDate())) {
//	            errors.add(new ErrorList("100", "IntimatedDate", "IntimatedDate cannot be blank"));
//	        }
//	        if (StringUtils.isBlank(request.getLossLocation())) {
//	            errors.add(new ErrorList("100", "LossLocation", "LossLocation cannot be blank"));
//	        }
//	        if (StringUtils.isBlank(request.getPoliceStation())) {
//	            errors.add(new ErrorList("100", "PoliceStation", "PoliceStation cannot be blank"));
//	        }
//	        if (StringUtils.isBlank(request.getPoliceReportNo())) {
//	            errors.add(new ErrorList("100", "PoliceReportNo", "PoliceReportNo cannot be blank"));
//	        }
//	        if (StringUtils.isBlank(request.getLossDescription())) {
//	            errors.add(new ErrorList("100", "LossDescription", "LossDescription cannot be blank"));
//	        }
//	        if (StringUtils.isBlank(request.getAtFault())) {
//	            errors.add(new ErrorList("100", "AtFault", "AtFault cannot be blank"));
//	        }
//	        if (StringUtils.isBlank(request.getPolicyPeriod())) {
//	            errors.add(new ErrorList("100", "PolicyPeriod", "PolicyPeriod cannot be blank"));
//	        }
//	        if (StringUtils.isBlank(request.getContactPersonPhoneNo())) {
//	            errors.add(new ErrorList("100", "ContactPersonPhoneNo", "ContactPersonPhoneNo cannot be blank"));
//	        }
//	        if (StringUtils.isBlank(request.getContactPersonPhoneCode())) {
//	            errors.add(new ErrorList("100", "ContactPersonPhoneCode", "ContactPersonPhoneCode cannot be blank"));
//	        }
//	        if (StringUtils.isBlank(request.getPolicyReferenceNo())) {
//	            errors.add(new ErrorList("100", "PolicyReferenceNo", "PolicyReferenceNo cannot be blank"));
//	        }
//	        if (StringUtils.isBlank(request.getPolicyICReferenceNo())) {
//	            errors.add(new ErrorList("100", "PolicyICReferenceNo", "PolicyICReferenceNo cannot be blank"));
//	        }
//	        if (StringUtils.isBlank(request.getClaimRequestReference())) {
//	            errors.add(new ErrorList("100", "ClaimRequestReference", "ClaimRequestReference cannot be blank"));
//	        }
//	        if (StringUtils.isBlank(request.getClaimCategory())) {
//	            errors.add(new ErrorList("100", "ClaimCategory", "ClaimCategory cannot be blank"));
//	        }
//
//	        // Validate Driver details
//	        ClaimIntimationDriver driver = request.getDriver();
//	        if (driver != null) {
//	            if (StringUtils.isBlank(driver.getEmiratesId())) {
//	                errors.add(new ErrorList("100", "Driver.EmiratesId", "EmiratesId cannot be blank"));
//	            }
//	            if (StringUtils.isBlank(driver.getLicenseNumber())) {
//	                errors.add(new ErrorList("100", "Driver.LicenseNumber", "LicenseNumber cannot be blank"));
//	            }
//	            if (StringUtils.isBlank(driver.getDob())) {
//	                errors.add(new ErrorList("100", "Driver.Dob", "Dob cannot be blank"));
//	            }
//	        }
//
//	     // Validate AttachmentDetails
//	        ClaimIntimationAttachmentDetails attachmentDetails = request.getAttachmentDetails();
//	        if (attachmentDetails != null && attachmentDetails.getDocumentDetails() != null) {
//	            for (ClaimIntimationDocumentDetails doc : attachmentDetails.getDocumentDetails()) {
//	                
//	                // Validate DocumentData
//	                if (StringUtils.isBlank(doc.getDocumentData())) {
//	                    errors.add(new ErrorList("100", "AttachmentDetails.DocumentDetails.DocumentData", "DocumentData cannot be blank"));
//	                }
//	                
//	                // Validate DocumentFormat
//	                if (StringUtils.isBlank(doc.getDocumentFormat())) {
//	                    errors.add(new ErrorList("100", "AttachmentDetails.DocumentDetails.DocumentFormat", "DocumentFormat cannot be blank"));
//	                }
//	                
//	                // Validate DocumentId
//	                if (StringUtils.isBlank(doc.getDocumentId())) {
//	                    errors.add(new ErrorList("100", "AttachmentDetails.DocumentDetails.DocumentId", "DocumentId cannot be blank"));
//	                }
//	                
//	                // Validate DocumentName
//	                if (StringUtils.isBlank(doc.getDocumentName())) {
//	                    errors.add(new ErrorList("100", "AttachmentDetails.DocumentDetails.DocumentName", "DocumentName cannot be blank"));
//	                }
//	                
//	                // Validate DocumentRefNo
//	                if (StringUtils.isBlank(doc.getDocumentRefNo())) {
//	                    errors.add(new ErrorList("100", "AttachmentDetails.DocumentDetails.DocumentRefNo", "DocumentRefNo cannot be blank"));
//	                }
//	                
//	                // Validate DocumentType
//	                if (StringUtils.isBlank(doc.getDocumentType())) {
//	                    errors.add(new ErrorList("100", "AttachmentDetails.DocumentDetails.DocumentType", "DocumentType cannot be blank"));
//	                }
//	                
//	                // Validate DocumentURL
//	                if (StringUtils.isBlank(doc.getDocumentURL())) {
//	                    errors.add(new ErrorList("100", "AttachmentDetails.DocumentDetails.DocumentURL", "DocumentURL cannot be blank"));
//	                }
//	            }
//	        } else if (attachmentDetails != null && attachmentDetails.getDocumentDetails() == null) {
//	            errors.add(new ErrorList("100", "AttachmentDetails.DocumentDetails", "DocumentDetails cannot be null"));
//	        }
//
//
//	     // Validate ThirdPartyInfo
//	        if (request.getIsThirdPartyInvolved() != null && request.getIsThirdPartyInvolved().equalsIgnoreCase("true")) {
//	            List<ClaimIntimationThirdPartyInfo> thirdPartyInfoList = request.getThirdPartyInfo();
//	            if (thirdPartyInfoList != null) {
//	                for (ClaimIntimationThirdPartyInfo info : thirdPartyInfoList) {
//	                    
//	                    // Validate TPDriverLiability
//	                    if (StringUtils.isBlank(info.getTpDriverLiability())) {
//	                        errors.add(new ErrorList("100", "ThirdPartyInfo.TPDriverLiability", "TPDriverLiability cannot be blank"));
//	                    }
//	                    
//	                    // Validate TPDriverLicenceNo
//	                    if (StringUtils.isBlank(info.getTpDriverLicenceNo())) {
//	                        errors.add(new ErrorList("100", "ThirdPartyInfo.TPDriverLicenceNo", "TPDriverLicenceNo cannot be blank"));
//	                    }
//	                    
//	                    // Validate TPDriverName
//	                    if (StringUtils.isBlank(info.getTpDriverName())) {
//	                        errors.add(new ErrorList("100", "ThirdPartyInfo.TPDriverName", "TPDriverName cannot be blank"));
//	                    }
//	                    
//	                    // Validate TPDriverNationalityCode
//	                    if (StringUtils.isBlank(info.getTpDriverNationalityCode())) {
//	                        errors.add(new ErrorList("100", "ThirdPartyInfo.TPDriverNationalityCode", "TPDriverNationalityCode cannot be blank"));
//	                    }
//	                    
//	                    // Validate TPDriverTrafficNo
//	                    if (StringUtils.isBlank(info.getTpDriverTrafficNo())) {
//	                        errors.add(new ErrorList("100", "ThirdPartyInfo.TPDriverTrafficNo", "TPDriverTrafficNo cannot be blank"));
//	                    }
//	                    
//	                    // Validate TPMobileNumber
//	                    if (StringUtils.isBlank(info.getTpMobileNumber())) {
//	                        errors.add(new ErrorList("100", "ThirdPartyInfo.TPMobileNumber", "TPMobileNumber cannot be blank"));
//	                    }
//	                    
//	                    // Validate TPVehicleCurrentInsurer
//	                    if (StringUtils.isBlank(info.getTpVehicleCurrentInsurer())) {
//	                        errors.add(new ErrorList("100", "ThirdPartyInfo.TPVehicleCurrentInsurer", "TPVehicleCurrentInsurer cannot be blank"));
//	                    }
//	                    
//	                    // Validate TPVehicleMake
//	                    if (StringUtils.isBlank(info.getTpVehicleMake())) {
//	                        errors.add(new ErrorList("100", "ThirdPartyInfo.TPVehicleMake", "TPVehicleMake cannot be blank"));
//	                    }
//	                    
//	                    // Validate TPVehicleMakeCode
//	                    if (StringUtils.isBlank(info.getTpVehicleMakeCode())) {
//	                        errors.add(new ErrorList("100", "ThirdPartyInfo.TPVehicleMakeCode", "TPVehicleMakeCode cannot be blank"));
//	                    }
//	                    
//	                    // Validate TPVehicleModel
//	                    if (StringUtils.isBlank(info.getTpVehicleModel())) {
//	                        errors.add(new ErrorList("100", "ThirdPartyInfo.TPVehicleModel", "TPVehicleModel cannot be blank"));
//	                    }
//	                    
//	                    // Validate TPVehicleModelCode
//	                    if (StringUtils.isBlank(info.getTpVehicleModelCode())) {
//	                        errors.add(new ErrorList("100", "ThirdPartyInfo.TPVehicleModelCode", "TPVehicleModelCode cannot be blank"));
//	                    }
//	                    
//	                    // Validate TPVehiclePlateCode
//	                    if (StringUtils.isBlank(info.getTpVehiclePlateCode())) {
//	                        errors.add(new ErrorList("100", "ThirdPartyInfo.TPVehiclePlateCode", "TPVehiclePlateCode cannot be blank"));
//	                    }
//	                    
//	                    // Validate TPVehiclePlateNo
//	                    if (StringUtils.isBlank(info.getTpVehiclePlateNo())) {
//	                        errors.add(new ErrorList("100", "ThirdPartyInfo.TPVehiclePlateNo", "TPVehiclePlateNo cannot be blank"));
//	                    }
//	                    
//	                    // Validate TPVehiclePlateTypeCode
//	                    if (StringUtils.isBlank(info.getTpVehiclePlateTypeCode())) {
//	                        errors.add(new ErrorList("100", "ThirdPartyInfo.TPVehiclePlateTypeCode", "TPVehiclePlateTypeCode cannot be blank"));
//	                    }
//	                    
//	                    // Validate ThirdPartyReference
//	                    if (StringUtils.isBlank(info.getThirdPartyReference())) {
//	                        errors.add(new ErrorList("100", "ThirdPartyInfo.ThirdPartyReference", "ThirdPartyReference cannot be blank"));
//	                    }
//	                    
//	                    // Validate ThirdPartyType
//	                    if (StringUtils.isBlank(info.getThirdPartyType())) {
//	                        errors.add(new ErrorList("100", "ThirdPartyInfo.ThirdPartyType", "ThirdPartyType cannot be blank"));
//	                    }
//	                }
//	            } else {
//	                errors.add(new ErrorList("100", "ThirdPartyInfo", "ThirdPartyInfo cannot be null when IsThirdPartyInvolved is true"));
//	            }
//	        }
//
//
//	        return errors;
//	}
	
	public List<ErrorList> validateClaimIntemationDetails(SaveClaimRequest request) {
	    List<ErrorList> errors = new ArrayList<>();

	    // Validate RequestMetaData fields
	    ClaimIntimationRequestMetaData metaData = request.getRequestMetaData();
	    if (metaData != null) {
	        // Validate ConsumerTrackingID
	        if (StringUtils.isBlank(metaData.getConsumerTrackingID())) {
	            errors.add(new ErrorList("100", "RequestMetaData.ConsumerTrackingID", "ConsumerTrackingID cannot be blank"));
	        }
	        
	        // Validate CurrentBranch
	        if (StringUtils.isBlank(metaData.getCurrentBranch())) {
	            errors.add(new ErrorList("100", "RequestMetaData.CurrentBranch", "CurrentBranch cannot be blank"));
	        }
	        
	        // Validate IpAddress
	        /*
	        if (StringUtils.isBlank(metaData.getIpAddress())) {
	            errors.add(new ErrorList("100", "RequestMetaData.IpAddress", "IpAddress cannot be blank"));
	        }
	        */

	        // Validate OriginBranch
	        if (StringUtils.isBlank(metaData.getOriginBranch())) {
	            errors.add(new ErrorList("100", "RequestMetaData.OriginBranch", "OriginBranch cannot be blank"));
	        }
	        
	        // Validate RequestData
	        /*
	        if (StringUtils.isBlank(metaData.getRequestData())) {
	            errors.add(new ErrorList("100", "RequestMetaData.RequestData", "RequestData cannot be blank"));
	        }
	        */
	        
	        // Validate RequestGeneratedDateTime
	        if (StringUtils.isBlank(metaData.getRequestGeneratedDateTime())) {
	            errors.add(new ErrorList("100", "RequestMetaData.RequestGeneratedDateTime", "RequestGeneratedDateTime cannot be blank"));
	        }
	        
	        // Validate RequestId
	        /*
	        if (StringUtils.isBlank(metaData.getRequestId())) {
	            errors.add(new ErrorList("100", "RequestMetaData.RequestId", "RequestId cannot be blank"));
	        }
	        */
	        
	        // Validate RequestOrigin
	        if (StringUtils.isBlank(metaData.getRequestOrigin())) {
	            errors.add(new ErrorList("100", "RequestMetaData.RequestOrigin", "RequestOrigin cannot be blank"));
	        }
	        
	        // Validate RequestReference
	        /*
	        if (StringUtils.isBlank(metaData.getRequestReference())) {
	            errors.add(new ErrorList("100", "RequestMetaData.RequestReference", "RequestReference cannot be blank"));
	        }
	        */

	        // Validate RequestedService
	        /*
	        if (StringUtils.isBlank(metaData.getRequestedService())) {
	            errors.add(new ErrorList("100", "RequestMetaData.RequestedService", "RequestedService cannot be blank"));
	        }
	        */

	        // Validate SourceCode
	        /*
	        if (StringUtils.isBlank(metaData.getSourceCode())) {
	            errors.add(new ErrorList("100", "RequestMetaData.SourceCode", "SourceCode cannot be blank"));
	        }
	        */

	        // Validate UserName
	        if (StringUtils.isBlank(metaData.getUserName())) {
	            errors.add(new ErrorList("100", "RequestMetaData.UserName", "UserName cannot be blank"));
	        }
	    } else {
	        errors.add(new ErrorList("100", "RequestMetaData", "RequestMetaData cannot be null"));
	    }

	    // Validate SaveClaimRequest fields
	    if (StringUtils.isBlank(request.getLanguageCode())) {
	        errors.add(new ErrorList("100", "LanguageCode", "LanguageCode cannot be blank"));
	    }
	    if (StringUtils.isBlank(request.getPolicyNo())) {
	        errors.add(new ErrorList("100", "PolicyNo", "PolicyNo cannot be blank"));
	    }
	    if (StringUtils.isBlank(request.getInsuredId())) {
	        errors.add(new ErrorList("100", "InsuredId", "InsuredId cannot be blank"));
	    }
	    if (StringUtils.isBlank(request.getLossDate())) {
	        errors.add(new ErrorList("100", "LossDate", "LossDate cannot be blank"));
	    }
	    if (StringUtils.isBlank(request.getIntimatedDate())) {
	        errors.add(new ErrorList("100", "IntimatedDate", "IntimatedDate cannot be blank"));
	    }
	    if (StringUtils.isBlank(request.getLossLocation())) {
	        errors.add(new ErrorList("100", "LossLocation", "LossLocation cannot be blank"));
	    }
	    if (StringUtils.isBlank(request.getPoliceStation())) {
	        errors.add(new ErrorList("100", "PoliceStation", "PoliceStation cannot be blank"));
	    }
	    if (StringUtils.isBlank(request.getPoliceReportNo())) {
	        errors.add(new ErrorList("100", "PoliceReportNo", "PoliceReportNo cannot be blank"));
	    }
	    if (StringUtils.isBlank(request.getLossDescription())) {
	        errors.add(new ErrorList("100", "LossDescription", "LossDescription cannot be blank"));
	    }
	    if (StringUtils.isBlank(request.getAtFault())) {
	        errors.add(new ErrorList("100", "AtFault", "AtFault cannot be blank"));
	    }
	    /*
	    if (StringUtils.isBlank(request.getPolicyPeriod())) {
	        errors.add(new ErrorList("100", "PolicyPeriod", "PolicyPeriod cannot be blank"));
	    }
	    */
	    /*
	    if (StringUtils.isBlank(request.getContactPersonPhoneNo())) {
	        errors.add(new ErrorList("100", "ContactPersonPhoneNo", "ContactPersonPhoneNo cannot be blank"));
	    }
	    */
	    /*
	    if (StringUtils.isBlank(request.getContactPersonPhoneCode())) {
	        errors.add(new ErrorList("100", "ContactPersonPhoneCode", "ContactPersonPhoneCode cannot be blank"));
	    }
	    */
	    /*
	    if (StringUtils.isBlank(request.getPolicyReferenceNo())) {
	        errors.add(new ErrorList("100", "PolicyReferenceNo", "PolicyReferenceNo cannot be blank"));
	    }
	    */
	    /*
	    if (StringUtils.isBlank(request.getPolicyICReferenceNo())) {
	        errors.add(new ErrorList("100", "PolicyICReferenceNo", "PolicyICReferenceNo cannot be blank"));
	    }
	    */
	    /*
	    if (StringUtils.isBlank(request.getClaimRequestReference())) {
	        errors.add(new ErrorList("100", "ClaimRequestReference", "ClaimRequestReference cannot be blank"));
	    }
	    */
	    /*
	    if (StringUtils.isBlank(request.getClaimCategory())) {
	        errors.add(new ErrorList("100", "ClaimCategory", "ClaimCategory cannot be blank"));
	    }
	    */

	    // Validate Driver details
	    ClaimIntimationDriver driver = request.getDriver();
	    if (driver != null) {
	        if (StringUtils.isBlank(driver.getEmiratesId())) {
	            errors.add(new ErrorList("100", "Driver.EmiratesId", "EmiratesId cannot be blank"));
	        }
	        if (StringUtils.isBlank(driver.getLicenseNumber())) {
	            errors.add(new ErrorList("100", "Driver.LicenseNumber", "LicenseNumber cannot be blank"));
	        }
	        if (StringUtils.isBlank(driver.getDob())) {
	            errors.add(new ErrorList("100", "Driver.Dob", "Dob cannot be blank"));
	        }
	    }

	    // Validate AttachmentDetails
	    ClaimIntimationAttachmentDetails attachmentDetails = request.getAttachmentDetails();
	    if (attachmentDetails != null && attachmentDetails.getDocumentDetails() != null) {
	        for (ClaimIntimationDocumentDetails doc : attachmentDetails.getDocumentDetails()) {
	            
	            // Validate DocumentData
	            if (StringUtils.isBlank(doc.getDocumentData())) {
	                errors.add(new ErrorList("100", "AttachmentDetails.DocumentDetails.DocumentData", "DocumentData cannot be blank"));
	            }
	            
	            // Validate DocumentFormat
	            if (StringUtils.isBlank(doc.getDocumentFormat())) {
	                errors.add(new ErrorList("100", "AttachmentDetails.DocumentDetails.DocumentFormat", "DocumentFormat cannot be blank"));
	            }
	            
	            // Validate DocumentId
	            if (StringUtils.isBlank(doc.getDocumentId())) {
	                errors.add(new ErrorList("100", "AttachmentDetails.DocumentDetails.DocumentId", "DocumentId cannot be blank"));
	            }
	            
	            // Validate DocumentName
	            if (StringUtils.isBlank(doc.getDocumentName())) {
	                errors.add(new ErrorList("100", "AttachmentDetails.DocumentDetails.DocumentName", "DocumentName cannot be blank"));
	            }
	            
	            // Validate DocumentRefNo
	            if (StringUtils.isBlank(doc.getDocumentRefNo())) {
	                errors.add(new ErrorList("100", "AttachmentDetails.DocumentDetails.DocumentRefNo", "DocumentRefNo cannot be blank"));
	            }
	            
	            // Validate DocumentType
	            if (StringUtils.isBlank(doc.getDocumentType())) {
	                errors.add(new ErrorList("100", "AttachmentDetails.DocumentDetails.DocumentType", "DocumentType cannot be blank"));
	            }
	            
	            // Validate DocumentURL
	            if (StringUtils.isBlank(doc.getDocumentURL())) {
	                errors.add(new ErrorList("100", "AttachmentDetails.DocumentDetails.DocumentURL", "DocumentURL cannot be blank"));
	            }
	        }
	    } else if (attachmentDetails != null && attachmentDetails.getDocumentDetails() == null) {
	        errors.add(new ErrorList("100", "AttachmentDetails.DocumentDetails", "DocumentDetails cannot be null"));
	    }

	    // Validate ThirdPartyInfo
	    if (request.getIsThirdPartyInvolved() != null && request.getIsThirdPartyInvolved().equalsIgnoreCase("true")) {
	        List<ClaimIntimationThirdPartyInfo> thirdPartyInfoList = request.getThirdPartyInfo();
	        if (thirdPartyInfoList != null) {
	            for (ClaimIntimationThirdPartyInfo info : thirdPartyInfoList) {
	                
	                // Validate TPDriverLiability
	                if (StringUtils.isBlank(info.getTpDriverLiability())) {
	                    errors.add(new ErrorList("100", "ThirdPartyInfo.TpDriverLiability", "TpDriverLiability cannot be blank"));
	                }
	                
	            }
	        } else {
	            errors.add(new ErrorList("100", "ThirdPartyInfo", "ThirdPartyInfo cannot be null"));
	        }
	    }

	    return errors;
	}

	
	public List<ErrorList> validateFnolRequest(FnolRequest request) {
            List<ErrorList> errors = new ArrayList<>();
        
        // Validate RequestMetaData
        FnolRequestMetaData metaData = request.getRequestMetaData();
        if (metaData != null) {
            if (StringUtils.isBlank(metaData.getConsumerTrackingID())) {
                errors.add(new ErrorList("100", "RequestMetaData.ConsumerTrackingID", "ConsumerTrackingID cannot be blank"));
            }
            if (StringUtils.isBlank(metaData.getCurrentBranch())) {
                errors.add(new ErrorList("100", "RequestMetaData.CurrentBranch", "CurrentBranch cannot be blank"));
            }
            if (StringUtils.isBlank(metaData.getIpAddress())) {
                errors.add(new ErrorList("100", "RequestMetaData.IpAddress", "IpAddress cannot be blank"));
            }
            if (StringUtils.isBlank(metaData.getOriginBranch())) {
                errors.add(new ErrorList("100", "RequestMetaData.OriginBranch", "OriginBranch cannot be blank"));
            }
            if (StringUtils.isBlank(metaData.getRequestData())) {
                errors.add(new ErrorList("100", "RequestMetaData.RequestData", "RequestData cannot be blank"));
            }
            if (StringUtils.isBlank(metaData.getRequestGeneratedDateTime())) {
                errors.add(new ErrorList("100", "RequestMetaData.RequestGeneratedDateTime", "RequestGeneratedDateTime cannot be blank"));
            }
            if (StringUtils.isBlank(metaData.getRequestId())) {
                errors.add(new ErrorList("100", "RequestMetaData.RequestId", "RequestId cannot be blank"));
            }
            if (StringUtils.isBlank(metaData.getRequestOrigin())) {
                errors.add(new ErrorList("100", "RequestMetaData.RequestOrigin", "RequestOrigin cannot be blank"));
            }
            if (StringUtils.isBlank(metaData.getRequestReference())) {
                errors.add(new ErrorList("100", "RequestMetaData.RequestReference", "RequestReference cannot be blank"));
            }
            if (StringUtils.isBlank(metaData.getRequestedService())) {
                errors.add(new ErrorList("100", "RequestMetaData.RequestedService", "RequestedService cannot be blank"));
            }
            if (StringUtils.isBlank(metaData.getResponseData())) {
                errors.add(new ErrorList("100", "RequestMetaData.ResponseData", "ResponseData cannot be blank"));
            }
            if (StringUtils.isBlank(metaData.getSourceCode())) {
                errors.add(new ErrorList("100", "RequestMetaData.SourceCode", "SourceCode cannot be blank"));
            }
            if (StringUtils.isBlank(metaData.getUserName())) {
                errors.add(new ErrorList("100", "RequestMetaData.UserName", "UserName cannot be blank"));
            }
        } else {
            errors.add(new ErrorList("100", "RequestMetaData", "RequestMetaData cannot be null"));
        }
        
        // Validate other fields
        if (StringUtils.isEmpty(request.getJwtToken())) {
            errors.add(new ErrorList("100", "JwtToken", "JwtToken cannot be blank"));
        }
        if (StringUtils.isBlank(request.getCustomerId())) {
            errors.add(new ErrorList("100", "CustomerId", "CustomerId cannot be blank"));
        }
        if (StringUtils.isBlank(request.getPolicyNo())) {
            errors.add(new ErrorList("100", "PolicyNo", "PolicyNo cannot be blank"));
        }
        if (StringUtils.isBlank(request.getFnolNo())) {
            errors.add(new ErrorList("100", "FnolNo", "FnolNo cannot be blank"));
        }
        if (StringUtils.isBlank(request.getLossDate())) {
            errors.add(new ErrorList("100", "LossDate", "LossDate cannot be blank"));
        }
        
        return errors;
	}

	public List<ErrorList> validateClaimTransactionRequest(ClaimTransactionRequest request) {
List<ErrorList> errors = new ArrayList<>();
        
        // Validate RequestMetaData
        ClaimTransactionRequestMetaData metaData = request.getRequestMetaData();
        if (metaData != null) {
            if (StringUtils.isEmpty(metaData.getConsumerTrackingID())) {
                errors.add(new ErrorList("100", "RequestMetaData.ConsumerTrackingID", "ConsumerTrackingID cannot be blank"));
            }
            if (StringUtils.isEmpty(metaData.getCurrentBranch())) {
                errors.add(new ErrorList("100", "RequestMetaData.CurrentBranch", "CurrentBranch cannot be blank"));
            }
            if (StringUtils.isEmpty(metaData.getIpAddress())) {
                errors.add(new ErrorList("100", "RequestMetaData.IpAddress", "IpAddress cannot be blank"));
            }
            if (StringUtils.isEmpty(metaData.getOriginBranch())) {
                errors.add(new ErrorList("100", "RequestMetaData.OriginBranch", "OriginBranch cannot be blank"));
            }
            if (StringUtils.isEmpty(metaData.getRequestData())) {
                errors.add(new ErrorList("100", "RequestMetaData.RequestData", "RequestData cannot be blank"));
            }
            if (StringUtils.isEmpty(metaData.getRequestGeneratedDateTime())) {
                errors.add(new ErrorList("100", "RequestMetaData.RequestGeneratedDateTime", "RequestGeneratedDateTime cannot be blank"));
            }
            if (StringUtils.isEmpty(metaData.getRequestId())) {
                errors.add(new ErrorList("100", "RequestMetaData.RequestId", "RequestId cannot be blank"));
            }
            if (StringUtils.isEmpty(metaData.getRequestOrigin())) {
                errors.add(new ErrorList("100", "RequestMetaData.RequestOrigin", "RequestOrigin cannot be blank"));
            }
            if (StringUtils.isEmpty(metaData.getRequestReference())) {
                errors.add(new ErrorList("100", "RequestMetaData.RequestReference", "RequestReference cannot be blank"));
            }
            if (StringUtils.isEmpty(metaData.getRequestedService())) {
                errors.add(new ErrorList("100", "RequestMetaData.RequestedService", "RequestedService cannot be blank"));
            }
            if (StringUtils.isEmpty(metaData.getResponseData())) {
                errors.add(new ErrorList("100", "RequestMetaData.ResponseData", "ResponseData cannot be blank"));
            }
            if (StringUtils.isEmpty(metaData.getSourceCode())) {
                errors.add(new ErrorList("100", "RequestMetaData.SourceCode", "SourceCode cannot be blank"));
            }
            if (StringUtils.isEmpty(metaData.getUserName())) {
                errors.add(new ErrorList("100", "RequestMetaData.UserName", "UserName cannot be blank"));
            }
        }

        // Validate other fields
        if (StringUtils.isEmpty(request.getJwtToken())) {
            errors.add(new ErrorList("100", "JwtToken", "JwtToken cannot be blank"));
        }
        if (StringUtils.isEmpty(request.getClaimsTpReferenceNo())) {
            errors.add(new ErrorList("100", "ClaimsTpReferenceNo", "ClaimsTpReferenceNo cannot be blank"));
        }
        if (StringUtils.isEmpty(request.getFnolNo())) {
            errors.add(new ErrorList("100", "FnolNo", "FnolNo cannot be blank"));
        }
        if (StringUtils.isEmpty(request.getTpPolicyReferenceNo())) {
            errors.add(new ErrorList("100", "TpPolicyReferenceNo", "TpPolicyReferenceNo cannot be blank"));
        }
        if (StringUtils.isEmpty(request.getTransactionRefNo())) {
            errors.add(new ErrorList("100", "TransactionRefNo", "TransactionRefNo cannot be blank"));
        }

        return errors;
	}

	public List<ErrorList> validateLoginRequest(LoginRequest request) {
        List<ErrorList> errors = new ArrayList<>();

        if (request == null) {
            errors.add(new ErrorList("100", "LoginRequest", "LoginRequest cannot be null"));
            return errors;
        }

        if (StringUtils.isBlank(request.getLoginId())) {
            errors.add(new ErrorList("100", "LoginId", "LoginId cannot be blank"));
        }

        if (StringUtils.isBlank(request.getPassword())) {
            errors.add(new ErrorList("100", "Password", "Password cannot be blank"));
        }

        return errors;
    }

	public List<ErrorList> validateDeleteGarageDamageDetails(List<GarageSectionDetailsSaveReq> reqList) {
		List<ErrorList> list = new ArrayList<>();
	    int line = 1;
	    for (GarageSectionDetailsSaveReq req : reqList) {
	    	if(StringUtils.isNotBlank(req.getDamageSno())) {
	    		line = Integer.valueOf(req.getDamageSno());
			}
	    	
			if (StringUtils.isBlank(req.getClaimNo())) {
				list.add(new ErrorList("100", "ClaimNo", "Claim number cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getQuotationNo())) {
				list.add(new ErrorList("101", "QuotationNO", "Quotation number cannot be blank in line number : "+line));
			}

			if (StringUtils.isBlank(req.getDamageSno())) {
				list.add(new ErrorList("102", "DamageSno", "Damage Sno cannot be blank in line number : "+line));
			}

        
			line++;
		}
		return list;
	}
}
