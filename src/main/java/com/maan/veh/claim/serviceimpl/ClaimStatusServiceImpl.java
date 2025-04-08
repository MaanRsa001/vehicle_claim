package com.maan.veh.claim.serviceimpl;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;

import com.maan.veh.claim.entity.VcFlowMaster;
import com.maan.veh.claim.qiic.request.ClaimCountRequest;
import com.maan.veh.claim.repository.InsuredVehicleInfoRepository;
import com.maan.veh.claim.repository.VcFlowMasterRepository;
import com.maan.veh.claim.response.DropDownRes;
import com.maan.veh.claim.service.ClaimStatusService;

@Service
public class ClaimStatusServiceImpl implements ClaimStatusService{
	
	private Logger log = LogManager.getLogger(ClaimStatusServiceImpl.class);
	
	@Autowired
    private VcFlowMasterRepository flowMasterRepo;
	
	@Autowired
	private InsuredVehicleInfoRepository insuredVehicleInfoRepo;

	@Override
	public List<DropDownRes> getGarageStatus(String currentStatus) {
		return getStatus("Garage",currentStatus);
	}

	@Override
	public List<DropDownRes> getSurveyorStatus(String currentStatus) {
		return getStatus("Surveyor",currentStatus);
	}
	
	@Override
	public List<DropDownRes> getDealerStatus(String currentStatus) {
		return getStatus("Dealer",currentStatus);
	}
	
	private List<DropDownRes> getStatus(String usertype, String currentStatus) {
		List<DropDownRes> resList = new ArrayList<>();
	    try {
	        // Retrieve list of VcFlowMaster with usertype "Garage"
	        List<VcFlowMaster> flowList = flowMasterRepo.findByUsertypeAndStatusIdAndCompanyId(usertype,currentStatus,"100030");
	        
	        flowList.sort(Comparator.comparing(VcFlowMaster::getOrderId));

	        Set<String> uniqueStatuses = new HashSet<>();

	        for (VcFlowMaster flow : flowList) {
	            if (uniqueStatuses.add(flow.getSubStatus())) { // Add only if subStatus is new
	                DropDownRes res = new DropDownRes();
	                res.setCode(flow.getSubStatus());
	                res.setCodeDesc(flow.getGridDescription());
	                res.setCodeDescLocal(flow.getGridDescLocal());
	                resList.add(res);
	            }
	        }
	    } catch (Exception e) {
	        log.error("Error in getGridStatus: {}", e.getMessage(), e);
	    }

	    return resList;
	}

	@Override
	public List<DropDownRes> getGridStatus(String usertype, String companyId, String flowId) {
	    List<DropDownRes> resList = new ArrayList<>();

	    try {
	        // Get data from the database
	        List<VcFlowMaster> flowList = flowMasterRepo.findByUsertypeAndCompanyIdAndFlowId(usertype, companyId, flowId);

	        // Sort the list by orderId
	        flowList.sort(Comparator.comparing(VcFlowMaster::getOrderId));

	        // Keep track of unique statuses
	        Set<String> uniqueStatuses = new HashSet<>();

	        // Convert VcFlowMaster objects to DropDownRes objects
	        for (VcFlowMaster flow : flowList) {
	            if (uniqueStatuses.add(flow.getSubStatus())) { // Add only if subStatus is new
	                DropDownRes res = new DropDownRes();
	                res.setCode(flow.getSubStatus());
	                res.setCodeDesc(flow.getGridDescription());
	                res.setCodeDescLocal(flow.getGridDescLocal());
	                resList.add(res);
	            }
	        }

	    } catch (Exception e) {
	        log.error("Error in getGridStatus: {}", e.getMessage(), e);
	    }

	    return resList;
	}

	@Override
	public List<DropDownRes> getClaimCount(ClaimCountRequest req) {
	    List<DropDownRes> resList = new ArrayList<>();

	    try {
	        // Fetch flow data based on user type and company ID
	        List<VcFlowMaster> flowList = flowMasterRepo.findByUsertypeAndCompanyId(req.getUserType(), req.getCompanyId());
	        
	        Map<String, String> statusDesc = new HashMap<>();
	        
	        // Using a Set to store unique statuses
	        Set<String> uniqueStatuses = flowList.stream()
	                                             .map(VcFlowMaster::getSubStatus)
	                                             .collect(Collectors.toSet());
	        for(VcFlowMaster flow : flowList) {
	        	statusDesc.put(flow.getSubStatus(),flow.getSubStatusDescription());
	        }

	        // Map to store status counts
	        Map<String, Integer> countMap = new HashMap<>();

	        for (String status : uniqueStatuses) {
	            List<String> subStatusList = new ArrayList<>();
	            subStatusList.add(status);
	            
	            if (status.equalsIgnoreCase("PFG")) {
	                subStatusList.add("Y");
	            }

	            // Fetch insured vehicles count instead of fetching the list (performance optimization)
	            int insuredCount = insuredVehicleInfoRepo.countByCompanyIdAndGarageIdAndStatusIn(
	                                    Integer.valueOf(req.getCompanyId()), req.getLoginId(), subStatusList);

	            countMap.put(status, insuredCount);
	        }

	        // Convert map to response list
	        for (Map.Entry<String, Integer> entry : countMap.entrySet()) {
	            DropDownRes res = new DropDownRes();
	            res.setCode(entry.getKey());
	            res.setCodeDesc(entry.getValue().toString());
	            res.setCodeDescLocal(statusDesc.get(entry.getKey()));
	            resList.add(res);
	        }

	    }catch (Exception e) {
	        log.error("Unexpected error in getClaimCount: {}", e.getMessage(), e);
	    }

	    return resList;
	}


}
