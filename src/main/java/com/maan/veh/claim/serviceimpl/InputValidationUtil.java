package com.maan.veh.claim.serviceimpl;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maan.veh.claim.auth.passwordEnc;
import com.maan.veh.claim.dto.GarageLoginMasterDTO;
import com.maan.veh.claim.entity.GarageWorkOrder;
import com.maan.veh.claim.entity.LoginMaster;
import com.maan.veh.claim.entity.VcFlowMaster;
import com.maan.veh.claim.file.DocumentUploadDetailsReqRes;
import com.maan.veh.claim.repository.GarageWorkOrderRepository;
import com.maan.veh.claim.repository.LoginMasterRepository;
import com.maan.veh.claim.repository.VcFlowMasterRepository;
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
import com.maan.veh.claim.request.InsuranceCompanyMasterRequest;
import com.maan.veh.claim.request.LoginRequest;
import com.maan.veh.claim.request.SaveClaimRequest;
import com.maan.veh.claim.request.TotalAmountDetailsRequest;
import com.maan.veh.claim.request.VcSparePartsDetailsRequest;
import com.maan.veh.claim.response.ErrorList;
import com.maan.veh.claim.response.GarageWorkOrderSaveReq;

@Component
public class InputValidationUtil {
	
	@Autowired
	private LoginMasterRepository loginRepo;
	
	@Autowired
    private GarageWorkOrderRepository garageWorkOrderRepository;
	
	@Autowired
    private VcFlowMasterRepository flowMasterRepo;
	
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
		
	}
	
	public List<ErrorList> validateWorkOrder(GarageWorkOrderSaveReq req) {
	    List<ErrorList> list = new ArrayList<>();

	    // Existing validations
	    if (StringUtils.isBlank(req.getClaimNo())) {
	        list.add(new ErrorList("100", "Claim number", "Claim number cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getWorkOrderNo())) {
	        list.add(new ErrorList("100", "WorkOrderNo", "Work order number cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getWorkOrderType())) {
	        //list.add(new ErrorList("100", "WorkOrderType", "Work order type cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getSettlementType())) {
	        //list.add(new ErrorList("100", "SettlementType", "Settlement type cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getSettlementTo())) {
	       // list.add(new ErrorList("100", "SettlementTo", "Settlement to cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getLocation())) {
	        list.add(new ErrorList("100", "Location", "Location cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getRepairType())) {
	        list.add(new ErrorList("100", "RepairType", "Repair type cannot be blank"));
	    }

	    if (StringUtils.isNotBlank(req.getSparepartsDealerId())) {
	        if (StringUtils.isBlank(req.getQuotationNo())) {
	            list.add(new ErrorList("100", "QuotationNo", "Quotation number cannot be blank"));
	        }
	    }

	    if (StringUtils.isBlank(req.getLossType())) {
	        //list.add(new ErrorList("100", "LossType", "Loss type cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getCreatedBy())) {
	        list.add(new ErrorList("100", "CreatedBy", "Created by cannot be blank"));
	    }
	    
	    if (StringUtils.isBlank(req.getQuoteStatus())) {
	        list.add(new ErrorList("100", "Status", "status cannot be blank"));
	    }
	    
	    if (StringUtils.isBlank(req.getRemarks())) {
	        list.add(new ErrorList("100", "Remarks", "remarks cannot be blank"));
	    }

	    Date workOrderDate = req.getWorkOrderDate();
	    Date deliveryDate = req.getDeliveryDate();

	    // Parse and validate workOrderDate
	    if (req.getWorkOrderDate() == null) {
	    	list.add(new ErrorList("100", "WorkOrderDate", "Work order date is invalid or not in the correct format (dd/MM/yyyy)"));
	    }
	    if (req.getDeliveryDate() == null) {
	    	 list.add(new ErrorList("100", "DeliveryDate", "Delivery Date is invalid or not in the correct format (dd/MM/yyyy)"));
	    }
	   

	    // Validate deliveryDate is not before workOrderDate
	    if (workOrderDate != null && deliveryDate != null) {
	        if (deliveryDate.before(workOrderDate)) {
	            list.add(new ErrorList("101", "DeliveryDate", "Delivery date must not be less than work order date"));
	        }
	    }

	    // Validate totalLoss
	    try {
	        if (!StringUtils.isBlank(req.getTotalLoss())) {
	            new BigDecimal(req.getTotalLoss());
	        }
	    } catch (NumberFormatException e) {
	        list.add(new ErrorList("100", "TotalLoss", "Total loss must be a valid number"));
	    }
	    
	    // Status check block
	    try {
	        Optional<GarageWorkOrder> optional = garageWorkOrderRepository.findByClaimNoAndGarageId(req.getClaimNo(),req.getGarageId());
	        
	        if (optional.isPresent()) {
	            String quoteStatus = optional.get().getQuoteStatus();
	            
	            // Dynamically retrieve flowList based on usertype and quoteStatus
	            List<VcFlowMaster> flowList = flowMasterRepo.findByStatusId(quoteStatus);
	            
	            // Extract the list of valid status IDs dynamically
	            Set<String> validStatusIds = flowList.stream()
	                                                 .map(VcFlowMaster::getSubStatus)
	                                                 .collect(Collectors.toSet());
	            
	            // Check if the requested quote status is valid
	            if (!validStatusIds.contains(req.getQuoteStatus())) {
	            	List<VcFlowMaster> flowListStatus = flowMasterRepo.findByStatusId(req.getQuoteStatus());
	            	String stausDesc = (flowListStatus!=null && flowListStatus.size()>0) ? flowListStatus.get(0).getStatusDescription() : "Current Status";
	                list.add(new ErrorList("101", "Status", 
	                        String.format("The Status cannot be %s", stausDesc)));
	            }
	        }else {
                String quoteStatus = "PFG";
	            
	            // Dynamically retrieve flowList based on usertype and quoteStatus
	            List<VcFlowMaster> flowList = flowMasterRepo.findByUsertypeAndStatusId("Garage", quoteStatus);
	            
	            // Extract the list of valid status IDs dynamically
	            Set<String> validStatusIds = flowList.stream()
	                                                 .map(VcFlowMaster::getSubStatus)
	                                                 .collect(Collectors.toSet());
	            
	            // Check if the requested quote status is valid
	            if (!validStatusIds.contains(req.getQuoteStatus())) {
	            	List<VcFlowMaster> flowListStatus = flowMasterRepo.findByStatusId(req.getQuoteStatus());
	            	String stausDesc = (flowListStatus!=null && flowListStatus.size()>0) ? flowListStatus.get(0).getStatusDescription() : "Current Status";
	                list.add(new ErrorList("101", "Status", 
	                        String.format("The Status cannot be %s", stausDesc)));
	            }
	        }
	    } catch (Exception ex) {
	        //log.error("Exception during status check: {}", ex.getMessage(), ex);
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
				
				if (StringUtils.isBlank(req.getUnitPrice())) {
					list.add(new ErrorList("106", "UnitPrice", "Unit price cannot be blank in line number : "+line));
				}
				
			}
			
			if (StringUtils.isBlank(req.getNoOfUnits())) {
				list.add(new ErrorList("105", "NoOfUnits", "Number of Units cannot be blank in line number : "+line));
			}
			
			if (StringUtils.isBlank(req.getReplacementCharge())) {
				list.add(new ErrorList("111", "LabourCharge", "Labour Charge cannot be blank in line number : "+line));
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
//	        if (StringUtils.isBlank(metaData.getRequestGeneratedDateTime())) {
//	            errors.add(new ErrorList("100", "RequestMetaData.RequestGeneratedDateTime", "RequestGeneratedDateTime cannot be blank"));
//	        }
	        
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
//	    if (StringUtils.isBlank(request.getLossDate())) {
//	        errors.add(new ErrorList("100", "LossDate", "LossDate cannot be blank"));
//	    }
//	    if (StringUtils.isBlank(request.getIntimatedDate())) {
//	        errors.add(new ErrorList("100", "IntimatedDate", "IntimatedDate cannot be blank"));
//	    }
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

	public List<ErrorList> validateCreateCompany(InsuranceCompanyMasterRequest req) {
	    List<ErrorList> list = new ArrayList<>();

	    // Validate mandatory fields
	    if (StringUtils.isBlank(req.getCompanyId())) {
	        list.add(new ErrorList("100", "CompanyId", "Company ID cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getEffectiveDateStart())) {
	        list.add(new ErrorList("100", "EffectiveDateStart", "Effective start date cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getCompanyName())) {
	        list.add(new ErrorList("100", "CompanyName", "Company name cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getCoreAppCode())) {
	        list.add(new ErrorList("100", "CoreAppCode", "Core app code cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getCreatedBy())) {
	        list.add(new ErrorList("100", "CreatedBy", "Created by cannot be blank"));
	    }

	    // Email validation
	    if (StringUtils.isNotBlank(req.getCompanyEmail()) && !EmailValidator.getInstance().isValid(req.getCompanyEmail())) {
	        list.add(new ErrorList("100", "CompanyEmail", "Company email is not valid"));
	    }

	    // Phone validation
	    if (StringUtils.isNotBlank(req.getCompanyPhone()) && !req.getCompanyPhone().matches("^\\+?[0-9]{7,15}$")) {
	        list.add(new ErrorList("100", "CompanyPhone", "Company phone must contain only numbers and optional '+' at the beginning"));
	    }

	    // Date validation for EffectiveDateStart
	    Date effectiveDateStart = null;
	    try {
	        if (StringUtils.isNotBlank(req.getEffectiveDateStart())) {
	            effectiveDateStart = DD_MM_YYYY.parse(req.getEffectiveDateStart());
	        }
	    } catch (ParseException e) {
	        list.add(new ErrorList("100", "EffectiveDateStart", "Effective start date is invalid or not in the correct format (dd/MM/yyyy)"));
	    }

	    // Validate Status as a single character
	    if (StringUtils.isNotBlank(req.getStatus()) && req.getStatus().length() > 1) {
	        list.add(new ErrorList("100", "Status", "Status must be a single character"));
	    }


	    return list;
	}

	public List<ErrorList> validateLogin(GarageLoginMasterDTO req) {
	    List<ErrorList> list = new ArrayList<>();

	    // Validate mandatory fields
	    if (StringUtils.isBlank(req.getUserType())) {
	        list.add(new ErrorList("100", "UserType", "User Type cannot be blank"));
	    }
	    if (StringUtils.isBlank(req.getLoginName())) {
		        list.add(new ErrorList("100", "Loginname", "Login name cannot be blank"));
		}

	    if (StringUtils.isBlank(req.getLoginId())) {
	        list.add(new ErrorList("100", "Loginid", "Login ID cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getCompanyId())) {
	        list.add(new ErrorList("100", "CompanyId", "Company ID cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getCoreAppCode())) {
	        list.add(new ErrorList("100", "CoreAppCode", "Core App Code cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getAddress())) {
	        list.add(new ErrorList("100", "Address", "Address cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getCityName())) {
	        list.add(new ErrorList("100", "CityName", "City name cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getStatus())) {
	        list.add(new ErrorList("100", "Status", "Status cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getPassWord())) {
	        list.add(new ErrorList("100", "PassWord", "Password cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getRepassWord())) {
	        list.add(new ErrorList("100", "RePassWord", "Re-entered password cannot be blank"));
	    } else if (!req.getPassWord().equals(req.getRepassWord())) {
	        list.add(new ErrorList("101", "RePassWord", "Passwords do not match"));
	    }

	    if (StringUtils.isBlank(req.getBranchCode())) {
	        list.add(new ErrorList("100", "BranchCode", "Branch code cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getGarageAddress())) {
	        list.add(new ErrorList("100", "Garageaddress", "Garage address cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getStateName())) {
	        //list.add(new ErrorList("100", "Statename", "State name cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getContactPersonName())) {
	        list.add(new ErrorList("100", "Contactpersonname", "Contact person name cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getMobileNo())) {
	        list.add(new ErrorList("100", "Mobileno", "Mobile number cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getEmailid())) {
	        list.add(new ErrorList("100", "Emailid", "Email ID cannot be blank"));
	    } else if (!EmailValidator.getInstance().isValid(req.getEmailid())) {
	        list.add(new ErrorList("101", "Emailid", "Invalid email format"));
	    }

	    if (req.getEffectiveDate() == null) {
	        list.add(new ErrorList("100", "Effectivedate", "Effective date cannot be blank"));
	    }

	    // Additional rule: effectiveDate should not be before entryDate
	    if (req.getEffectiveDate() != null) {
	        Date today = new Date();
	        // Remove time component by setting hours, minutes, seconds, and milliseconds to zero.
	        Calendar calendar = Calendar.getInstance();

	        // Process the request's effective date.
	        calendar.setTime(req.getEffectiveDate());
	        calendar.set(Calendar.HOUR_OF_DAY, 0);
	        calendar.set(Calendar.MINUTE, 0);
	        calendar.set(Calendar.SECOND, 0);
	        calendar.set(Calendar.MILLISECOND, 0);
	        Date effectiveDateOnly = calendar.getTime();

	        // Process today's date.
	        calendar.setTime(today);
	        calendar.set(Calendar.HOUR_OF_DAY, 0);
	        calendar.set(Calendar.MINUTE, 0);
	        calendar.set(Calendar.SECOND, 0);
	        calendar.set(Calendar.MILLISECOND, 0);
	        Date todayOnly = calendar.getTime();

	        // Validate the effective date against today's date.
	        if (effectiveDateOnly.before(todayOnly)) {
	            list.add(new ErrorList("101", "Effectivedate", "Effective date cannot be before entry date"));
	        }
	    }
	    
	    // New validation for duplicate loginId and coreAppCode when oaCode is provided
	    if (StringUtils.isBlank(req.getOaCode())) {
	        // Check for duplicate loginId
	        LoginMaster existingLogin = loginRepo.findByLoginId(req.getLoginId());
	        if (existingLogin != null) {
	            list.add(new ErrorList("102", "LoginId", "Login ID already exists"));
	        }

	        // Check for duplicate coreAppCode
	        LoginMaster existingCoreAppCode = loginRepo.findByCoreAppCode(req.getCoreAppCode());
	        if (existingCoreAppCode != null) {
	            list.add(new ErrorList("102", "CoreAppCode", "Core App Code already exists"));
	        }
	    }


	    return list;
	}
	
	public List<ErrorList> validateDocumentUploadDetails(DocumentUploadDetailsReqRes req) {
	    List<ErrorList> list = new ArrayList<>();

	    // Validate mandatory fields
	    if (StringUtils.isBlank(req.getClaimNo())) {
	        list.add(new ErrorList("100", "ClaimNo", "Claim number cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getDocumentRef())) {
	       // list.add(new ErrorList("100", "DocumentRef", "Document reference cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getDocTypeId())) {
	        list.add(new ErrorList("100", "DocTypeId", "Document type ID cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getCompanyId())) {
	        list.add(new ErrorList("100", "CompanyId", "Company ID cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getFilePathName())) {
	       // list.add(new ErrorList("100", "FilePathName", "File path name cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getFileName())) {
	        list.add(new ErrorList("100", "FileName", "File name cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getUploadType())) {
	        list.add(new ErrorList("100", "UploadType", "Upload type cannot be blank"));
	    }

	    if (StringUtils.isBlank(req.getUploadedBy())) {
	        list.add(new ErrorList("100", "UploadedBy", "Uploader's name cannot be blank"));
	    }

	    // Optional field validations
	    if (StringUtils.isNotBlank(req.getErrorRes()) && req.getErrorRes().length() > 255) {
	        list.add(new ErrorList("101", "ErrorRes", "Error response text is too long"));
	    }

	    if (StringUtils.isNotBlank(req.getImgUrl()) && !isValidUrl(req.getImgUrl())) {
	        list.add(new ErrorList("101", "ImgUrl", "Invalid URL format for ImgUrl"));
	    }

	    if (StringUtils.isNotBlank(req.getFileType()) && !isValidFileType(req.getFileType())) {
	        list.add(new ErrorList("101", "FileType", "Unsupported file type"));
	    }

	    return list;
	}

	// Helper method to validate URL format
	private boolean isValidUrl(String url) {
	    try {
	        new java.net.URL(url).toURI();
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}

	// Helper method to validate file types (customize as needed)
	private boolean isValidFileType(String fileType) {
	    return List.of("pdf", "jpeg", "png", "docx").contains(fileType.toLowerCase());
	}

    public List<ErrorList> validateSaveSpareParts(VcSparePartsDetailsRequest req) {
	    List<ErrorList> errors = new ArrayList<>();

	    // Validate mandatory fields
	    if (StringUtils.isBlank(req.getClaimNo())) {
	        errors.add(new ErrorList("100", "ClaimNumber", "Claim number cannot be blank"));
	    }
	    if (StringUtils.isBlank(req.getQuotationNo())) {
	        errors.add(new ErrorList("100", "QuotationNumber", "Quotation number cannot be blank"));
	    }
	    if (StringUtils.isBlank(req.getGarageId())) {
	        errors.add(new ErrorList("100", "GarageId", "GarageId cannot be blank"));
	    }

	    // Optional fields that must be valid numbers if provided
	    validateDecimalField(req.getReplacementCost(), "ReplacementCost", errors);
	    validateDecimalField(req.getReplacementCostDeductible(), "ReplacementCostDeductible", errors);
	    validateDecimalField(req.getSparePartDepreciation(), "SparePartDepreciation", errors);
	    validateDecimalField(req.getDiscountOnSpareParts(), "DiscountOnSpareParts", errors);
	    validateDecimalField(req.getTotalAmountReplacement(), "TotalAmountReplacement", errors);
	    validateDecimalField(req.getRepairLabour(), "RepairLabour", errors);
	    validateDecimalField(req.getRepairLabourDeductible(), "RepairLabourDeductible", errors);
	    validateDecimalField(req.getRepairLabourDiscountAmount(), "RepairLabourDiscountAmount", errors);
	    validateDecimalField(req.getTotalAmountRepairLabour(), "TotalAmountRepairLabour", errors);
	    validateDecimalField(req.getNetAmount(), "NetAmount", errors);
	    validateDecimalField(req.getUnknownAccidentDeduction(), "UnknownAccidentDeduction", errors);
	    validateDecimalField(req.getAmountToBeRecovered(), "AmountToBeRecovered", errors);
	    validateDecimalField(req.getTotalAfterDeductions(), "TotalAfterDeductions", errors);
	    validateDecimalField(req.getVatRatePer(), "VatRatePer", errors);
	    validateDecimalField(req.getVatRate(), "VatRate", errors);
	    validateDecimalField(req.getVatAmount(), "VatAmount", errors);
	    validateDecimalField(req.getTotalWithVAT(), "TotalWithVAT", errors);
	    validateDecimalField(req.getSalvageDeduction(),"SalvageDeduction", errors);
	    
	    // Additional validation rules
	    try {
	        BigDecimal sparePartDepreciation = toBigDecimal(req.getSparePartDepreciation());
	        BigDecimal discountOnSpareParts = toBigDecimal(req.getDiscountOnSpareParts());
	        BigDecimal replacementCost = toBigDecimal(req.getReplacementCost());

	        if (sparePartDepreciation.add(discountOnSpareParts).compareTo(replacementCost) > 0) {
	            errors.add(new ErrorList("102", "SparePartDepreciation & DiscountOnSpareParts",
	                "SparePartDepreciation + DiscountOnSpareParts should not be greater than ReplacementCost"));
	        }

	        BigDecimal repairLabourDiscountAmount = toBigDecimal(req.getRepairLabourDiscountAmount());
	        BigDecimal repairLabour = toBigDecimal(req.getRepairLabour());

	        if (repairLabourDiscountAmount.compareTo(repairLabour) > 0) {
	            errors.add(new ErrorList("103", "RepairLabourDiscountAmount",
	                "RepairLabourDiscountAmount should not be greater than RepairLabour"));
	        }

	        BigDecimal replacementCostDeductible = toBigDecimal(req.getReplacementCostDeductible());
	        BigDecimal repairLabourDeductible = toBigDecimal(req.getRepairLabourDeductible());
	        BigDecimal unknownAccidentDeduction = toBigDecimal(req.getUnknownAccidentDeduction());
	        BigDecimal salvageDeduction = toBigDecimal(req.getSalvageDeduction());
	        BigDecimal totalAmountReplacement = toBigDecimal(req.getTotalAmountReplacement());
	        BigDecimal totalAmountRepairLabour = toBigDecimal(req.getTotalAmountRepairLabour());

	        BigDecimal totalDeductions = replacementCostDeductible.add(repairLabourDeductible)
	            .add(unknownAccidentDeduction).add(salvageDeduction);

	        BigDecimal totalLabourReplacement = totalAmountReplacement.add(totalAmountRepairLabour);

	        if (totalDeductions.compareTo(totalLabourReplacement) > 0) {
	            errors.add(new ErrorList("104", "Deductions",
	                "ReplacementCostDeductible + RepairLabourDeductible + UnknownAccidentDeduction + SalvageDeduction " +
	                "should not be greater than TotalAmountReplacement + TotalAmountRepairLabour"));
	        }

	    } catch (NumberFormatException e) {
	        errors.add(new ErrorList("105", "ValidationError", "Invalid number format in request fields"));
	    }

	    return errors;
	}

	private BigDecimal toBigDecimal(String value) {
		if (StringUtils.isBlank(value)) {
			return BigDecimal.ZERO; // Default to zero if value is blank or null
		}
		try {
			return new BigDecimal(value); // Attempt to parse the string
		} catch (NumberFormatException e) {
			throw new IllegalArgumentException("Invalid decimal value: " + value, e);
		}
	}


	private void validateDecimalField(String value, String fieldName, List<ErrorList> errors) {
        if (StringUtils.isBlank(value)) {
            // Field is blank, so we add a blank validation error
            errors.add(new ErrorList("100", fieldName, fieldName + " cannot be blank"));
        } else {
            try {
                new BigDecimal(value);  // Validates if value is a valid decimal
            } catch (NumberFormatException e) {
                errors.add(new ErrorList("101", fieldName, fieldName + " must be a valid decimal number"));
            }
        }
        
     
    }

	public List<ErrorList> validateAssignWorkOrder(GarageWorkOrderSaveReq req) {
		List<ErrorList> list = new ArrayList<>();

		// Existing validations
		if (StringUtils.isBlank(req.getClaimNo())) {
		    list.add(new ErrorList("100", "Claim number", "Claim number cannot be blank"));
		}

		if (StringUtils.isBlank(req.getWorkOrderNo())) {
		    list.add(new ErrorList("100", "WorkOrderNo", "Work order number cannot be blank"));
		}

		if (StringUtils.isBlank(req.getWorkOrderType())) {
		    list.add(new ErrorList("100", "WorkOrderType", "Work order type cannot be blank"));
		}

		if (StringUtils.isBlank(req.getSettlementType())) {
		    list.add(new ErrorList("100", "SettlementType", "Settlement type cannot be blank"));
		}

		if (StringUtils.isBlank(req.getSettlementTo())) {
		    list.add(new ErrorList("100", "SettlementTo", "Settlement to cannot be blank"));
		}

		if (StringUtils.isBlank(req.getGarageName())) {
		    list.add(new ErrorList("100", "GarageName", "Garage name cannot be blank"));
		}

		if (StringUtils.isBlank(req.getGarageId())) {
		    list.add(new ErrorList("100", "GarageId", "Garage ID cannot be blank"));
		}

		if (StringUtils.isBlank(req.getLocation())) {
		    list.add(new ErrorList("100", "Location", "Location cannot be blank"));
		}

		if (StringUtils.isBlank(req.getRepairType())) {
		    list.add(new ErrorList("100", "RepairType", "Repair type cannot be blank"));
		}

		if (StringUtils.isNotBlank(req.getSparepartsDealerId())) {
		    if (StringUtils.isBlank(req.getQuotationNo())) {
		        list.add(new ErrorList("100", "QuotationNo", "Quotation number cannot be blank"));
		    }
		}

		if (StringUtils.isBlank(req.getLossType())) {
		   // list.add(new ErrorList("100", "LossType", "Loss type cannot be blank"));
		}

		if (StringUtils.isBlank(req.getCreatedBy())) {
		    list.add(new ErrorList("100", "CreatedBy", "Created by cannot be blank"));
		}

		if (StringUtils.isBlank(req.getUpdatedBy())) {
		    list.add(new ErrorList("100", "UpdatedBy", "Updated by cannot be blank"));
		}

		if (StringUtils.isBlank(req.getQuoteStatus())) {
		    list.add(new ErrorList("100", "QuoteStatus", "Quote status cannot be blank"));
		}

		if (StringUtils.isBlank(req.getRemarks())) {
		    list.add(new ErrorList("100", "Remarks", "Remarks cannot be blank"));
		}

		if (StringUtils.isBlank(req.getUserType())) {
		    list.add(new ErrorList("100", "UserType", "User type cannot be blank"));
		}

		// Date validations
		Date workOrderDate = req.getWorkOrderDate();
		Date deliveryDate = req.getDeliveryDate();

		if (workOrderDate == null) {
		    list.add(new ErrorList("100", "WorkOrderDate", "Work order date is invalid or not in the correct format (dd/MM/yyyy)"));
		}
		if (deliveryDate == null) {
		    list.add(new ErrorList("100", "DeliveryDate", "Delivery Date is invalid or not in the correct format (dd/MM/yyyy)"));
		}
		if (workOrderDate != null && deliveryDate != null && deliveryDate.before(workOrderDate)) {
		    list.add(new ErrorList("101", "DeliveryDate", "Delivery date must not be less than work order date"));
		}

		// TotalLoss validation
		try {
		    if (!StringUtils.isBlank(req.getTotalLoss())) {
		        new BigDecimal(req.getTotalLoss());
		    }
		} catch (NumberFormatException e) {
		    list.add(new ErrorList("100", "TotalLoss", "Total loss must be a valid number"));
		}

		// JointOrderYn and SubrogationYn validations
		if (StringUtils.isBlank(req.getJointOrderYn())) {
		    list.add(new ErrorList("100", "JointOrderYn", "Joint order indicator cannot be blank"));
		}
		if (StringUtils.isBlank(req.getSubrogationYn())) {
		    list.add(new ErrorList("100", "SubrogationYn", "Subrogation indicator cannot be blank"));
		}

	    
	    // Status check block
	    try {
	        Optional<GarageWorkOrder> optional = garageWorkOrderRepository.findByClaimNoAndGarageId(req.getClaimNo(),req.getGarageId());
	        
	        if (optional.isPresent()) {
	            String quoteStatus = optional.get().getQuoteStatus();
	            
	            // Dynamically retrieve flowList based on usertype and quoteStatus
	            List<VcFlowMaster> flowList = flowMasterRepo.findByStatusId(quoteStatus);
	            
	            // Extract the list of valid status IDs dynamically
	            Set<String> validStatusIds = flowList.stream()
	                                                 .map(VcFlowMaster::getSubStatus)
	                                                 .collect(Collectors.toSet());
	            
	            // Check if the requested quote status is valid
	            if (!validStatusIds.contains(req.getQuoteStatus())) {
	            	List<VcFlowMaster> flowListStatus = flowMasterRepo.findByStatusId(req.getQuoteStatus());
	            	String stausDesc = (flowListStatus!=null && flowListStatus.size()>0) ? flowListStatus.get(0).getStatusDescription() : "Current Status";
	                list.add(new ErrorList("101", "Status", 
	                        String.format("The Status cannot be %s", stausDesc)));
	            }
	        }else {
                String quoteStatus = "PFG";
	            
	            // Dynamically retrieve flowList based on usertype and quoteStatus
	            List<VcFlowMaster> flowList = flowMasterRepo.findByUsertypeAndStatusId("Garage", quoteStatus);
	            
	            // Extract the list of valid status IDs dynamically
	            Set<String> validStatusIds = flowList.stream()
	                                                 .map(VcFlowMaster::getSubStatus)
	                                                 .collect(Collectors.toSet());
	            
	            // Check if the requested quote status is valid
	            if (!validStatusIds.contains(req.getQuoteStatus())) {
	            	List<VcFlowMaster> flowListStatus = flowMasterRepo.findByStatusId(req.getQuoteStatus());
	            	String stausDesc = (flowListStatus!=null && flowListStatus.size()>0) ? flowListStatus.get(0).getStatusDescription() : "Current Status";
	                list.add(new ErrorList("101", "Status", 
	                        String.format("The Status cannot be %s", stausDesc)));
	            }
	        }
	    } catch (Exception ex) {
	        //log.error("Exception during status check: {}", ex.getMessage(), ex);
	    }


	    return list;
	}

	
}
