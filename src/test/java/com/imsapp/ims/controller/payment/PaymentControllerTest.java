package com.imsapp.ims.controller.payment;

import com.imsapp.ims.entity.payment.Payment;
import com.imsapp.ims.service.payment.PaymentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPayPremium() {
        String policyNumber = "12345";
        Double amount = 1000.0;
        Payment payment = new Payment();
        when(paymentService.payPremium(policyNumber, amount)).thenReturn(payment);

        ResponseEntity<Payment> response = paymentController.payPremium(policyNumber, amount);

        assertEquals(ResponseEntity.ok(payment), response);
        verify(paymentService, times(1)).payPremium(policyNumber, amount);
    }

    @Test
    void testGetPaymentsByPolicy() {
        String policyNumber = "12345";
        List<Payment> payments = List.of(new Payment());
        when(paymentService.getPaymentsByPolicy(policyNumber)).thenReturn(payments);

        ResponseEntity<List<Payment>> response = paymentController.getPaymentsByPolicy(policyNumber);

        assertEquals(ResponseEntity.ok(payments), response);
        verify(paymentService, times(1)).getPaymentsByPolicy(policyNumber);
    }

    @Test
    void testUpdatePayment() {
        Long id = 1L;
        Double newAmount = 2000.0;
        Payment updatedPayment = new Payment();
        when(paymentService.updatePayment(id, newAmount)).thenReturn(updatedPayment);

        ResponseEntity<Payment> response = paymentController.updatePayment(id, newAmount);

        assertEquals(ResponseEntity.ok(updatedPayment), response);
        verify(paymentService, times(1)).updatePayment(id, newAmount);
    }

    @Test
    void testGetTotalPaidAmount() {
        String policyNumber = "12345";
        Double totalPaid = 5000.0;
        when(paymentService.getTotalPaidAmount(policyNumber)).thenReturn(totalPaid);

        ResponseEntity<Double> response = paymentController.getTotalPaidAmount(policyNumber);

        assertEquals(ResponseEntity.ok(totalPaid), response);
        verify(paymentService, times(1)).getTotalPaidAmount(policyNumber);
    }

    @Test
    void testCheckBalancePremium() {
        String policyNumber = "12345";
        Double balancePremium = 1500.0;
        when(paymentService.checkBalancePremium(policyNumber)).thenReturn(balancePremium);

        ResponseEntity<Double> response = paymentController.checkBalancePremium(policyNumber);

        assertEquals(ResponseEntity.ok(balancePremium), response);
        verify(paymentService, times(1)).checkBalancePremium(policyNumber);
    }

    @Test
    void testIsPolicyFullyPaid() {
        String policyNumber = "12345";
        boolean isFullyPaid = true;
        when(paymentService.isPolicyFullyPaid(policyNumber)).thenReturn(isFullyPaid);

        ResponseEntity<Boolean> response = paymentController.isPolicyFullyPaid(policyNumber);

        assertEquals(ResponseEntity.ok(isFullyPaid), response);
        verify(paymentService, times(1)).isPolicyFullyPaid(policyNumber);
    }
}

