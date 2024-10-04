package com.maan.veh.claim.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimIntimationDTOThirdPartyInfo {
    @JsonProperty("tPDriverLiability")
    private String tpDriverLiability;

    @JsonProperty("tPDriverLicenceNo")
    private String tpDriverLicenceNo;

    @JsonProperty("tPDriverName")
    private String tpDriverName;

    @JsonProperty("tPDriverNationalityCode")
    private String tpDriverNationalityCode;

    @JsonProperty("tPDriverTrafficNo")
    private String tpDriverTrafficNo;

    @JsonProperty("tPMobileNumber")
    private String tpMobileNumber;

    @JsonProperty("tPVehicleCurrentInsurer")
    private String tpVehicleCurrentInsurer;

    @JsonProperty("tPVehicleMake")
    private String tpVehicleMake;

    @JsonProperty("tPVehicleMakeCode")
    private String tpVehicleMakeCode;

    @JsonProperty("tPVehicleModel")
    private String tpVehicleModel;

    @JsonProperty("tPVehicleModelCode")
    private String tpVehicleModelCode;

    @JsonProperty("tPVehiclePlateCode")
    private String tpVehiclePlateCode;

    @JsonProperty("tPVehiclePlateNo")
    private String tpVehiclePlateNo;

    @JsonProperty("tPVehiclePlateTypeCode")
    private String tpVehiclePlateTypeCode;

    @JsonProperty("thirdPartyReference")
    private String thirdPartyReference;

    @JsonProperty("thirdPartyType")
    private String thirdPartyType;
}
