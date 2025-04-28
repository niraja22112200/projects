package com.imsapp.ims.entity.policy;

import com.imsapp.ims.entity.payment.Payment;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PolicyTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testPolicyFields() {
        Policy policy = new Policy();
        policy.setId(1L);
        policy.setPolicyNumber("POL123456");
        policy.setPolicyType("Health");
        policy.setCoverageDetails("Full Coverage");
        policy.setContactInfo("123-456-7890");
        policy.setTotalPremiumAmount(5000.0);
        Payment payment = new Payment();
        policy.setPayments(List.of(payment));

        assertEquals(1L, policy.getId());
        assertEquals("POL123456", policy.getPolicyNumber());
        assertEquals("Health", policy.getPolicyType());
        assertEquals("Full Coverage", policy.getCoverageDetails());
        assertEquals("123-456-7890", policy.getContactInfo());
        assertEquals(5000.0, policy.getTotalPremiumAmount());
        assertEquals(List.of(payment), policy.getPayments());
    }

    @Test
    void testPolicyValidation() {
        Policy policy = new Policy();
        policy.setPolicyNumber("POL123456");
        policy.setPolicyType("Health");
        policy.setCoverageDetails("Full Coverage");
        policy.setContactInfo("123-456-7890");
        policy.setTotalPremiumAmount(5000.0);
        Payment payment = new Payment();
        policy.setPayments(List.of(payment));

        Set<ConstraintViolation<Policy>> violations = validator.validate(policy);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testPolicyValidationWithNullFields() {
        Policy policy = new Policy();
        Set<ConstraintViolation<Policy>> violations = validator.validate(policy);
        assertEquals(4, violations.size());
    }

    @Test
    void testPolicyValidationWithInvalidPolicyNumber() {
        Policy policy = new Policy();
        policy.setPolicyNumber("POL12345678901234567890"); // Exceeds 20 characters
        policy.setPolicyType("Health");
        policy.setCoverageDetails("Full Coverage");
        policy.setContactInfo("123-456-7890");
        policy.setTotalPremiumAmount(5000.0);
        Payment payment = new Payment();
        policy.setPayments(List.of(payment));

        Set<ConstraintViolation<Policy>> violations = validator.validate(policy);
        assertEquals(1, violations.size());
        assertEquals("Policy number must be less than 20 characters", violations.iterator().next().getMessage());
    }

    @Test
    void testPolicyEqualsAndHashCode() {
        Policy policy1 = new Policy();
        policy1.setId(1L);
        policy1.setPolicyNumber("POL123456");
        policy1.setPolicyType("Health");
        policy1.setCoverageDetails("Full Coverage");
        policy1.setContactInfo("123-456-7890");
        policy1.setTotalPremiumAmount(5000.0);
        Payment payment1 = new Payment();
        policy1.setPayments(List.of(payment1));

        Policy policy2 = new Policy();
        policy2.setId(1L);
        policy2.setPolicyNumber("POL123456");
        policy2.setPolicyType("Health");
        policy2.setCoverageDetails("Full Coverage");
        policy2.setContactInfo("123-456-7890");
        policy2.setTotalPremiumAmount(5000.0);
        Payment payment2 = new Payment();
        policy2.setPayments(List.of(payment2));

        assertEquals(policy1, policy2);
        assertEquals(policy1.hashCode(), policy2.hashCode());
    }
}

