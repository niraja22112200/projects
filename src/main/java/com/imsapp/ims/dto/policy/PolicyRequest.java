package com.imsapp.ims.dto.policy;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PolicyRequest {
    private String policyNumber;
    private String policyType;
    private String coverageDetails;
    private String contactInfo;
    private Double totalPremiumAmount;
}

