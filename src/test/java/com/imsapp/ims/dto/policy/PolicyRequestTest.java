package com.imsapp.ims.dto.policy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PolicyRequestTest {

    @Test
     void testPolicyRequest() {
        PolicyRequest policyRequest = new PolicyRequest();

        // Test setting and getting policyNumber
        policyRequest.setPolicyNumber("12345");
        assertEquals("12345", policyRequest.getPolicyNumber());

        // Test setting and getting policyType
        policyRequest.setPolicyType("Health");
        assertEquals("Health", policyRequest.getPolicyType());

        // Test setting and getting coverageDetails
        policyRequest.setCoverageDetails("Full Coverage");
        assertEquals("Full Coverage", policyRequest.getCoverageDetails());

        // Test setting and getting contactInfo
        policyRequest.setContactInfo("contact@example.com");
        assertEquals("contact@example.com", policyRequest.getContactInfo());
    }
}

