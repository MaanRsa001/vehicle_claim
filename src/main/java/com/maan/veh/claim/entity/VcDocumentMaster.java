package com.maan.veh.claim.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "vc_document_master")
@IdClass(VcDocumentMasterId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VcDocumentMaster {

    @Id
    @Column(name = "DOCUMENT_ID", nullable = false)
    private int documentId;

    @Id
    @Column(name = "COMPANY_ID", nullable = false)
    private int companyId;

    @Column(name = "DOCUMENT_NAME", nullable = false, length = 100)
    private String documentName;

    @Column(name = "MANDATORY_STATUS", length = 2)
    private String mandatoryStatus;

    @Column(name = "STATUS", length = 2)
    private String status;

    @Column(name = "ENTRY_DATE")
    @Temporal(TemporalType.DATE)
    private Date entryDate;

    @Column(name = "REMARKS", length = 100)
    private String remarks;
}

