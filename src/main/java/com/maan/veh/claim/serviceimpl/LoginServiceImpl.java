package com.maan.veh.claim.serviceimpl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
import com.maan.veh.claim.dto.GarageLoginMasterDTO;
import com.maan.veh.claim.entity.BranchMaster;
import com.maan.veh.claim.entity.LoginMaster;
import com.maan.veh.claim.entity.LoginMasterId;
import com.maan.veh.claim.entity.LoginUserInfo;
import com.maan.veh.claim.entity.MenuMaster;
import com.maan.veh.claim.entity.SessionMaster;
import com.maan.veh.claim.repository.BranchMasterRepository;
import com.maan.veh.claim.repository.LoginMasterRepository;
import com.maan.veh.claim.repository.LoginUserInfoRepository;
import com.maan.veh.claim.repository.MenuMasterRepository;
import com.maan.veh.claim.repository.SessionMasterRepository;
import com.maan.veh.claim.request.LoginRequest;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.ErrorList;
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
	
	@Autowired
	private LoginUserInfoRepository LoginUserInfoRepo;
	
	@Autowired
	private InputValidationUtil validation;
	
	@Override
	public CommonResponse isValidUser(LoginRequest req) {
	    CommonResponse comResponse = new CommonResponse();
	    try {
	        // --- Authentication Logic ---
	        passwordEnc passEnc = new passwordEnc();
	        String password = passEnc.crypt(req.getPassword().trim());
	        LoginMaster login = loginRepo.findByLoginIdIgnoreCaseAndPasswordAndStatus(req.getLoginId(), password, "Y");
	        
	        if (login == null) {
	            comResponse.setMessage("Invalid login credentials");
	            comResponse.setResponse(Collections.emptyMap());
	            comResponse.setErrors(Collections.singletonList("User not found or inactive."));
	            comResponse.setIsError(true);
	            return comResponse;
	        }

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
	        session.setEndTime(endTime);
	        session = sessionRepo.save(session);
	    
	        List<BranchMaster> list = branchRepo.findByBranchCode(login.getBranchCode());
	        BranchMaster bm = list.stream()
	                               .max(Comparator.comparing(BranchMaster::getAmendId))
	                               .orElseThrow(() -> new RuntimeException("Branch not found"));
	        
	        LoginUserInfo userInfo = LoginUserInfoRepo.findByLoginId(login.getLoginId());
	        
	        // --- MenuList Block Start ---
	        List<MenuMaster> menu_master = menuMasterRepo.findByCompanyIdAndUsertypeIgnoreCase(login.getCompanyId(), login.getUserType());

	        // Step 1: Map each MenuMaster to a Map<String, Object>
	        Map<String, Map<String, Object>> menuMap = new HashMap<>();
	        for (MenuMaster m : menu_master) {
	            Map<String, Object> menu = new LinkedHashMap<>();
	            menu.put("link", StringUtils.isBlank(m.getMenuUrl()) ? "/" : m.getMenuUrl());
	            menu.put("title", m.getMenuName());
	            menu.put("icon", StringUtils.isBlank(m.getMenuLogo()) ? "" : m.getMenuLogo());
	            menu.put("id", m.getMenuId().toString());
	            menu.put("parent", StringUtils.isBlank(m.getParentMenu()) ? "99999" : m.getParentMenu());
	            menu.put("orderby", m.getDisplayOrder() != null ? m.getDisplayOrder() : 0);
	            menu.put("IsDesti", "Y".equalsIgnoreCase(m.getIsclick()));
	            menu.put("children", null); // To be populated later
	            menu.put("titleLocal", StringUtils.isBlank(m.getMenuNameLocal()) ? null : m.getMenuNameLocal());
	            menuMap.put(m.getMenuId().toString(), menu);
	        }

	        // Step 2: Assign children to their respective parents
	        List<Map<String, Object>> hierarchicalMenu = new ArrayList<>();
	        for (MenuMaster m : menu_master) {
	            String parentId = StringUtils.isBlank(m.getParentMenu()) ? "99999" : m.getParentMenu();
	            Map<String, Object> currentMenu = menuMap.get(m.getMenuId().toString());

	            if ("99999".equals(parentId)) {
	                // Root menu
	                hierarchicalMenu.add(currentMenu);
	            } else {
	                // Child menu
	                Map<String, Object> parentMenu = menuMap.get(parentId);
	                if (parentMenu != null) {
	                    List<Map<String, Object>> children = (List<Map<String, Object>>) parentMenu.get("children");
	                    if (children == null) {
	                        children = new ArrayList<>();
	                        parentMenu.put("children", children);
	                    }
	                    children.add(currentMenu);
	                }
	            }
	        }

	        // Step 3: Sort the hierarchical menu and its children based on 'orderby'
	        Comparator<Map<String, Object>> orderComparator = Comparator.comparingInt(menu -> (int) menu.get("orderby"));
	        hierarchicalMenu.sort(orderComparator);
	        for (Map<String, Object> menu : hierarchicalMenu) {
	            sortChildren(menu, orderComparator);
	        }

	        // Assign the hierarchical menu to resMenuList
	        List<LinkedHashMap<String, Object>> resMenuList = new ArrayList<>();
	        for (Map<String, Object> menu : hierarchicalMenu) {
	            resMenuList.add(new LinkedHashMap<>(menu));
	        }
	        // --- MenuList Block End ---

	        // --- Prepare the Response ---
	        LinkedHashMap<String, Object> response = new LinkedHashMap<>();
	        response.put("CompanyId", login.getCompanyId());
	        response.put("LoginId", login.getLoginId());
	        response.put("Token", session.getTempTokenid());
	        response.put("UserName",userInfo.getUserName());
	        response.put("UserType", login.getUserType());
	        response.put("BranchCode", bm.getBranchCode());
	        response.put("BranchName", bm.getBranchName());
	        response.put("MenuList", resMenuList);
	        
	        comResponse.setMessage("Success");
	        comResponse.setResponse(response);
	        comResponse.setErrors(Collections.emptyList());
	        comResponse.setIsError(false);
	        return comResponse;
	        
	    } catch (Exception e) {
	        log.error("Error validating user", e);
	        comResponse.setMessage("Failure");
	        comResponse.setResponse(Collections.emptyMap());
	        comResponse.setErrors(Collections.singletonList(e.getMessage()));
	        comResponse.setIsError(true);
	        return comResponse;
	    }
	}

	/**
	 * Helper method to recursively sort children menus based on 'orderby'.
	 * 
	 * @param menu The current menu item map.
	 * @param comparator Comparator to sort the menus.
	 */
	private void sortChildren(Map<String, Object> menu, Comparator<Map<String, Object>> comparator) {
	    List<Map<String, Object>> children = (List<Map<String, Object>>) menu.get("children");
	    if (children != null && !children.isEmpty()) {
	        children.sort(comparator);
	        for (Map<String, Object> child : children) {
	            sortChildren(child, comparator);
	        }
	    } else {
	        // Set 'children' to null if there are no children
	        menu.put("children", null);
	    }
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

	@Override
	public CommonResponse logout(LoginRequest req) {
		CommonResponse comResponse = new CommonResponse();
		try {
		
			LoginMaster login = loginRepo.findByLoginId(req.getLoginId());
			if (login!=null) {

				SessionMaster session = sessionRepo.findByTempTokenid(req.getToken());
				session.setLogoutDate(new Date());
				session.setStatus("DE-ACTIVE");
				session = sessionRepo.save(session);
				comResponse.setMessage("Log Out Sucessfully");
			}else {
				comResponse.setMessage("Log Out Failed");
			}
		} catch (Exception e) {
			comResponse.setMessage("Log Out Failed");
			e.printStackTrace();
		}
		comResponse.setResponse(Collections.emptyList());
		comResponse.setErrors(Collections.emptyList());
		comResponse.setIsError(false);

		return comResponse;
	}

	@Override
	public CommonResponse createGarageLogin(GarageLoginMasterDTO req) {
		CommonResponse comResponse = new CommonResponse();
	    try {
	    	List<ErrorList> errors = validation.validateGarageLogin(req);
			if(errors.size()>0){
				comResponse.setErrors(errors);
				comResponse.setIsError(true);
				comResponse.setMessage("User Not Found");
				comResponse.setResponse(Collections.EMPTY_LIST);
				return comResponse;
			}
			
			LoginMaster loginMaster = loginRepo.findByLoginId(req.getGarageId());
			
			if(loginMaster!=null && StringUtils.isNotBlank(req.getUserType())) {
				loginMaster.setCoreAppCode(req.getCoreAppCode());
				loginMaster.setBranchCode(req.getBranchCode());
				loginMaster.setUpdatedBy(req.getCreatedBy());
				loginMaster.setUpdatedDate(new Date());
				loginMaster.setStatus(req.getStatus());
				loginRepo.save(loginMaster);
			}else {
				LoginMaster loginMasterNew = new LoginMaster();
				loginMasterNew.setLoginId(req.getGarageId());
				loginMasterNew.setCoreAppCode(req.getCoreAppCode());
				loginMasterNew.setUserType("Garage");
				passwordEnc passEnc = new passwordEnc();
				String password = passEnc.crypt(req.getPassWord().trim());
				loginMasterNew.setPassword(password);
				loginMasterNew.setStatus(req.getStatus());
				loginMasterNew.setBranchCode(req.getBranchCode());
				loginMasterNew.setCompanyId(req.getCompanyId());
				loginMasterNew.setPwdCount("1");
				loginMasterNew.setLpassDate(new Date());
				loginMasterNew.setCreatedBy(req.getCreatedBy());
				loginMasterNew.setEntryDate(new Date());
			}
	        
			Map<String, String> response = new HashMap<>();
			
	        comResponse.setMessage("Success");
	        comResponse.setResponse(response);
	        comResponse.setErrors(Collections.emptyList());
	        comResponse.setIsError(false);
	        return comResponse;
	        
	    } catch (Exception e) {
	        log.error("Error validating user", e);
	        comResponse.setMessage("Failure");
	        comResponse.setResponse(Collections.emptyMap());
	        comResponse.setErrors(Collections.singletonList(e.getMessage()));
	        comResponse.setIsError(true);
	        return comResponse;
	    }
	}
	
}
