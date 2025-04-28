package com.imsapp.ims.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PaymentProcessingExceptionTest {

    @Test
    void testPaymentProcessingExceptionMessage() {
        String errorMessage = "Payment processing error";
        String errorCause = "Insufficient funds";
        PaymentProcessingException exception = new PaymentProcessingException(errorMessage, errorCause);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testPaymentProcessingExceptionThrown() {
        String errorMessage = "Payment processing error";
        String errorCause = "Insufficient funds";
        Exception exception = assertThrows(PaymentProcessingException.class, () -> {
            throw new PaymentProcessingException(errorMessage, errorCause);
        });
        assertEquals(errorMessage, exception.getMessage());
    }
}