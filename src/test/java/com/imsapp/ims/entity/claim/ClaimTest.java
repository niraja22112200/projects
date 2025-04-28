package com.imsapp.ims.entity.claim;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

 class ClaimTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testClaimFields() {
        Claim claim = new Claim();
        claim.setId(1L);
        claim.setPolicy(123L);
        claim.setStatus("Pending");
        claim.setAmount(1000.0);
        claim.setDateFiled(LocalDate.now());

        assertEquals(1L, claim.getId());
        assertEquals(123L, claim.getPolicy());
        assertEquals("Pending", claim.getStatus());
        assertEquals(1000.0, claim.getAmount());
        assertEquals(LocalDate.now(), claim.getDateFiled());
    }

    @Test
    void testClaimValidation() {
        Claim claim = new Claim();
        claim.setPolicy(123L);
        claim.setStatus("Pending");
        claim.setAmount(1000.0);
        claim.setDateFiled(LocalDate.now());

        Set<ConstraintViolation<Claim>> violations = validator.validate(claim);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testClaimValidationWithNullFields() {
        Claim claim = new Claim();
        Set<ConstraintViolation<Claim>> violations = validator.validate(claim);
        assertEquals(4, violations.size());
    }

    @Test
    void testClaimValidationWithInvalidAmount() {
        Claim claim = new Claim();
        claim.setPolicy(123L);
        claim.setStatus("Pending");
        claim.setAmount(-1000.0);
        claim.setDateFiled(LocalDate.now());

        Set<ConstraintViolation<Claim>> violations = validator.validate(claim);
        assertEquals(1, violations.size());
        assertEquals("Amount must be positive", violations.iterator().next().getMessage());
    }

    @Test
    void testClaimEqualsAndHashCode() {
        Claim claim1 = new Claim();
        claim1.setId(1L);
        claim1.setPolicy(123L);
        claim1.setStatus("Pending");
        claim1.setAmount(1000.0);
        claim1.setDateFiled(LocalDate.now());

        Claim claim2 = new Claim();
        claim2.setId(1L);
        claim2.setPolicy(123L);
        claim2.setStatus("Pending");
        claim2.setAmount(1000.0);
        claim2.setDateFiled(LocalDate.now());

        assertEquals(claim1, claim2);
        assertEquals(claim1.hashCode(), claim2.hashCode());
    }
}

