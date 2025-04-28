package com.imsapp.ims.dto.resources;

import com.imsapp.ims.config.DocumentType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

 class ResourceRequestTest {

    @Test
    void testResourceRequestFields() {
        ResourceRequest resourceRequest = new ResourceRequest();
        resourceRequest.setName("Test Resource");
        resourceRequest.setType(DocumentType.POLICY_DOCUMENT);
        resourceRequest.setUrl("http://test.com");

        assertEquals("Test Resource", resourceRequest.getName());
        assertEquals(DocumentType.POLICY_DOCUMENT, resourceRequest.getType());
        assertEquals("http://test.com", resourceRequest.getUrl());
    }

    @Test
    void testResourceRequestNotNull() {
        ResourceRequest resourceRequest = new ResourceRequest();
        resourceRequest.setName("Test Resource");
        resourceRequest.setType(DocumentType.POLICY_DOCUMENT);
        resourceRequest.setUrl("http://test.com");

        assertNotNull(resourceRequest.getName());
        assertNotNull(resourceRequest.getType());
        assertNotNull(resourceRequest.getUrl());
    }
}
