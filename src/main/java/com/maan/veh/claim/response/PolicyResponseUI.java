package com.maan.veh.claim.response;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import java.util.List;

@Data
public class PolicyResponseUI {

    @JsonProperty("PolicyNumber")
    private String policyNumber;

    @JsonProperty("InsuredId")
    private String insuredId;

    @JsonProperty("PolicyFromDate")
    private String policyFromDate;

    @JsonProperty("PolicyToDate")
    private String policyToDate;
    
    @JsonProperty("EngFullName")
    private String engFullName;

    @JsonProperty("CustomerDetails")
    private CustomerDetailsUI customerDetails;

    @Data
    public static class CustomerDetailsUI {
        @JsonProperty("PolicyHolder")
        private List<PolicyHolderUI> policyHolder;
    }

    @Data
    public static class PolicyHolderUI {
        @JsonProperty("EngFullName")
        private String engFullName;
    }
}

