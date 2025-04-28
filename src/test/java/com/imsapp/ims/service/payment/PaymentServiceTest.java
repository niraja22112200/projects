package com.imsapp.ims.service.payment;

import com.imsapp.ims.entity.payment.Payment;
import com.imsapp.ims.entity.policy.Policy;
import com.imsapp.ims.exception.PaymentProcessingException;
import com.imsapp.ims.exception.PolicyNotFoundException;
import com.imsapp.ims.repository.payment.PaymentRepository;
import com.imsapp.ims.repository.policy.PolicyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private PolicyRepository policyRepository;

    @InjectMocks
    private PaymentService paymentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPayPremium() {
        String policyNumber = "12345";
        Double amount = 1000.0;
        Policy policy = new Policy();
        Payment payment = new Payment();
        when(policyRepository.findByPolicyNumber(policyNumber)).thenReturn(policy);
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.payPremium(policyNumber, amount);

        assertNotNull(result);
        verify(policyRepository, times(1)).findByPolicyNumber(policyNumber);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }

    @Test
    void testPayPremiumPolicyNotFound() {
        String policyNumber = "12345";
        Double amount = 1000.0;
        when(policyRepository.findByPolicyNumber(policyNumber)).thenReturn(null);

        assertThrows(PolicyNotFoundException.class, () -> paymentService.payPremium(policyNumber, amount));
        verify(policyRepository, times(1)).findByPolicyNumber(policyNumber);
    }
    @Test
    void testPayPremiumProcessingException() {
        String policyNumber = "12345";
        Double amount = 1000.0;
        Policy policy = new Policy();

        when(policyRepository.findByPolicyNumber(policyNumber)).thenReturn(policy);
        when(paymentRepository.save(any(Payment.class))).thenThrow(new RuntimeException("Database error"));

        Exception exception = assertThrows(PaymentProcessingException.class, () -> paymentService.payPremium(policyNumber, amount));
        assertEquals("An error occurred while processing the payment", exception.getMessage());

        verify(policyRepository, times(1)).findByPolicyNumber(policyNumber);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }


    @Test
    void testCheckBalancePremium() {
        String policyNumber = "12345";
        Policy policy = new Policy();
        policy.setTotalPremiumAmount(5000.0);
        Payment payment = new Payment();
        payment.setAmount(1000.0);
        when(policyRepository.findByPolicyNumber(policyNumber)).thenReturn(policy);
        when(paymentRepository.findByPolicy(policy)).thenReturn(List.of(payment));

        Double balance = paymentService.checkBalancePremium(policyNumber);

        assertEquals(4000.0, balance);
        verify(policyRepository, times(1)).findByPolicyNumber(policyNumber);
        verify(paymentRepository, times(1)).findByPolicy(policy);
    }

    @Test
    void testCheckBalancePremiumPolicyNotFound() {
        String policyNumber = "12345";
        when(policyRepository.findByPolicyNumber(policyNumber)).thenReturn(null);

        assertThrows(PolicyNotFoundException.class, () -> paymentService.checkBalancePremium(policyNumber));
        verify(policyRepository, times(1)).findByPolicyNumber(policyNumber);
    }

    @Test
    void testGetPaymentsByPolicy() {
        String policyNumber = "12345";
        Policy policy = new Policy();
        Payment payment = new Payment();
        when(policyRepository.findByPolicyNumber(policyNumber)).thenReturn(policy);
        when(paymentRepository.findByPolicy(policy)).thenReturn(List.of(payment));

        List<Payment> payments = paymentService.getPaymentsByPolicy(policyNumber);

        assertEquals(1, payments.size());
        verify(policyRepository, times(1)).findByPolicyNumber(policyNumber);
        verify(paymentRepository, times(1)).findByPolicy(policy);
    }

    @Test
    void testGetPaymentsByPolicyPolicyNotFound() {
        String policyNumber = "12345";
        when(policyRepository.findByPolicyNumber(policyNumber)).thenReturn(null);

        assertThrows(PolicyNotFoundException.class, () -> paymentService.getPaymentsByPolicy(policyNumber));
        verify(policyRepository, times(1)).findByPolicyNumber(policyNumber);
    }

    @Test
    void testUpdatePayment() {
        Long paymentId = 1L;
        Double newAmount = 2000.0;
        Payment payment = new Payment();
        Policy policy = new Policy();
        policy.setTotalPremiumAmount(5000.0); // Set the totalPremiumAmount field
        payment.setPolicy(policy);
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(payment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(payment);

        Payment result = paymentService.updatePayment(paymentId, newAmount);

        assertNotNull(result);
        verify(paymentRepository, times(1)).findById(paymentId);
        verify(paymentRepository, times(1)).save(any(Payment.class));
    }
    @Test
    void testUpdatePaymentNotFound() {
        Long paymentId = 1L;
        Double newAmount = 2000.0;
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        assertThrows(PaymentProcessingException.class, () -> paymentService.updatePayment(paymentId, newAmount));
        verify(paymentRepository, times(1)).findById(paymentId);
    }

    @Test
    void testGetTotalPaidAmount() {
        String policyNumber = "12345";
        Policy policy = new Policy();
        Payment payment = new Payment();
        payment.setAmount(1000.0);
        when(policyRepository.findByPolicyNumber(policyNumber)).thenReturn(policy);
        when(paymentRepository.findByPolicy(policy)).thenReturn(List.of(payment));

        Double totalPaid = paymentService.getTotalPaidAmount(policyNumber);

        assertEquals(1000.0, totalPaid);
        verify(policyRepository, times(1)).findByPolicyNumber(policyNumber);
        verify(paymentRepository, times(1)).findByPolicy(policy);
    }

    @Test
    void testGetTotalPaidAmountPolicyNotFound() {
        String policyNumber = "12345";
        when(policyRepository.findByPolicyNumber(policyNumber)).thenReturn(null);

        assertThrows(PolicyNotFoundException.class, () -> paymentService.getTotalPaidAmount(policyNumber));
        verify(policyRepository, times(1)).findByPolicyNumber(policyNumber);
    }

    @Test
    void testIsPolicyFullyPaid() {
        String policyNumber = "12345";
        Policy policy = new Policy();
        policy.setTotalPremiumAmount(5000.0);
        Payment payment = new Payment();
        payment.setAmount(5000.0);
        when(policyRepository.findByPolicyNumber(policyNumber)).thenReturn(policy);
        when(paymentRepository.findByPolicy(policy)).thenReturn(List.of(payment));

        boolean isFullyPaid = paymentService.isPolicyFullyPaid(policyNumber);

        assertTrue(isFullyPaid);
        verify(policyRepository, times(2)).findByPolicyNumber(policyNumber); // Expecting 2 invocations
        verify(paymentRepository, times(1)).findByPolicy(policy);
    }

    @Test
    void testIsPolicyFullyPaidPolicyNotFound() {
        String policyNumber = "12345";
        when(policyRepository.findByPolicyNumber(policyNumber)).thenReturn(null);

        assertThrows(PolicyNotFoundException.class, () -> paymentService.isPolicyFullyPaid(policyNumber));
        verify(policyRepository, times(1)).findByPolicyNumber(policyNumber);
    }
    @Test
    void testValidateAmountPositiveAndNonNull0() throws Exception {
        Policy policy = new Policy();
        policy.setTotalPremiumAmount(5000.0);

        Payment payment1 = new Payment();
        payment1.setAmount(1000.0);
        Payment payment2 = new Payment();
        payment2.setAmount(2000.0);

        when(paymentRepository.findByPolicy(policy)).thenReturn(List.of(payment1, payment2));

        Method method = PaymentService.class.getDeclaredMethod("validateAmount", Policy.class, Double.class);
        method.setAccessible(true);

        assertDoesNotThrow(() -> method.invoke(paymentService, policy, 1000.0));
    }
    @Test
    void testValidateAmountNullOrNegative() throws Exception {
        Policy policy = new Policy();
        policy.setTotalPremiumAmount(5000.0);

        Method method = PaymentService.class.getDeclaredMethod("validateAmount", Policy.class, Double.class);
        method.setAccessible(true);

        try {
            method.invoke(paymentService, policy, null);
            fail("Expected IllegalArgumentException for null amount");
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
            assertEquals("Amount must be positive and non-null", e.getCause().getMessage());
        }

        try {
            method.invoke(paymentService, policy, -1000.0);
            fail("Expected IllegalArgumentException for negative amount");
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
            assertEquals("Amount must be positive and non-null", e.getCause().getMessage());
        }
    }

    @Test
    void testValidateAmountExceedsTotalPremium() throws Exception {
        Policy policy = new Policy();
        policy.setTotalPremiumAmount(5000.0);

        Payment payment1 = new Payment();
        payment1.setAmount(3000.0);
        Payment payment2 = new Payment();
        payment2.setAmount(2000.0);

        when(paymentRepository.findByPolicy(policy)).thenReturn(List.of(payment1, payment2));

        Method method = PaymentService.class.getDeclaredMethod("validateAmount", Policy.class, Double.class);
        method.setAccessible(true);

        try {
            method.invoke(paymentService, policy, 1000.0);
            fail("Expected IllegalArgumentException for amount exceeding total premium");
        } catch (Exception e) {
            assertTrue(e.getCause() instanceof IllegalArgumentException);
            assertEquals("Paid amount exceeds the total policy amount", e.getCause().getMessage());
        }
    }

    @Test
    void testIsPolicyFullyPaidNotFullyPaid() {
        String policyNumber = "12345";
        Policy policy = new Policy();
        policy.setTotalPremiumAmount(5000.0);
        Payment payment = new Payment();
        payment.setAmount(3000.0);

        when(policyRepository.findByPolicyNumber(policyNumber)).thenReturn(policy);
        when(paymentRepository.findByPolicy(policy)).thenReturn(List.of(payment));

        boolean isFullyPaid = paymentService.isPolicyFullyPaid(policyNumber);
        assertFalse(isFullyPaid);

        verify(policyRepository, times(2)).findByPolicyNumber(policyNumber);
        verify(paymentRepository, times(1)).findByPolicy(policy);
    }


}


