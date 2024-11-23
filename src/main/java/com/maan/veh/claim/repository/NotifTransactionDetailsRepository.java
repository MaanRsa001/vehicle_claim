package com.maan.veh.claim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.maan.veh.claim.entity.NotifTransactionDetails;

public interface NotifTransactionDetailsRepository  extends JpaRepository<NotifTransactionDetails,Integer > , JpaSpecificationExecutor<NotifTransactionDetails> ,PagingAndSortingRepository<NotifTransactionDetails, Integer> {

}
