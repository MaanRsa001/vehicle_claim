package com.maan.veh.claim.serviceimpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.maan.veh.claim.entity.ListItemValue;
import com.maan.veh.claim.repository.ListItemValueRepository;
import com.maan.veh.claim.response.DropDownRes;
import com.maan.veh.claim.service.ClaimDropDownService;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;

@Component
public class ClaimDropDownServiceImpl implements ClaimDropDownService {
	
	 private Logger log = LogManager.getLogger(DropDownServiceImpl.class);
	    
    @PersistenceContext
	private EntityManager entityManager;

	@Autowired
	private ListItemValueRepository listRepo;

	@Override
	public List<DropDownRes> getPoliceStation() {
		return getDropdownValues("POLICE_STATION");
	}
	
	private List<DropDownRes> getDropdownValues(String itemType) {
        List<DropDownRes> resList = new ArrayList<>();
        try {
            //List<ListItemValue> getList = listRepo.findByItemTypeAndStatusOrderByItemCodeAsc(itemType, "Y");
        	List<ListItemValue> getList = getFromListItemValue(itemType);
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
	
	@Transactional
	public List<ListItemValue> getFromListItemValue(String itemType) {
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

			// Combine the predicates
			cq.where(cb.and(itemTypePredicate, statusPredicate, amendIdPredicate));

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
	public List<DropDownRes> getLossLocation() {
		return getDropdownValues("LOSS_LOCATION");
	}

	@Override
	public List<DropDownRes> getNatureOfLoss() {
		return getDropdownValues("NATURE_OF_LOSS");
	}
}
