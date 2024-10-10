package com.maan.veh.claim.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vc_flow_master")
@IdClass(VcFlowMasterId.class)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VcFlowMaster {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SNO")
    private Long sno;

    @Id
    @Column(name = "FLOW_ID", length = 20)
    private String flowId;

    @Id
    @Column(name = "STATUS_ID", length = 20)
    private String statusId;

    @Id
    @Column(name = "COMPANY_ID", length = 100)
    private String companyId;

    @Column(name = "STATUS_DESCRIPTION", length = 100, nullable = false)
    private String statusDescription;

    @Column(name = "USERTYPE", length = 20, nullable = false)
    private String usertype;

    @Column(name = "SUB_STATUS", length = 20, nullable = false)
    private String subStatus;

    @Column(name = "SUB_STATUS_DESCRIPTION", length = 100, nullable = false)
    private String subStatusDescription;

    @Column(name = "ORDER_ID", length = 50)
    private String orderId;

    @Column(name = "THRESHOLD_DAYS", length = 50)
    private String thresholdDays;
}
