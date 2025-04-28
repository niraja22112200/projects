package com.imsapp.ims.dto.resources;

import com.imsapp.ims.config.DocumentType;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

 class ResourceResponseTest {

    @Test
    void testResourceResponseFields() {
        ResourceResponse resourceResponse = new ResourceResponse();
        resourceResponse.setId(1L);
        resourceResponse.setName("Test Resource");
        resourceResponse.setType(DocumentType.POLICY_DOCUMENT);
        resourceResponse.setUrl("http://test.com");

        assertEquals(1L, resourceResponse.getId());
        assertEquals("Test Resource", resourceResponse.getName());
        assertEquals(DocumentType.POLICY_DOCUMENT, resourceResponse.getType());
        assertEquals("http://test.com", resourceResponse.getUrl());
    }

    @Test
    void testResourceResponseNotNull() {
        ResourceResponse resourceResponse = new ResourceResponse();
        resourceResponse.setId(1L);
        resourceResponse.setName("Test Resource");
        resourceResponse.setType(DocumentType.POLICY_DOCUMENT);
        resourceResponse.setUrl("http://test.com");

        assertNotNull(resourceResponse.getId());
        assertNotNull(resourceResponse.getName());
        assertNotNull(resourceResponse.getType());
        assertNotNull(resourceResponse.getUrl());
    }
}

