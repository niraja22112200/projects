package com.imsapp.ims.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PolicyNotFoundExceptionTest {

    @Test
    void testPolicyNotFoundExceptionMessage() {
        String errorMessage = "Policy not found with id: 1";
        PolicyNotFoundException exception = assertThrows(PolicyNotFoundException.class, () -> {
            throw new PolicyNotFoundException(errorMessage);
        });

        assertEquals(errorMessage, exception.getMessage());
    }
}

