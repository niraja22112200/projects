package com.imsapp.ims.dto.policy;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

 class PolicyResponseTest {

    @Test
     void testPolicyResponse() {
        PolicyResponse policyResponse = new PolicyResponse();

        // Test setting and getting id
        policyResponse.setId(1L);
        assertEquals(1L, policyResponse.getId());

        // Test setting and getting policyNumber
        policyResponse.setPolicyNumber("12345");
        assertEquals("12345", policyResponse.getPolicyNumber());

        // Test setting and getting policyType
        policyResponse.setPolicyType("Health");
        assertEquals("Health", policyResponse.getPolicyType());

        // Test setting and getting coverageDetails
        policyResponse.setCoverageDetails("Full Coverage");
        assertEquals("Full Coverage", policyResponse.getCoverageDetails());

        // Test setting and getting contactInfo
        policyResponse.setContactInfo("contact@example.com");
        assertEquals("contact@example.com", policyResponse.getContactInfo());
    }
}

