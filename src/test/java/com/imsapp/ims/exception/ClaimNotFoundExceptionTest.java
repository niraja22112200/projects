package com.imsapp.ims.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

 class ClaimNotFoundExceptionTest {
    @Test
    void testClaimNotFoundExceptionMessage() {
        String errorMessage = "Claim not found";
        ClaimNotFoundException exception = new ClaimNotFoundException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testClaimNotFoundExceptionThrown() {
        String errorMessage = "Claim not found";
        Exception exception = assertThrows(ClaimNotFoundException.class, () -> {
            throw new ClaimNotFoundException(errorMessage);
        });
        assertEquals(errorMessage, exception.getMessage());
    }

}
