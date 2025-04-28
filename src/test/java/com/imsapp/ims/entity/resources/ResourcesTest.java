package com.imsapp.ims.entity.resources;

import com.imsapp.ims.config.DocumentType;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ResourcesTest {

    private Validator validator;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testResourcesFields() {
        Resources resources = new Resources();
        resources.setId(1L);
        resources.setName("Test Resource");
        resources.setType(DocumentType.POLICY_DOCUMENT);
        resources.setUrl("http://test.com");

        assertEquals(1L, resources.getId());
        assertEquals("Test Resource", resources.getName());
        assertEquals(DocumentType.POLICY_DOCUMENT, resources.getType());
        assertEquals("http://test.com", resources.getUrl());
    }

    @Test
    void testResourcesValidation() {
        Resources resources = new Resources();
        resources.setName("Test Resource");
        resources.setType(DocumentType.POLICY_DOCUMENT);
        resources.setUrl("http://test.com");

        Set<ConstraintViolation<Resources>> violations = validator.validate(resources);
        assertTrue(violations.isEmpty());
    }

    @Test
    void testResourcesValidationWithNullFields() {
        Resources resources = new Resources();
        Set<ConstraintViolation<Resources>> violations = validator.validate(resources);
        assertEquals(0, violations.size());
    }

    @Test
    void testResourcesEqualsAndHashCode() {
        Resources resources1 = new Resources();
        resources1.setId(1L);
        resources1.setName("Test Resource");
        resources1.setType(DocumentType.POLICY_DOCUMENT);
        resources1.setUrl("http://test.com");

        Resources resources2 = new Resources();
        resources2.setId(1L);
        resources2.setName("Test Resource");
        resources2.setType(DocumentType.POLICY_DOCUMENT);
        resources2.setUrl("http://test.com");

        assertEquals(resources1, resources2);
        assertEquals(resources1.hashCode(), resources2.hashCode());
    }
}

