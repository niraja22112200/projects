package com.imsapp.ims.exception;

public class PaymentProcessingException extends RuntimeException {
    public PaymentProcessingException(String message, String cause) {
        super(message);
    }
}

