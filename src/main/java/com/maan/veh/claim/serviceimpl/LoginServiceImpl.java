package com.maan.veh.claim.serviceimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.maan.veh.claim.auth.EncryDecryService;
import com.maan.veh.claim.auth.JwtTokenUtil;
import com.maan.veh.claim.auth.passwordEnc;
import com.maan.veh.claim.entity.BranchMaster;
import com.maan.veh.claim.entity.LoginMaster;
import com.maan.veh.claim.entity.LoginMasterId;
import com.maan.veh.claim.entity.MenuMaster;
import com.maan.veh.claim.entity.SessionMaster;
import com.maan.veh.claim.repository.BranchMasterRepository;
import com.maan.veh.claim.repository.LoginMasterRepository;
import com.maan.veh.claim.repository.MenuMasterRepository;
import com.maan.veh.claim.repository.SessionMasterRepository;
import com.maan.veh.claim.request.LoginRequest;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.service.LoginService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Service
public class LoginServiceImpl implements LoginService,UserDetailsService{
	
	Logger log = LogManager.getLogger(LoginServiceImpl.class);
	
	@PersistenceContext
	private EntityManager em;
		
	@Autowired
	private JwtTokenUtil jwtTokenUtil;
	
	@Autowired
	private SessionMasterRepository sessionRepo;
	
	@Autowired
	private LoginMasterRepository loginRepo;
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired
	private EncryDecryService endecryService;

	@Autowired
	private BranchMasterRepository branchRepo; ;
	
	@Autowired
	private MenuMasterRepository menuMasterRepo;
	
	@Override
	public CommonResponse isValidUser(LoginRequest req) {
		CommonResponse comResponse = new CommonResponse();
		try {
			passwordEnc passEnc = new passwordEnc();
			String password = passEnc.crypt(req.getPassword().trim());
			LoginMaster login =loginRepo.findByLoginIdIgnoreCaseAndPasswordAndStatus(req.getLoginId(),password,"Y");
			String token = jwtTokenUtil.doGenerateToken(req.getLoginId());
			log.info("-----token------" + token);
			SessionMaster session = new SessionMaster();
			session.setLoginId(req.getLoginId());
			session.setTokenId(token);
			session.setStatus("ACTIVE");
			String temptoken = bCryptPasswordEncoder.encode("vehicle_claim");
			session.setTempTokenid(temptoken);
			session.setUserType(login.getUserType());
			Date today = new Date(); 
			session.setEntryDate(today);
			session.setStartTime(today);
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MINUTE, 50);
			Date endTime = cal.getTime();
			session.setEndTime(endTime );
			session =sessionRepo.save(session);
		
			List<BranchMaster> list = branchRepo.findByBranchCode(login.getBranchCode());
			BranchMaster bm =list.stream().max((a,b) -> a.getAmendId().compareTo(b.getAmendId())).get();
			
			List<MenuMaster> menu_master =menuMasterRepo.findByCompanyIdAndUsertypeIgnoreCase(login.getCompanyId(),login.getUserType());
			List<MenuMaster> parent_menu = menu_master.stream().filter(p ->"99999".equals(p.getParentMenu()))
					.sorted((a,b) -> a.getDisplayOrder().compareTo(b.getDisplayOrder()))
					.collect(Collectors.toList());
		
			List<LinkedHashMap<String,Object>> resMenuList = new ArrayList<>();
			for(MenuMaster m : parent_menu) {
				LinkedHashMap<String,Object> menu = new LinkedHashMap<String,Object>();
				menu.put("ParentMenuName", m.getMenuName());
				Predicate<MenuMaster> predicate = sm -> sm.getParentMenu().equals(m.getMenuId().toString());
				List<Map<String,Object>> filterMenu = menu_master.stream().filter(predicate).map(map ->{
					Map<String,Object> mm = new HashMap<>();
					mm.put("ChildMenuName", StringUtils.isBlank(map.getMenuName())?"":map.getMenuName());
					return mm;
				})
						
				.collect(Collectors.toList());
				
				
				menu.put("ChildMenuYn", filterMenu.size()>0?true:false);
				menu.put("ChildMenuList", filterMenu);
				resMenuList.add(menu);

			}
			
			
			
			LinkedHashMap<String,Object> response = new LinkedHashMap<>();
			response.put("LoginId", login.getLoginId());
			response.put("Token", session.getTempTokenid());
			response.put("UserType", login.getUserType());
			response.put("BranchCode",bm.getBranchCode());
			response.put("BranchName", bm.getBranchName());
			response.put("MenuList", resMenuList);
			
			comResponse.setMessage("Success");
			comResponse.setResponse(response);
			comResponse.setErrors(Collections.EMPTY_LIST);
			return comResponse;
			
		}catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	@SuppressWarnings("static-access")
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		LoginMaster userList = new LoginMaster ();
		try {
			log.info("loadUserByUsername==>" + username);
			
			String[] split = username.split(":");
			
			LoginMasterId id = new LoginMasterId();
			id.setLoginId(split[0]);
			
			LoginMaster  userListopt = loginRepo.findByLoginId(split[0]);
			 if(userListopt!=null) {
				 userList = userListopt;
			 }
			if (userList!=null) {
				//user = userList.get(0);
				String pass = bCryptPasswordEncoder.encode(endecryService.decrypt("zQYgCo7GMZeX1tBQyzAi8Q=="));
				userList.setPassword(pass);
				Set<GrantedAuthority> grantedAuthorities = new HashSet<GrantedAuthority>();
				grantedAuthorities.add(new SimpleGrantedAuthority("ADMIN"));
				log.info("loadUserByTokenEncrypt==>" + userList.getPassword());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return new org.springframework.security.core.userdetails.User(userList.getLoginId(), userList.getPassword(),getAuthority());
	}
	
	private List<SimpleGrantedAuthority> getAuthority() {
		return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
	}
	
}
