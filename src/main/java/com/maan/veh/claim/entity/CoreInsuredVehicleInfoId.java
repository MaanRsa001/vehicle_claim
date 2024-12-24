package com.maan.veh.claim.entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoreInsuredVehicleInfoId implements Serializable {

    private Integer companyId;
    private String policyNo;
    private String claimNo;
    private String garageId;

    private static final long serialVersionUID = 1L;
}
