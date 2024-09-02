package com.maan.veh.claim.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maan.veh.claim.auth.passwordEnc;
import com.maan.veh.claim.entity.LoginMaster;
import com.maan.veh.claim.repository.LoginMasterRepository;
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
	

}
