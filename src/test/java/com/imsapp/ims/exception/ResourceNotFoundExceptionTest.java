package com.imsapp.ims.exception;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

 class ResourceNotFoundExceptionTest {

    @Test
    void testResourceNotFoundExceptionMessage() {
        String errorMessage = "Resource not found";
        ResourceNotFoundException exception = new ResourceNotFoundException(errorMessage);
        assertEquals(errorMessage, exception.getMessage());
    }

    @Test
    void testResourceNotFoundExceptionThrown() {
        String errorMessage = "Resource not found";
        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            throw new ResourceNotFoundException(errorMessage);
        });
        assertEquals(errorMessage, exception.getMessage());
    }
}
