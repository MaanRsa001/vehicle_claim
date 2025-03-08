package com.maan.veh.claim.dto;
import lombok.Data;
import java.util.List;

@Data
public class PolicyResponseDTO {
    private boolean hasError;
    private int status;
    private String message;
    private PolicyData data;

    @Data
    public static class PolicyData {
        private String policyNumber;
        private String insuredId;
        private String policyFromDate;
        private String policyToDate;
        private CustomerDetails customerDetails;

    }

    @Data
    public static class CustomerDetails {
        private List<PolicyHolder> policyHolder;
    }

    @Data
    public static class PolicyHolder {
        private String engFullName;
    }

}

