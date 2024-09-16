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
import com.maan.veh.claim.request.DamageSectionDetailsSaveReq;
import com.maan.veh.claim.request.DealerSectionDetailsSaveReq;
import com.maan.veh.claim.request.GarageSectionDetailsSaveReq;
import com.maan.veh.claim.request.LoginRequest;
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

        // Validate and set garageName
        if (StringUtils.isBlank(req.getGarageName())) {
            list.add(new ErrorList("100","GarageName","Garage name cannot be blank"));

        }

        // Validate and set location
        if (StringUtils.isBlank(req.getLocation())) {
            list.add(new ErrorList("100","Location","Location cannot be blank"));

        }

        // Validate and set repairType
        if (StringUtils.isBlank(req.getRepairType())) {
            list.add(new ErrorList("100","RepairType","Repair type cannot be blank"));

        }

        // Validate and set quotationNo
        if (StringUtils.isBlank(req.getQuotationNo())) {
            list.add(new ErrorList("100","QuotationNo","Quotation number cannot be blank"));

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
	


}
