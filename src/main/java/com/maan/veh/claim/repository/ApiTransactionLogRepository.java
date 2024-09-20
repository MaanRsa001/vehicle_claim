package com.maan.veh.claim.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.veh.claim.entity.ApiTransactionLog;

@Repository
public interface ApiTransactionLogRepository extends JpaRepository<ApiTransactionLog, Long> {
    // You can define custom queries here if needed
}
