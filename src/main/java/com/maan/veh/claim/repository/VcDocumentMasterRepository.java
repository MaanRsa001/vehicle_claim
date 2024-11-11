package com.maan.veh.claim.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.maan.veh.claim.entity.VcDocumentMaster;
import com.maan.veh.claim.entity.VcDocumentMasterId;

@Repository
public interface VcDocumentMasterRepository extends JpaRepository<VcDocumentMaster, VcDocumentMasterId> {
    // You can add custom query methods here if needed
}
