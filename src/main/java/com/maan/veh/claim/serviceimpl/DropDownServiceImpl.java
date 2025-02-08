package com.maan.veh.claim.serviceimpl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.maan.veh.claim.entity.BranchMaster;
import com.maan.veh.claim.entity.CityMaster;
import com.maan.veh.claim.entity.ClaimLossTypeMaster;
import com.maan.veh.claim.entity.CountryMaster;
import com.maan.veh.claim.entity.InsuranceCompanyMaster;
import com.maan.veh.claim.entity.ListItemValue;
import com.maan.veh.claim.entity.LoginMaster;
import com.maan.veh.claim.entity.VcDocumentMaster;
import com.maan.veh.claim.entity.VehicleBodypartsMaster;
import com.maan.veh.claim.repository.BranchMasterRepository;
import com.maan.veh.claim.repository.CityMasterRepository;
import com.maan.veh.claim.repository.ClaimLosstypeMasterRepository;
import com.maan.veh.claim.repository.CountryMasterRepository;
import com.maan.veh.claim.repository.InsuranceCompanyMasterRepository;
import com.maan.veh.claim.repository.ListItemValueRepository;
import com.maan.veh.claim.repository.LoginMasterRepository;
import com.maan.veh.claim.repository.VcDocumentMasterRepository;
import com.maan.veh.claim.repository.VehicleBodypartsMasterRepository;
import com.maan.veh.claim.response.DropDownRes;
import com.maan.veh.claim.service.DropDownService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

@Service
public class DropDownServiceImpl implements DropDownService {

    private Logger log = LogManager.getLogger(DropDownServiceImpl.class);
    
	@PersistenceContext
	private EntityManager entityManager;

    @Autowired
    private ListItemValueRepository listRepo;
    
    @Autowired
    private ClaimLosstypeMasterRepository lossRepo;
    
    @Autowired
    private VehicleBodypartsMasterRepository bodyPartRepo;
    
    @Autowired
    private VcDocumentMasterRepository documentMasterRepo;
    
    @Autowired
    private LoginMasterRepository loginRepo;
    
    @Autowired
    private BranchMasterRepository branchMasterRepo;
    
    @Autowired
    private InsuranceCompanyMasterRepository companyMasterRepo;
    
    @Autowired
    private CountryMasterRepository countryMasterRepo;
    
    @Autowired
    private CityMasterRepository cityMasterRepo;

    @Override
    public List<DropDownRes> getDamageDirection(String companyId) {
        return getDropdownValues("DAMAGE_DIRECTION",companyId);
    }

    @Override
    public List<DropDownRes> getDamageDropdown(String companyId) {
        return getDropdownValues("DAMAGE_DROPDOWN",companyId);
    }

    @Override
    public List<DropDownRes> getWorkOrderType(String companyId) {
        return getDropdownValues("WORK_ORDER_TYPE",companyId);
    }
    
    @Transactional
    @Override
    public String getItemCodeByItemValue(String value,String type) {
    	try {
			List<ListItemValue> list = listRepo.findByItemValueAndItemTypeOrderByAmendIdDesc(value,type);
			if(list != null && list.size()>0) {
				return list.get(0).getItemCode();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
    	return null;
    }

    @Override
    public List<DropDownRes> getSettlementType(String companyId) {
        return getDropdownValues("SETTLEMENT_TYPE",companyId);
    }

    @Override
    public List<DropDownRes> getLossType(String companyId) {
        return getDropdownValues("LOSS_TYPE",companyId);
    }

    @Override
    public List<DropDownRes> getDamageType(String companyId) {
        return getDropdownValues("DAMAGE_TYPE",companyId);
    }
    
	@Override
	public List<DropDownRes> getVatPercentage(String companyId) {
		 return getDropdownValues("VAT_PERCENTAGE",companyId);
	}
	
	@Override
	public List<DropDownRes> getAccountForSettlement(String companyId) {
		 return getDropdownValues("ACCOUNT_SETTLEMENT",companyId);
	}
	
	@Override
	public List<DropDownRes> getRepairReplace(String companyId) {
		 return getDropdownValues("REPAIR_REPLACE",companyId);
	}
	
	@Override
	public List<DropDownRes> getStatus(String companyId) {
		 return getDropdownValues("STATUS",companyId);
	}
	@Override
	public List<DropDownRes> getRepairType(String companyId) {
		return getDropdownValues("REPAIR_TYPE",companyId);
	}
	@Override
	public List<DropDownRes> getUserType(String companyId) {
		return getDropdownValues("USER_TYPE",companyId);
	}
	@Override
	public List<DropDownRes> getLossLocation(String companyId) {
		return getDropdownValues("LOSS_LOCATION",companyId);
	}
	
    private List<DropDownRes> getDropdownValues(String itemType,String companyId) {
        List<DropDownRes> resList = new ArrayList<>();
        try {
            //List<ListItemValue> getList = listRepo.findByItemTypeAndStatusOrderByItemCodeAsc(itemType, "Y");
        	List<ListItemValue> getList = getFromListItemValue(itemType,companyId);
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
	public List<DropDownRes> getLosstype(String companyId) {
		List<DropDownRes> resList = new ArrayList<>();
        try {
            List<ClaimLossTypeMaster> getList = lossRepo.findByStatusAndCompanyIdOrderByCategoryIdAsc("Y",Integer.valueOf(companyId));
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
	public List<DropDownRes> getbodyPart(String companyId) {
		List<DropDownRes> resList = new ArrayList<>();
        try {
            List<VehicleBodypartsMaster> getList = bodyPartRepo.findByStatusAndCompanyIdOrderByPartIdAsc("Y",new BigDecimal(companyId));
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
	
	@Override
	public String getbodyPartCodeByValue(String value) {
		String code = "";
        try {
            List<VehicleBodypartsMaster> getList = bodyPartRepo.findByStatusAndPartDescriptionOrderByPartIdAsc("Y",value);
            if(getList != null && getList.size() > 0) {
            	code = getList.get(0).getCoreAppCode();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.info("Exception is ---> " + e.getMessage());
            return null;
        }
        return code;
	}
	

	@Transactional
	public List<ListItemValue> getFromListItemValue(String itemType,String companyId) {
		try {
			CriteriaBuilder cb = entityManager.getCriteriaBuilder();
			CriteriaQuery<ListItemValue> cq = cb.createQuery(ListItemValue.class);
			Root<ListItemValue> root = cq.from(ListItemValue.class);

			// Subquery to get the max amendId for each itemId
			Subquery<Integer> subquery = cq.subquery(Integer.class);
			Root<ListItemValue> subRoot = subquery.from(ListItemValue.class);
			subquery.select(cb.max(subRoot.get("amendId")));
			subquery.where(cb.equal(subRoot.get("itemId"), root.get("itemId")),
					cb.equal(subRoot.get("companyId"), root.get("companyId")),
					cb.equal(subRoot.get("branchCode"), root.get("branchCode")));

			// Predicate for the main query
			Predicate itemTypePredicate = cb.equal(root.get("itemType"), itemType);
			Predicate statusPredicate = cb.equal(root.get("status"), "Y");
			Predicate amendIdPredicate = cb.equal(root.get("amendId"), subquery);
			Predicate companyIdPredicate = cb.equal(root.get("companyId"), companyId);

			// Combine the predicates
			cq.where(cb.and(itemTypePredicate, statusPredicate, amendIdPredicate,companyIdPredicate));

			// Order by itemCode ascending
			cq.orderBy(cb.asc(root.get("itemCode")));

			// Execute the query
			return entityManager.createQuery(cq).getResultList();
		} catch (Exception e) {
			// Log the exception (optional, depending on your logging framework)
			e.printStackTrace();

			// Return an empty list in case of an exception
			return Collections.emptyList();
		}
	}

	@Override
	public List<DropDownRes> getGarageLoginId(String companyId) {
		List<DropDownRes> resList = new ArrayList<>();
        try {
            List<LoginMaster> getList = loginRepo.findByUserTypeAndCompanyId("Garage",companyId);
            for (LoginMaster data : getList) {
                DropDownRes res = new DropDownRes();
                res.setCode(data.getOaCode() != null ? data.getOaCode().toString() : "");
                res.setCodeDesc(data.getLoginId());
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
	public List<DropDownRes> getDealerLoginId(String companyId) {
		List<DropDownRes> resList = new ArrayList<>();
        try {
            List<LoginMaster> getList = loginRepo.findByUserTypeAndCompanyId("Dealer",companyId);
            for (LoginMaster data : getList) {
                DropDownRes res = new DropDownRes();
                res.setCode(data.getLoginId());
                res.setCodeDesc(data.getLoginId());
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
	public List<DropDownRes> geDocumentType(String companyId) {
		List<DropDownRes> resList = new ArrayList<>();
        try {
            List<VcDocumentMaster> getList = documentMasterRepo.findByStatusAndCompanyIdOrderByDocumentIdAsc("Y",Integer.valueOf(companyId));
            for (VcDocumentMaster data : getList) {
                DropDownRes res = new DropDownRes();
                res.setCode(data.getDocumentId().toString());
                res.setCodeDesc(data.getDocumentName());
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
	public List<DropDownRes> getBranch(String companyId) {
		List<DropDownRes> resList = new ArrayList<>();
        try {
            List<BranchMaster> getList = branchMasterRepo.findByCompanyIdAndStatus(companyId,"Y");
            for (BranchMaster data : getList) {
                DropDownRes res = new DropDownRes();
                res.setCode(data.getBranchCode().toString());
                res.setCodeDesc(data.getBranchName());
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
	public List<DropDownRes> getCompany(String companyId) {
		List<DropDownRes> resList = new ArrayList<>();
        try {
            List<InsuranceCompanyMaster> getList = companyMasterRepo.findByStatus("Y");
            for (InsuranceCompanyMaster data : getList) {
                DropDownRes res = new DropDownRes();
                res.setCode(data.getCompanyId().toString());
                res.setCodeDesc(data.getCompanyName());
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
	public List<DropDownRes> getCountry(String companyId) {
		List<DropDownRes> resList = new ArrayList<>();
        try {
            List<CountryMaster> getList = countryMasterRepo.findByStatusAndCompanyId("Y",companyId);
            for (CountryMaster data : getList) {
                DropDownRes res = new DropDownRes();
                res.setCode(data.getCountryId().toString());
                res.setCodeDesc(data.getCountryName());
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
	public List<DropDownRes> getCity(String companyId) {
		List<DropDownRes> resList = new ArrayList<>();
        try {
            List<CityMaster> getList = cityMasterRepo.findByStatusAndCompanyId("Y",companyId);
            for (CityMaster data : getList) {
                DropDownRes res = new DropDownRes();
                res.setCode(data.getCityId().toString());
                res.setCodeDesc(data.getCityName());
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
