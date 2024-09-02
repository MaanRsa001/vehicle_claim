package com.maan.veh.claim.serviceimpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maan.veh.claim.auth.passwordEnc;
import com.maan.veh.claim.entity.LoginMaster;
import com.maan.veh.claim.repository.LoginMasterRepository;
import com.maan.veh.claim.request.DamageSectionDetailsSaveReq;
import com.maan.veh.claim.request.LoginRequest;
import com.maan.veh.claim.response.ErrorList;
import com.maan.veh.claim.response.GarageWorkOrderSaveReq;

@Component
public class InputValidationUtil {
	
	@Autowired
	private LoginMasterRepository loginRepo;
	
	
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

       
		
		return list;
	}

	public List<ErrorList> validateDamageDetails(List<DamageSectionDetailsSaveReq> reqList) {
	    List<ErrorList> list = new ArrayList<>();
	    int line = 1;
	    for (DamageSectionDetailsSaveReq req : reqList) {
	    	
			if (StringUtils.isBlank(req.getClaimNo())) {
				list.add(new ErrorList("100", "ClaimNo", "Claim number cannot be blank in line number : "+line));
			}
//			if (StringUtils.isBlank(req.getDamageSno())) {
//				list.add(new ErrorList("101", "DamageSno", "Damage serial number cannot be blank in line number : "+line));
//			}
			if (StringUtils.isBlank(req.getDamageDirection())) {
				list.add(new ErrorList("102", "DamageDirection", "Damage direction cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getDamagePart())) {
				list.add(new ErrorList("103", "DamagePart", "Damage part cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getRepairReplace())) {
				list.add(new ErrorList("104", "RepairReplace", "Repair/Replace field cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getNoOfParts())) {
				list.add(new ErrorList("105", "NoOfParts", "Number of parts cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getGaragePrice())) {
				list.add(new ErrorList("106", "GaragePrice", "Garage price cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getDealerPrice())) {
				list.add(new ErrorList("107", "DealerPrice", "Dealer price cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getGarageLoginId())) {
				list.add(new ErrorList("108", "GarageLoginId", "Garage login ID cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getDealerLoginId())) {
				list.add(new ErrorList("109", "DealerLoginId", "Dealer login ID cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getSurveyorId())) {
				list.add(new ErrorList("110", "SurveyorId", "Surveyor ID cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getReplaceCost())) {
				list.add(new ErrorList("111", "ReplaceCost", "Replace cost cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getReplaceCostDeduct())) {
				list.add(new ErrorList("112", "ReplaceCostDeduct", "Replace cost deduction cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getSparepartDeprection())) {
				list.add(new ErrorList("113", "SparepartDeprection", "Spare part depreciation cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getDiscountSparepart())) {
				list.add(new ErrorList("114", "DiscountSparepart", "Discount on spare parts cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getTotamtReplace())) {
				list.add(new ErrorList("115", "TotamtReplace", "Total amount for replacement cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getLabourCost())) {
				list.add(new ErrorList("116", "LabourCost", "Labour cost cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getLabourCostDeduct())) {
				list.add(new ErrorList("117", "LabourCostDeduct", "Labour cost deduction cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getLabourDisc())) {
				list.add(new ErrorList("118", "LabourDisc", "Labour discount cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getTotamtOfLabour())) {
				list.add(new ErrorList("119", "TotamtOfLabour", "Total amount of labour cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getTotPrice())) {
				list.add(new ErrorList("120", "TotPrice", "Total price cannot be blank in line number : "+line));
			}
			if (StringUtils.isBlank(req.getStatus())) {
				list.add(new ErrorList("122", "Status", "Status cannot be blank in line number : "+line));
			}
			line++;
		}
		return list;
	}


	

}
