package com.imsapp.ims.service.policy;

import com.imsapp.ims.entity.policy.Policy;
import com.imsapp.ims.repository.policy.PolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PolicyServiceTest {

    @Mock
    private PolicyRepository policyRepository;

    @InjectMocks
    private PolicyService policyService;

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
        when(policyRepository.findAll()).thenReturn(policies);

        List<Policy> foundPolicies = policyService.getAllPolicies();
        assertEquals(2, foundPolicies.size());
        assertEquals("12345", foundPolicies.get(0).getPolicyNumber());
        assertEquals("67890", foundPolicies.get(1).getPolicyNumber());
    }

    @Test
    void testGetPolicyById() {
        Policy policy = new Policy();
        policy.setId(1L);
        policy.setPolicyNumber("12345");
        policy.setPolicyType("Health");
        policy.setCoverageDetails("Full Coverage");
        policy.setContactInfo("contact@example.com");

        when(policyRepository.findById(1L)).thenReturn(Optional.of(policy));

        Optional<Policy> foundPolicy = policyService.getPolicyById(1L);
        assertEquals(1L, foundPolicy.get().getId());
        assertEquals("12345", foundPolicy.get().getPolicyNumber());
    }

    @Test
    void testSavePolicy() {
        Policy policy = new Policy();
        policy.setPolicyNumber("12345");
        policy.setPolicyType("Health");
        policy.setCoverageDetails("Full Coverage");
        policy.setContactInfo("contact@example.com");

        when(policyRepository.save(any(Policy.class))).thenReturn(policy);

        Policy savedPolicy = policyService.savePolicy(policy);
        verify(policyRepository, times(1)).save(policy);
        assertEquals("12345", savedPolicy.getPolicyNumber());
    }

    @Test
    void testDeletePolicy() {
        doNothing().when(policyRepository).deleteById(1L);

        policyService.deletePolicy(1L);
        verify(policyRepository, times(1)).deleteById(1L);
    }
}

