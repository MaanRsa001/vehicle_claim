package com.maan.veh.claim.entity;
import java.util.Date;

import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "vc_document_upload_details")
@IdClass(VcDocumentUploadDetailsId.class)
public class VcDocumentUploadDetails {

    @Id
    @Column(name = "CLAIM_NO", nullable = false, length = 100)
    private String claimNo;
    
    @Id
    @Column(name = "DOCUMENT_REF")
    private Long documentRef;

    @Id
    @Column(name = "COMPANY_ID", nullable = false)
    private int companyId;

    @Column(name = "DOC_TYPE_ID")
    private Integer docTypeId;

    @Column(name = "DOC_NAME", length = 200)
    private String docName;

    @Column(name = "FILE_PATH_NAME", length = 200)
    private String filePathName;

    @Column(name = "UPLOADED_TIME")
    private Date uploadedTime;

    @Column(name = "DESCRIPTION", length = 200)
    private String description;

    @Column(name = "FILE_NAME", length = 500)
    private String fileName;

    @Column(name = "UPLOAD_TYPE", length = 100)
    private String uploadType;

    @Column(name = "COMMON_FILE_PATH", length = 200)
    private String commonFilePath;

    @Column(name = "ERROR_RES", length = 200)
    private String errorRes;

    @Column(name = "UPLOADED_BY", length = 100)
    private String uploadedBy;
    
    @Column(name = "USER_TYPE")
    private String userType;

    @Column(name = "REMARKS", length = 200)
    private String remarks;
    
    @Column(name = "FILE_TYPE")
    private String fileType;
}
