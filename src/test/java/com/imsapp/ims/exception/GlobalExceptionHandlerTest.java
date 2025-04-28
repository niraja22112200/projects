package com.imsapp.ims.exception;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;
    private WebRequest webRequest;
    MethodArgumentNotValidException exx;

    @BeforeEach
    void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
        webRequest = mock(WebRequest.class);
        when(webRequest.getDescription(false)).thenReturn("uri=/test");

        // Mocking MethodArgumentNotValidException
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError1 = new FieldError("policy", "policyNumber", "Policy number is mandatory");
        FieldError fieldError2 = new FieldError("policy", "policyType", "Policy type is mandatory");
        lenient().when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(fieldError1, fieldError2));

         exx = new MethodArgumentNotValidException(null, bindingResult);

        // Mocking WebRequest
        WebRequest request = mock(WebRequest.class);
        lenient().when(request.getDescription(false)).thenReturn("uri=/test");
    }

    @Test
    void testHandlePolicyNotFoundException() {
        PolicyNotFoundException ex = new PolicyNotFoundException("Policy not found");
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handlePolicyNotFoundException(ex, webRequest);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Policy not found", response.getBody().getMessage());
        assertEquals("uri=/test", response.getBody().getPath());
    }

    @Test
    void testHandleGlobalException() {
        Exception ex = new Exception("Internal server error");
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleGlobalException(ex, webRequest);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Internal server error", response.getBody().getMessage());
        assertEquals("uri=/test", response.getBody().getPath());
    }

    @Test
     void testHandleValidationExceptions() {
        // Creating instance of the class containing the exception handler
        globalExceptionHandler = new GlobalExceptionHandler();

        // Calling the method and asserting the response
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationExceptions(exx, webRequest);

        // Asserting the status
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        // Asserting the error message
        ErrorResponse errorResponse = response.getBody();
        assertEquals("Validation failed", errorResponse.getMessage());


        // Asserting the errors map
        Map<String, String> expectedErrors = new HashMap<>();
        expectedErrors.put("policyNumber", "Policy number is mandatory");
        expectedErrors.put("policyType", "Policy type is mandatory");
        assertEquals(expectedErrors, errorResponse.getErrors());

        // Additional assertions
        assertEquals(2, errorResponse.getErrors().size());
        assertEquals("Policy number is mandatory", errorResponse.getErrors().get("policyNumber"));
        assertEquals("Policy type is mandatory", errorResponse.getErrors().get("policyType"));
    }


    //@Test
    void testHandleValidationExceptionsWithMultipleErrors() {
        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        BindingResult bindingResult = mock(BindingResult.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(Arrays.asList(
                new FieldError("objectName", "field1", "defaultMessage1"),
                new FieldError("objectName", "field2", "defaultMessage2")
        ));

        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleValidationExceptions(ex, webRequest);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Validation failed", response.getBody().getMessage());
        assertEquals("uri=/test", response.getBody().getPath());
        assertEquals("defaultMessage1", response.getBody().getErrors().get("field1"));
        assertEquals("defaultMessage2", response.getBody().getErrors().get("field2"));
    }
    @Test
    void testHandleClaimNotFoundException() {
        ClaimNotFoundException ex = new ClaimNotFoundException("Claim not found");
        ResponseEntity<ErrorResponse> response = globalExceptionHandler.handleClaimNotFoundException(ex, webRequest);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Claim not found", response.getBody().getMessage());
        assertEquals("uri=/test", response.getBody().getPath());
    }

    @Test
    void testHandleResourceNotFoundException() {
        ResourceNotFoundException exo = new ResourceNotFoundException("Resource not found");
        ResponseEntity<Object> response = globalExceptionHandler.handleResourceNotFoundException(exo, webRequest);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        Map<String, Object> body = (Map<String, Object>) response.getBody();
        assertEquals("Resource not found", body.get("message"));
        assertEquals("uri=/test", body.get("path"));
        assertEquals(HttpStatus.NOT_FOUND.value(), body.get("status"));
    }
}


