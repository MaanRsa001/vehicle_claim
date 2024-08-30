package com.maan.veh.claim.serviceimpl;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.maan.veh.claim.entity.ClaimLossTypeMaster;
import com.maan.veh.claim.entity.ListItemValue;
import com.maan.veh.claim.entity.VehicleBodypartsMaster;
import com.maan.veh.claim.repository.ClaimLosstypeMasterRepository;
import com.maan.veh.claim.repository.ListItemValueRepository;
import com.maan.veh.claim.repository.VehicleBodypartsMasterRepository;
import com.maan.veh.claim.response.DropDownRes;
import com.maan.veh.claim.service.DropDownService;

@Service
public class DropDownServiceImpl implements DropDownService {

    private Logger log = LogManager.getLogger(DropDownServiceImpl.class);

    @Autowired
    private ListItemValueRepository listRepo;
    
    @Autowired
    private ClaimLosstypeMasterRepository lossRepo;
    
    @Autowired
    private VehicleBodypartsMasterRepository bodyPartRepo;

    @Override
    public List<DropDownRes> getDamageDirection() {
        return getDropdownValues("DAMAGE_DIRECTION");
    }

    @Override
    public List<DropDownRes> getDamageDropdown() {
        return getDropdownValues("DAMAGE_DROPDOWN");
    }

    @Override
    public List<DropDownRes> getWorkOrderType() {
        return getDropdownValues("WORK_ORDER_TYPE");
    }

    @Override
    public List<DropDownRes> getSettlementType() {
        return getDropdownValues("SETTLEMENT_TYPE");
    }

    @Override
    public List<DropDownRes> getLossType() {
        return getDropdownValues("LOSS_TYPE");
    }

    @Override
    public List<DropDownRes> getDamageType() {
        return getDropdownValues("DAMAGE_TYPE");
    }

    private List<DropDownRes> getDropdownValues(String itemType) {
        List<DropDownRes> resList = new ArrayList<>();
        try {
            List<ListItemValue> getList = listRepo.findByItemTypeAndStatusOrderByItemCodeAsc(itemType, "Y");
            for (ListItemValue data : getList) {
                DropDownRes res = new DropDownRes();
                res.setCode(data.getItemCode());
                res.setCodeDesc(data.getItemValue());
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
	public List<DropDownRes> getLosstype() {
		List<DropDownRes> resList = new ArrayList<>();
        try {
            List<ClaimLossTypeMaster> getList = lossRepo.findByStatusOrderByCategoryIdAsc("Y");
            for (ClaimLossTypeMaster data : getList) {
                DropDownRes res = new DropDownRes();
                res.setCode(data.getCategoryId().toString());
                res.setCodeDesc(data.getCategoryDesc());
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
	public List<DropDownRes> getbodyPart() {
		List<DropDownRes> resList = new ArrayList<>();
        try {
            List<VehicleBodypartsMaster> getList = bodyPartRepo.findByStatusOrderByPartIdAsc("Y");
            for (VehicleBodypartsMaster data : getList) {
                DropDownRes res = new DropDownRes();
                res.setCode(data.getPartId().toString());
                res.setCodeDesc(data.getPartDescription());
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
