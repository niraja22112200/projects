package com.imsapp.ims.controller.policy;

import com.imsapp.ims.dto.policy.PolicyRequest;
import com.imsapp.ims.dto.policy.PolicyResponse;
import com.imsapp.ims.entity.policy.Policy;
import com.imsapp.ims.exception.PolicyNotFoundException;
import com.imsapp.ims.service.policy.PolicyService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PolicyControllerTest {

    @Mock
    private PolicyService policyService;

    @InjectMocks
    private PolicyController policyController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllPolicies() {
        Policy policy1 = new Policy();
        policy1.setId(1L);
        policy1.setPolicyNumber("12345");
        policy1.setPolicyType("Health");
        policy1.setCoverageDetails("Full Coverage");
        policy1.setContactInfo("contact@example.com");

        Policy policy2 = new Policy();
        policy2.setId(2L);
        policy2.setPolicyNumber("67890");
        policy2.setPolicyType("Life");
        policy2.setCoverageDetails("Partial Coverage");
        policy2.setContactInfo("contact2@example.com");

        List<Policy> policies = Arrays.asList(policy1, policy2);
        when(policyService.getAllPolicies()).thenReturn(policies);

        List<PolicyResponse> response = policyController.getAllPolicies();
        assertEquals(2, response.size());
        assertEquals("12345", response.get(0).getPolicyNumber());
        assertEquals("67890", response.get(1).getPolicyNumber());
    }

    @Test
    void testGetPolicyById() {
        Policy policy = new Policy();
        policy.setId(1L);
        policy.setPolicyNumber("12345");
        policy.setPolicyType("Health");
        policy.setCoverageDetails("Full Coverage");
        policy.setContactInfo("contact@example.com");

        when(policyService.getPolicyById(1L)).thenReturn(Optional.of(policy));

        ResponseEntity<PolicyResponse> response = policyController.getPolicyById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("12345", response.getBody().getPolicyNumber());
    }

    @Test
    void testGetPolicyById_NotFound() {
        when(policyService.getPolicyById(1L)).thenReturn(Optional.empty());

        try {
            policyController.getPolicyById(1L);
        } catch (PolicyNotFoundException ex) {
            assertEquals("Policy not found with id: 1", ex.getMessage());
        }
    }

    @Test
    void testCreatePolicy() {
        PolicyRequest policyRequest = new PolicyRequest();
        policyRequest.setPolicyNumber("12345");
        policyRequest.setPolicyType("Health");
        policyRequest.setCoverageDetails("Full Coverage");
        policyRequest.setContactInfo("contact@example.com");

        Policy policy = new Policy();
        policy.setPolicyNumber("12345");
        policy.setPolicyType("Health");
        policy.setCoverageDetails("Full Coverage");
        policy.setContactInfo("contact@example.com");

        when(policyService.savePolicy(any(Policy.class))).thenReturn(policy);

        PolicyResponse response = policyController.createPolicy(policyRequest);
        assertEquals("12345", response.getPolicyNumber());
    }

    @Test
    void testUpdatePolicy() {
        PolicyRequest policyRequest = new PolicyRequest();
        policyRequest.setPolicyNumber("12345");
        policyRequest.setPolicyType("Health");
        policyRequest.setCoverageDetails("Full Coverage");
        policyRequest.setContactInfo("contact@example.com");

        Policy policy = new Policy();
        policy.setId(1L);
        policy.setPolicyNumber("12345");
        policy.setPolicyType("Health");
        policy.setCoverageDetails("Full Coverage");
        policy.setContactInfo("contact@example.com");

        when(policyService.getPolicyById(1L)).thenReturn(Optional.of(policy));
        when(policyService.savePolicy(any(Policy.class))).thenReturn(policy);

        ResponseEntity<PolicyResponse> response = policyController.updatePolicy(1L, policyRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("12345", response.getBody().getPolicyNumber());
    }

    @Test
    void testUpdatePolicy_NotFound() {
        PolicyRequest policyRequest = new PolicyRequest();
        policyRequest.setPolicyNumber("12345");
        policyRequest.setPolicyType("Health");
        policyRequest.setCoverageDetails("Full Coverage");
        policyRequest.setContactInfo("contact@example.com");

        when(policyService.getPolicyById(1L)).thenReturn(Optional.empty());

        try {
            policyController.updatePolicy(1L, policyRequest);
        } catch (PolicyNotFoundException ex) {
            assertEquals("Policy not found with id: 1", ex.getMessage());
        }
    }

    @Test
    void testDeletePolicy() {
        Policy policy = new Policy();
        policy.setId(1L);
        policy.setPolicyNumber("12345");
        policy.setPolicyType("Health");
        policy.setCoverageDetails("Full Coverage");
        policy.setContactInfo("contact@example.com");

        when(policyService.getPolicyById(1L)).thenReturn(Optional.of(policy));
        doNothing().when(policyService).deletePolicy(1L);

        ResponseEntity<Void> response = policyController.deletePolicy(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(policyService, times(1)).deletePolicy(1L);
    }

    @Test
    void testDeletePolicy_NotFound() {
        when(policyService.getPolicyById(1L)).thenReturn(Optional.empty());

        try {
            policyController.deletePolicy(1L);
        } catch (PolicyNotFoundException ex) {
            assertEquals("Policy not found with id: 1", ex.getMessage());
        }
    }
}
