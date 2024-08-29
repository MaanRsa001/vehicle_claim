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

}
