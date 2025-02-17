package com.maan.veh.claim.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "API_INTEG_MASTER")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ApiIntegMaster {

    @Id
    @Column(name = "COMPANY_ID")
    private String companyId;

    @Column(name = "PRODUCT_ID")
    private Integer productId;

    @Column(name = "API_TYPE")
    private String apiType;

    @Column(name = "API_DESC")
    private String apiDesc;

    @Column(name = "API_URL")
    private String apiUrl;

    @Column(name = "STATUS")
    private String status;
}
