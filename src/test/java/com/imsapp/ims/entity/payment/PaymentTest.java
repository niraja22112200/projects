package com.imsapp.ims.entity.payment;

import com.imsapp.ims.entity.policy.Policy;
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

 class PaymentTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testPaymentFields() {
        Payment payment = new Payment();
        payment.setId(1L);
        payment.setAmount(1000.0);
        payment.setDatePaid(LocalDate.now());
        Policy policy = new Policy();
        payment.setPolicy(policy);

        assertEquals(1L, payment.getId());
        assertEquals(1000.0, payment.getAmount());
        assertEquals(LocalDate.now(), payment.getDatePaid());
        assertEquals(policy, payment.getPolicy());
    }

    @Test
    void testPaymentValidation() {
        Payment payment = new Payment();
        payment.setAmount(1000.0);
        payment.setDatePaid(LocalDate.now());
        Policy policy = new Policy();
        payment.setPolicy(policy);

        Set<ConstraintViolation<Payment>> violations = validator.validate(payment);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testPaymentValidationWithNullFields() {
        Payment payment = new Payment();
        Set<ConstraintViolation<Payment>> violations = validator.validate(payment);
        assertEquals(1, violations.size());
    }

    @Test
    void testPaymentValidationWithNegativeAmount() {
        Payment payment = new Payment();
        payment.setAmount(-1000.0);
        payment.setDatePaid(LocalDate.now());
        Policy policy = new Policy();
        payment.setPolicy(policy);

        Set<ConstraintViolation<Payment>> violations = validator.validate(payment);
        assertEquals(1, violations.size());
        assertEquals("Amount must be positive", violations.iterator().next().getMessage());
    }

    @Test
    void testPaymentEqualsAndHashCode() {
        Payment payment1 = new Payment();
        payment1.setId(1L);
        payment1.setAmount(1000.0);
        payment1.setDatePaid(LocalDate.now());
        Policy policy1 = new Policy();
        payment1.setPolicy(policy1);

        Payment payment2 = new Payment();
        payment2.setId(1L);
        payment2.setAmount(1000.0);
        payment2.setDatePaid(LocalDate.now());
        Policy policy2 = new Policy();
        payment2.setPolicy(policy2);

        assertEquals(payment1, payment2);
        assertEquals(payment1.hashCode(), payment2.hashCode());
    }
}
