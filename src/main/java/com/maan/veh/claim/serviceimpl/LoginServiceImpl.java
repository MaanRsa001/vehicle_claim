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
import com.maan.veh.claim.request.GetAllLoginRequest;
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
	public CommonResponse createLogin(GarageLoginMasterDTO req) {
	    CommonResponse comResponse = new CommonResponse();
	    try {
	        // Validate request
	        List<ErrorList> errors = validation.validateLogin(req);
	        if (errors.size() > 0) {
	            comResponse.setErrors(errors);
	            comResponse.setIsError(true);
	            comResponse.setMessage("Validation Errors");
	            comResponse.setResponse(Collections.EMPTY_LIST);
	            return comResponse;
	        }

	        // Fetch existing data
	        LoginMaster loginMaster = loginRepo.findByLoginId(req.getLoginId());
	        LoginUserInfo userInfo = LoginUserInfoRepo.findByLoginId(req.getLoginId());

	        if (loginMaster != null && userInfo != null && StringUtils.isNotBlank(req.getOaCode())) {
	            // Update existing LoginMaster
	            loginMaster.setCoreAppCode(req.getCoreAppCode());
	            loginMaster.setBranchCode(req.getBranchCode());
	            loginMaster.setUpdatedBy(req.getCreatedBy());
	            loginMaster.setUpdatedDate(new Date());
	            loginMaster.setStatus(req.getStatus());
	            loginRepo.save(loginMaster);

	            // Update existing LoginUserInfo
	            userInfo.setUserName(req.getLoginName());
	            userInfo.setCoreAppCode(req.getCoreAppCode());
	            userInfo.setCustomerName(req.getContactPersonName());
	            userInfo.setCountryName(req.getCountryName());
	            userInfo.setAddress1(req.getAddress());
	            userInfo.setCityName(req.getCityName());
	            userInfo.setStateName(req.getStateName());
	            userInfo.setRemarks(req.getRemarks());
	            userInfo.setUpdatedBy(req.getCreatedBy());
	            userInfo.setUpdatedDate(new Date());
	            LoginUserInfoRepo.save(userInfo);
	        } else {
	        	 // Generate OA Code
	            Integer topOaCode = loginRepo.findMaxOaCode(); // Fetch the highest OA code
	            Integer newOaCode = (topOaCode != null ? topOaCode : 0) + 1;
	        	
	            // Create new LoginMaster
	            LoginMaster loginMasterNew = new LoginMaster();
	            loginMasterNew.setLoginId(req.getLoginId());
	            loginMasterNew.setCoreAppCode(req.getCoreAppCode());
	            loginMasterNew.setUserType(req.getUserType());
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
	            loginMasterNew.setOaCode(newOaCode);
	            loginRepo.save(loginMasterNew);

	            // Create new LoginUserInfo
	            LoginUserInfo userInfoNew = new LoginUserInfo();
	            userInfoNew.setLoginId(req.getLoginId());
	            userInfoNew.setUserName(req.getLoginName());
	            userInfoNew.setCoreAppCode(req.getCoreAppCode());
	            userInfoNew.setCustomerName(req.getContactPersonName());
	            userInfoNew.setCountryName(req.getCountryName());
	            userInfoNew.setAddress1(req.getAddress());
	            userInfoNew.setCityName(req.getCityName());
	            userInfoNew.setStateName(req.getStateName());
	            userInfoNew.setRemarks(req.getRemarks());
	            userInfoNew.setCreatedBy(req.getCreatedBy());
	            userInfoNew.setEntryDate(new Date());
	            userInfoNew.setCompanyId(req.getCompanyId());
	            userInfoNew.setOaCode(newOaCode);
	            LoginUserInfoRepo.save(userInfoNew);
	        }

	        // Prepare response
	        Map<String, String> response = new HashMap<>();
	        response.put("Message", "login created/updated successfully");
	        comResponse.setMessage("Success");
	        comResponse.setResponse(response);
	        comResponse.setErrors(Collections.emptyList());
	        comResponse.setIsError(false);
	        return comResponse;

	    } catch (Exception e) {
	        log.error("Error creating/updating login", e);
	        comResponse.setMessage("Failure");
	        comResponse.setResponse(Collections.emptyMap());
	        comResponse.setErrors(Collections.singletonList(e.getMessage()));
	        comResponse.setIsError(true);
	        return comResponse;
	    }
	}

	@Override
	public CommonResponse getAllLogin(GetAllLoginRequest req) {
	    CommonResponse comResponse = new CommonResponse();
	    try {
	        // Validate input
	        if (req.getCompanyId() == null || req.getCompanyId().isEmpty()) {
	            comResponse.setMessage("Invalid request. Company ID is missing.");
	            comResponse.setIsError(true);
	            comResponse.setErrors(Collections.singletonList("Company ID is required."));
	            return comResponse;
	        }

	        // Fetch data from LoginUserInfo and LoginMaster repositories
	        List<LoginUserInfo> userInfoList = LoginUserInfoRepo.findByCompanyId(req.getCompanyId());
	        List<LoginMaster> loginMasterList = loginRepo.findByCompanyId(req.getCompanyId());

	        // Map LoginMaster data for easy access
	        Map<String, LoginMaster> loginMasterMap = loginMasterList.stream()
	                .collect(Collectors.toMap(LoginMaster::getLoginId, lm -> lm));

	        // Check if any data is found
	        if (userInfoList == null || userInfoList.isEmpty()) {
	            comResponse.setMessage("No records found for the given Company ID.");
	            comResponse.setIsError(false);
	            comResponse.setResponse(Collections.emptyList());
	            return comResponse;
	        }

	        // Map the entity list to DTO list
	        List<GarageLoginMasterDTO> garageLoginList = userInfoList.stream().map(userInfo -> {
	            GarageLoginMasterDTO dto = new GarageLoginMasterDTO();

	            // Populate data from LoginUserInfo
	            dto.setLoginId(userInfo.getLoginId());
	            dto.setCompanyId(userInfo.getCompanyId());
	            dto.setCoreAppCode(userInfo.getCoreAppCode());
	            dto.setAddress(userInfo.getAddress1());
	            dto.setCityName(userInfo.getCityName());
	            dto.setStatus(userInfo.getStatus());
	            dto.setBranchCode(userInfo.getBranchCode());
	            dto.setGarageAddress(userInfo.getAddress1() + ", " + userInfo.getAddress2());
	            dto.setStateName(userInfo.getStateName());
	            dto.setContactPersonName(userInfo.getCustomerName());
	            dto.setMobileNo(userInfo.getUserMobile());
	            dto.setEmailid(userInfo.getUserMail());
	            dto.setEffectiveDate(userInfo.getEffectiveDateStart());
	            dto.setRemarks(userInfo.getRemarks());
	            dto.setOaCode(userInfo.getOaCode() != null ? userInfo.getOaCode().toString() : "");
	            dto.setEntryDate(userInfo.getEntryDate());
	            dto.setUpdatedBy(userInfo.getUpdatedBy());
	            dto.setUpdatedDate(userInfo.getUpdatedDate());
	            dto.setCityCode(userInfo.getCityCode());
	            dto.setStateCode(userInfo.getStateCode());
	            dto.setCountryCode(userInfo.getCountryCode());
	            dto.setPobox(userInfo.getPobox());
	            dto.setCountryName(userInfo.getCountryName());
	            dto.setMobileCode(userInfo.getMobileCode());
	            dto.setMobileCodeDesc(userInfo.getMobileCodeDesc());

	            // Merge data from LoginMaster
	            LoginMaster loginMaster = loginMasterMap.get(userInfo.getLoginId());
	            if (loginMaster != null) {
	                dto.setPassWord(loginMaster.getPassword());
	                dto.setRepassWord(loginMaster.getPassword());
	                dto.setStatus(loginMaster.getStatus());
	                dto.setCreatedBy(loginMaster.getCreatedBy());
	                dto.setEntryDate(loginMaster.getEntryDate());
	                dto.setUserType(loginMaster.getUserType());
	                dto.setLoginName(userInfo.getUserName());
		            
	            }

	            return dto;
	        }).collect(Collectors.toList());

	        // Populate the response
	        comResponse.setMessage("Success");
	        comResponse.setIsError(false);
	        comResponse.setResponse(garageLoginList);
	        comResponse.setErrors(Collections.emptyList());
	        return comResponse;

	    } catch (Exception e) {
	        log.error("Error fetching garage login details", e);
	        comResponse.setMessage("Error fetching garage login details");
	        comResponse.setIsError(true);
	        comResponse.setResponse(Collections.emptyList());
	        comResponse.setErrors(Collections.singletonList(e.getMessage()));
	        return comResponse;
	    }
	}
	
}
