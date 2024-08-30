/*
 * Java domain class for entity "ListItemValue" 
 * Created on 2022-08-24 ( Date ISO 2022-08-24 - Time 12:58:27 )
 * Generated by Telosys Tools Generator ( version 3.3.0 )
 */
 
 /*
 * Created on 2022-08-24 ( 12:58:27 )
 * Generated by Telosys ( http://www.telosys.org/ ) version 3.3.0
 */


package com.maan.veh.claim.repository;

import java.util.List;
import java.util.Map;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import com.maan.veh.claim.entity.ListItemValue;
import com.maan.veh.claim.entity.ListItemValueId;
/**
 * <h2>ListItemValueRepository</h2>
 *
 * createdAt : 2022-08-24 - Time 12:58:27
 * <p>
 * Description: "ListItemValue" Repository
 */
 
 
 
public interface ListItemValueRepository  extends JpaRepository<ListItemValue,ListItemValueId > , JpaSpecificationExecutor<ListItemValue> {

	List<ListItemValue> findByItemTypeAndStatusOrderByItemCodeAsc(String itemType, String string);




}