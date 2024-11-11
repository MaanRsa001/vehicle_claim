package com.maan.veh.claim.serviceimpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.veh.claim.entity.VcFlowMaster;
import com.maan.veh.claim.repository.VcFlowMasterRepository;
import com.maan.veh.claim.response.DropDownRes;
import com.maan.veh.claim.service.ClaimStatusService;

@Service
public class ClaimStatusServiceImpl implements ClaimStatusService{
	
	private Logger log = LogManager.getLogger(ClaimStatusServiceImpl.class);
	
	@Autowired
    private VcFlowMasterRepository flowMasterRepo;

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
	        List<VcFlowMaster> flowList = flowMasterRepo.findByUsertypeAndStatusId(usertype,currentStatus);

	        // Convert the list to a map with subStatus as the key and subStatusDescription as the value
	        Map<String, String> statusMap = flowList.stream()
	                .collect(Collectors.toMap(
	                        VcFlowMaster::getSubStatus,
	                        VcFlowMaster::getSubStatusDescription,
	                        (existing, replacement) -> existing // Handle duplicate keys by keeping the existing value
	                ));

	        // Iterate over the map entries to create DropDownRes objects
	        for (Map.Entry<String, String> entry : statusMap.entrySet()) {
	            DropDownRes res = new DropDownRes();
	            res.setCode(entry.getKey());
	            res.setCodeDesc(entry.getValue());
	            resList.add(res);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        log.info("Exception is ---> " + e.getMessage());
	        return null;
	    }
	    return resList;
	}

	@Override
	public List<DropDownRes> getGridStatus(String usertype) {
		List<DropDownRes> resList = new ArrayList<>();
	    try {
	        // Retrieve list of VcFlowMaster with usertype "Garage"
	        List<VcFlowMaster> flowList = flowMasterRepo.findByUsertype(usertype);

	        // Convert the list to a map with subStatus as the key and subStatusDescription as the value
	        Map<String, String> statusMap = flowList.stream()
	                .collect(Collectors.toMap(
	                        VcFlowMaster::getSubStatus,
	                        VcFlowMaster::getGridDescription,
	                        (existing, replacement) -> existing // Handle duplicate keys by keeping the existing value
	                ));

	        // Iterate over the map entries to create DropDownRes objects
	        for (Map.Entry<String, String> entry : statusMap.entrySet()) {
	            DropDownRes res = new DropDownRes();
	            res.setCode(entry.getKey());
	            res.setCodeDesc(entry.getValue());
	            resList.add(res);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	        log.info("Exception is ---> " + e.getMessage());
	        return null;
	    }
	    return resList;
	}


}
