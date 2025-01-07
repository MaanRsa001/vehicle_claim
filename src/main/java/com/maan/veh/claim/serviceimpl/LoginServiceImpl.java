package com.maan.veh.claim.serviceimpl;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.maan.veh.claim.auth.EncryDecryService;
import com.maan.veh.claim.auth.JwtTokenUtil;
import com.maan.veh.claim.auth.passwordEnc;
import com.maan.veh.claim.dto.GarageLoginMasterDTO;
import com.maan.veh.claim.dto.GetAllCoreAppCodeResponseDto;
import com.maan.veh.claim.dto.GetAllCoreAppCodeResponseDto.Dataset;
import com.maan.veh.claim.entity.ApiTransactionLog;
import com.maan.veh.claim.entity.BranchMaster;
import com.maan.veh.claim.entity.InsuranceCompanyMaster;
import com.maan.veh.claim.entity.LoginMaster;
import com.maan.veh.claim.entity.LoginMasterId;
import com.maan.veh.claim.entity.LoginUserInfo;
import com.maan.veh.claim.entity.MenuMaster;
import com.maan.veh.claim.entity.NotifTransactionDetails;
import com.maan.veh.claim.entity.SessionMaster;
import com.maan.veh.claim.external.ErrorResponse;
import com.maan.veh.claim.repository.ApiTransactionLogRepository;
import com.maan.veh.claim.repository.BranchMasterRepository;
import com.maan.veh.claim.repository.InsuranceCompanyMasterRepository;
import com.maan.veh.claim.repository.LoginMasterRepository;
import com.maan.veh.claim.repository.LoginUserInfoRepository;
import com.maan.veh.claim.repository.MenuMasterRepository;
import com.maan.veh.claim.repository.NotifTransactionDetailsRepository;
import com.maan.veh.claim.repository.SessionMasterRepository;
import com.maan.veh.claim.request.ChangePasswordReq;
import com.maan.veh.claim.request.GetAllLoginRequest;
import com.maan.veh.claim.request.GetCoreAppCodeRequest;
import com.maan.veh.claim.request.LoginRequest;
import com.maan.veh.claim.response.CommonResponse;
import com.maan.veh.claim.response.DropDownRes;
import com.maan.veh.claim.response.ErrorList;
import com.maan.veh.claim.response.SuccessRes;
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
	
	@Autowired
	private InsuranceCompanyMasterRepository companyMasterRepo;
	
	@Autowired
	private NotificationService notificationService;
	
	@Autowired 
	private NotifTransactionDetailsRepository notifTrans;
	
	@Autowired
    private ApiTransactionLogRepository apiTransactionLogRepo;
	
	@Autowired
    private ObjectMapper objectMapper;
	
	@Autowired
	private RestTemplate restTemplate;
	
    @Value("${external.api.url.partyMaster}")
    private String partyMaster;
	
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
	        response.put("PartyId", login.getCoreAppCode());
	        response.put("CategoryId", login.getAgencyCode());
	        response.put("ProdId", "1100");
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
	        
	        Instant now = Instant.now();
			Instant after = now.plus(Duration.ofDays(45));
			Date dateAfter = Date.from(after);
			
			passwordEnc passEnc = new passwordEnc();
			
	        if (loginMaster != null && userInfo != null && StringUtils.isNotBlank(req.getOaCode())) {
	            // Update existing LoginMaster
	            loginMaster.setCoreAppCode(req.getCoreAppCode());
	            loginMaster.setBranchCode(req.getBranchCode());
	            loginMaster.setUpdatedBy(req.getCreatedBy());
	            loginMaster.setUpdatedDate(new Date());
	            loginMaster.setStatus(req.getStatus());
	            loginMaster.setEffectiveDateStart(req.getEffectiveDate());
				
				if("Y".equalsIgnoreCase(req.getChangePassYN())) {
					String password = passEnc.crypt(req.getPassWord().trim());
					loginMaster.setPassword(password);
					loginMaster.setPwdCount("0");
					loginMaster.setLpassDate(dateAfter);
				}
	            
	            loginRepo.save(loginMaster);

	            // Update existing LoginUserInfo
	            userInfo.setUserName(req.getLoginName());
	            userInfo.setCoreAppCode(req.getCoreAppCode());
	            userInfo.setCustomerName(req.getContactPersonName());
	            userInfo.setCountryCode(req.getCountryCode());
	            userInfo.setAddress1(req.getAddress());
	            userInfo.setCityCode(req.getCityCode() != null ? Integer.valueOf(req.getCityCode()) : 0);
	            userInfo.setStateName(req.getStateName());
	            userInfo.setRemarks(req.getRemarks());
	            userInfo.setUpdatedBy(req.getCreatedBy());
	            userInfo.setUpdatedDate(new Date());
	            userInfo.setEffectiveDateStart(req.getEffectiveDate());
	            userInfo.setUserMobile(req.getMobileNo());
	            userInfo.setUserMail(req.getEmailid());
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
	            String password = passEnc.crypt(req.getPassWord().trim());
	            loginMasterNew.setPassword(password);
	            loginMasterNew.setStatus(req.getStatus());
	            loginMasterNew.setBranchCode(req.getBranchCode());
	            loginMasterNew.setCompanyId(req.getCompanyId());
	            loginMasterNew.setPwdCount("0");
	            loginMasterNew.setCreatedBy(req.getCreatedBy());
	            loginMasterNew.setEntryDate(new Date());
	            loginMasterNew.setOaCode(newOaCode);
	            loginMasterNew.setEffectiveDateStart(req.getEffectiveDate());
	         
				loginMasterNew.setLpassDate(dateAfter);
	            
	            loginRepo.save(loginMasterNew);

	            // Create new LoginUserInfo
	            LoginUserInfo userInfoNew = new LoginUserInfo();
	            userInfoNew.setLoginId(req.getLoginId());
	            userInfoNew.setUserName(req.getLoginName());
	            userInfoNew.setCoreAppCode(req.getCoreAppCode());
	            userInfoNew.setCustomerName(req.getContactPersonName());
	            userInfoNew.setCountryCode(req.getCountryCode());
	            userInfoNew.setAddress1(req.getAddress());
	            userInfoNew.setCityCode(req.getCityCode() != null ? Integer.valueOf(req.getCityCode()) : 0);
	            userInfoNew.setStateName(req.getStateName());
	            userInfoNew.setRemarks(req.getRemarks());
	            userInfoNew.setCreatedBy(req.getCreatedBy());
	            userInfoNew.setEntryDate(new Date());
	            userInfoNew.setCompanyId(req.getCompanyId());
	            userInfoNew.setOaCode(newOaCode);
	            userInfoNew.setEffectiveDateStart(req.getEffectiveDate());
	            userInfoNew.setUserMobile(req.getMobileNo());
	            userInfoNew.setUserMail(req.getEmailid());
	            userInfoNew.setAgencyCode(req.getCatagoryId());
	            
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
	            dto.setCityCode(userInfo.getCityCode() != null ? userInfo.getCityCode().toString() : "");
	            //dto.setStateCode(userInfo.getStateCode());
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
	                dto.setBranchCode(loginMaster.getBranchCode());
		            dto.setCatagoryId(loginMaster.getAgencyCode());
		            
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

	@Override
	public CommonResponse getLoginDetails(GetAllLoginRequest req) {
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
	        LoginUserInfo userInfo = LoginUserInfoRepo.findByLoginId(req.getLoginId());
	        LoginMaster loginMaster = loginRepo.findByLoginId(req.getLoginId());

	        // Check if any data is found
	        if (userInfo == null ) {
	            comResponse.setMessage("No records found for the given Company ID.");
	            comResponse.setIsError(false);
	            comResponse.setResponse(Collections.emptyList());
	            return comResponse;
	        }

	        // Map the entity list to DTO list
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
	            dto.setCityCode(userInfo.getCityCode() != null ? userInfo.getCityCode().toString() : "");
	            //dto.setStateCode(userInfo.getStateCode());
	            dto.setCountryCode(userInfo.getCountryCode());
	            dto.setPobox(userInfo.getPobox());
	            dto.setCountryName(userInfo.getCountryName());
	            dto.setMobileCode(userInfo.getMobileCode());
	            dto.setMobileCodeDesc(userInfo.getMobileCodeDesc());

	            // Merge data from LoginMaster
	            if (loginMaster != null) {
	                dto.setPassWord(loginMaster.getPassword());
	                dto.setRepassWord(loginMaster.getPassword());
	                dto.setStatus(loginMaster.getStatus());
	                dto.setCreatedBy(loginMaster.getCreatedBy());
	                dto.setEntryDate(loginMaster.getEntryDate());
	                dto.setUserType(loginMaster.getUserType());
	                dto.setLoginName(userInfo.getUserName());
	                dto.setBranchCode(loginMaster.getBranchCode());
	            }

	        // Populate the response
	        comResponse.setMessage("Success");
	        comResponse.setIsError(false);
	        comResponse.setResponse(dto);
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

	@Override
	public String LoginChangePassword(ChangePasswordReq req) {
		String res = new String();
		try {
		passwordEnc passEnc = new passwordEnc();
		String epass = passEnc.crypt(req.getOldpassword().trim());
		String newpass = passEnc.crypt(req.getNewPassword().trim());
		LoginMaster master = new LoginMaster();  
		log.info("EncryptPassword-->" + epass);
		LoginMaster model = loginRepo.findByLoginId(req.getLoginId());
		if(model !=null ) {
			master = model ;
			
			String pass1 = master.getPassword();
			String pass2 = master.getLpass1();
			String pass3 = master.getLpass2();
			String pass4 = master.getLpass4();
			String pass5 = master.getLpass5();
			
			master.setLpass1(pass1);
			master.setLpass2(pass2);
			master.setLpass3(pass3);
			master.setLpass4(pass4);
			master.setLpass5(pass5);
			master.setPassword(newpass);
			Integer pwdCount =  Integer.valueOf(master.getPwdCount())+1 ;
			master.setPwdCount(String.valueOf(pwdCount) );
			
			Instant now = Instant.now();
			Instant after = now.plus(Duration.ofDays(45));
			Date dateAfter = Date.from(after);
			master.setLpassDate(dateAfter);
			LoginMaster table = loginRepo.save(master);
			
			if(table!=null) {
				res  = "Password Changed Successfully";
			}
			else {
				res  = "FAILED" ;
				
			}
		}
		
		
		} catch (Exception e) {
			e.printStackTrace();
			log.info("Error-->" + e.getMessage());
		}
		return res;
	}

	@Override
	public SuccessRes LoginForgetPassword(ChangePasswordReq req) {
		SuccessRes res = new SuccessRes();
		try {
				String msg = sendUserPwd(req , "tempPwd" );
				res.setResponse(msg);
			
		
		} catch (Exception e) {
			log.info(e);
		}
		return res;
	}
	
	public String sendUserPwd(ChangePasswordReq req, String temp) {
		String msg = "Temporary Password Notification Sent ";
		try {
			String tempPassword = getTempPassword(req.getLoginId());

			LoginMaster ld = loginRepo.findByLoginId(req.getLoginId());

			InsuranceCompanyMaster cm = companyMasterRepo.findByCompanyId(ld.getCompanyId());

			LoginUserInfo lu = LoginUserInfoRepo.findByLoginId(req.getLoginId());

			sentDirectMail(req.getLoginId(), tempPassword, ld, lu, cm);

		} catch (Exception e) {
			log.info(e);
			return null;
		}
		return msg;
	}
	
	public void sentDirectMail(String loginId , String tempPassword , LoginMaster ld , LoginUserInfo lu , InsuranceCompanyMaster cm  ) {
		try {
			
 			Calendar calend = Calendar.getInstance();
			calend.setTime(new Date()); 
			calend.add(Calendar.DATE, 1); 

			
 			String tinyGroupId=String.valueOf(Instant.now().getEpochSecond());
			NotifTransactionDetails nt = NotifTransactionDetails.builder()
					.brokerCompanyName(lu.getUserName())
					.brokerMailId(lu.getUserMail())					
					.companyName(cm.getCompanyName())
					.customerMailid(lu.getUserMail())					
					.customerName(lu.getUserName())
					.entryDate(new Date())
					.notifcationPushDate(new Date())
					.notifcationEndDate(calend.getTime())
					.notifDescription(tempPassword)
					.notifNo(Instant.now().toEpochMilli())
					.notifPriority(1)
					.notifPushedStatus("P")
					.notifTemplatename("TEMP_PASSWORD")											
					.productName("Common")					
					.companyid(ld.getCompanyId())
					.productid(99999)
					.companyLogo(cm.getCompanyLogo())
					.companyAddress(cm.getCompanyAddress())											
					.tinyUrlActive("N")
					.tinyGroupId(tinyGroupId)
					.build();
			NotifTransactionDetails sv = notifTrans.save(nt);
			List<NotifTransactionDetails> text=new LinkedList<NotifTransactionDetails>();
			text.add(sv);
			notificationService.jobProcess(text);			
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String getTempPassword(String loginId) {
		final String alphabet = "Aa2Bb@3Cc#4Dd$5Ee%6Ff7Gg&8Hh9Jj2Kk=3L4Mm5Nn@6Pp7Qq#8Rr$9Ss%2Tt3Uu&4Vv5Ww+6Xx=7Yy8Zz9";
		final int N = alphabet.length();
		String temppwd = "";
		Random r = new Random();
		for (int i = 0; i < 10; i++) {
			temppwd += alphabet.charAt(r.nextInt(N));
		}
		try {
			passwordEnc passEnc = new passwordEnc();
			String password = passEnc.crypt(temppwd.trim());
			log.info("newpwd ==>" + password + ":userId ==>" + loginId + ":Temppassword==>" + temppwd);
			LoginMaster loginData = loginRepo.findByLoginId(loginId);
			
			if (loginData !=null) {
				LoginMaster model = loginData ;
				model.setLpass5(loginData.getLpass4());
				model.setLpass4(loginData.getLpass3());
				model.setLpass3(loginData.getLpass2());
				model.setLpass2(loginData.getLpass1());
				model.setLpass1(loginData.getPassword()); 
				model.setStatus("Y");
				model.setPwdCount("0");
				Instant now = Instant.now();
				Instant after = now.plus(Duration.ofDays(1));
				Date dateAfter = Date.from(after);
				model.setLpassDate(dateAfter);
				model.setPassword(password);
				loginRepo.saveAndFlush(model);
				
			}
			
		} catch (Exception e) {
			log.info(e);
		}
		return temppwd;
	}

	@Override
	public CommonResponse getCoreAppCode(GetCoreAppCodeRequest req) {
		CommonResponse response = new CommonResponse();
		ApiTransactionLog logTab = new ApiTransactionLog();
		logTab.setRequestTime(LocalDateTime.now());
		logTab.setEntryDate(new Date());
		logTab.setEndpoint(partyMaster);

		try {

			// Create headers and add JWT token
			HttpHeaders headers = new HttpHeaders();
			headers.set("Authorization", "No Auth");
			headers.setContentType(MediaType.APPLICATION_JSON);

			HttpEntity<String> entity = new HttpEntity<>(headers);
			logTab.setRequest(" ");

			// Configure SSL Trust Managers (if necessary)
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(java.security.cert.X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(java.security.cert.X509Certificate[] certs, String authType) {
				}
			} };

			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
			HttpsURLConnection.setDefaultHostnameVerifier((hostname, session) -> true);

			// Send request to external API
			ResponseEntity<String> apiResponse = restTemplate.exchange(logTab.getEndpoint(), // URL endpoint
					HttpMethod.GET, // HTTP method
					entity, // HttpEntity containing headers
					String.class // Expected response type
			);

			logTab.setResponse(apiResponse.getBody());
			logTab.setStatus("SUCCESS");

			// Parse response into ExternalApiResponse object
			GetAllCoreAppCodeResponseDto externalApiResponse = objectMapper.readValue(apiResponse.getBody(),
					GetAllCoreAppCodeResponseDto.class);

			if (externalApiResponse.isHasError()) {
				response.setMessage(externalApiResponse.getMessage());
				response.setIsError(true);

			} else {
				List<DropDownRes> resList = new ArrayList<>();

				List<Dataset> datalist = externalApiResponse.getData();

				// Filter the datalist by category and searchValue
				List<GetAllCoreAppCodeResponseDto.Dataset> filteredDataList = datalist.stream().filter(
						data -> req.getCategory() != null && req.getCategory().equalsIgnoreCase(data.getCategory())) // Filter
																														// by
																														// category
						.filter(data -> {
							// Apply searchValue filter (first compare with partyId, then partyName)
							String searchValue = req.getSearchValue();
							if (searchValue == null || searchValue.isEmpty()) {
								return true; // No search value means include all in this category
							}
							return (data.getPartyId() != null && data.getPartyId().contains(searchValue))
									|| (data.getPartyName() != null
											&& data.getPartyName().toLowerCase().contains(searchValue.toLowerCase()));
						}).collect(Collectors.toList());

				for (GetAllCoreAppCodeResponseDto.Dataset data : filteredDataList) {
					DropDownRes res = new DropDownRes();
					res.setCode(data.getPartyId() != null ? data.getPartyId().toString() : "");
					res.setCodeDesc(data.getPartyName());
					resList.add(res);
				}

				// Set success message
				response.setMessage(externalApiResponse.getMessage());
				response.setIsError(false);
				response.setResponse(resList);
			}

		} catch (Exception e) {
			logTab.setStatus("FAILURE");
			logTab.setErrorMessage(e.getMessage());
			response.setMessage("Failed to get data");
			response.setIsError(true);
			response.setErrors(Collections.singletonList(new ErrorResponse("100", "General", e.getMessage())));
		} finally {
			logTab.setResponseTime(LocalDateTime.now());
			//log.info("CoreAppCodes => " +logTab);
			//apiTransactionLogRepo.save(log);
		}

		return response;
	}
}
