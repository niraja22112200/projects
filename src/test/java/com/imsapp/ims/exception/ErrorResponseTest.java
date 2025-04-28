package com.imsapp.ims.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ErrorResponseTest {

    private ErrorResponse errorResponse;

    private HttpStatus status;
    private String message;
    private String path;
    private Map<String, String> errors;

    @BeforeEach
    void setUp() {

        status = HttpStatus.BAD_REQUEST;
        message = "Validation failed";
        path = "/api/policies";
        errors = new HashMap<>();
        errors.put("policyNumber", "must not be blank");

        errorResponse = new ErrorResponse(status, message, path);
        errorResponse.setErrors(errors);
    }

    @Test
    void testErrorResponseInitialization() {
        assertNotNull(errorResponse);
        assertEquals(status, errorResponse.getStatus());
        assertEquals(message, errorResponse.getMessage());
        assertEquals(path, errorResponse.getPath());
        assertNotNull(errorResponse.getTimestamp());
    }

    @Test
    void testGettersAndSetters() {
        errorResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        errorResponse.setMessage("Internal server error");
        errorResponse.setPath("/api/errors");
        errorResponse.setErrors(null);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, errorResponse.getStatus());
        assertEquals("Internal server error", errorResponse.getMessage());
        assertEquals("/api/errors", errorResponse.getPath());
        assertEquals(null, errorResponse.getErrors());
    }

    @Test
    void testErrors() {
        assertEquals(errors, errorResponse.getErrors());
        assertEquals("must not be blank", errorResponse.getErrors().get("policyNumber"));
    }
}
