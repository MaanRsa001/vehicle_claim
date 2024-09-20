package com.maan.veh.claim.request;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ClaimIntimationThirdPartyInfo {
    @JsonProperty("TPDriverLiability")
    private String tpDriverLiability;

    @JsonProperty("TPDriverLicenceNo")
    private String tpDriverLicenceNo;

    @JsonProperty("TPDriverName")
    private String tpDriverName;

    @JsonProperty("TPDriverNationalityCode")
    private String tpDriverNationalityCode;

    @JsonProperty("TPDriverTrafficNo")
    private String tpDriverTrafficNo;

    @JsonProperty("TPMobileNumber")
    private String tpMobileNumber;

    @JsonProperty("TPVehicleCurrentInsurer")
    private String tpVehicleCurrentInsurer;

    @JsonProperty("TPVehicleMake")
    private String tpVehicleMake;

    @JsonProperty("TPVehicleMakeCode")
    private String tpVehicleMakeCode;

    @JsonProperty("TPVehicleModel")
    private String tpVehicleModel;

    @JsonProperty("TPVehicleModelCode")
    private String tpVehicleModelCode;

    @JsonProperty("TPVehiclePlateCode")
    private String tpVehiclePlateCode;

    @JsonProperty("TPVehiclePlateNo")
    private String tpVehiclePlateNo;

    @JsonProperty("TPVehiclePlateTypeCode")
    private String tpVehiclePlateTypeCode;

    @JsonProperty("ThirdPartyReference")
    private String thirdPartyReference;

    @JsonProperty("ThirdPartyType")
    private String thirdPartyType;
}
