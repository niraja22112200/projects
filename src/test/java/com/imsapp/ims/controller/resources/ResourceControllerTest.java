package com.imsapp.ims.controller.resources;

import com.imsapp.ims.config.DocumentType;
import com.imsapp.ims.dto.resources.ResourceRequest;
import com.imsapp.ims.dto.resources.ResourceResponse;
import com.imsapp.ims.entity.resources.Resources;
import com.imsapp.ims.service.resouces.ResourceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ResourceControllerTest {



    @Mock
    private ResourceService resourceService;

    @InjectMocks
    private ResourceController resourceController;

    private Resources resource;
    private ResourceRequest resourceRequest;
    private ResourceResponse resourceResponse;

    @BeforeEach
    void setUp() {
        resource = new Resources();
        resource.setId(1L);
        resource.setName("Test Resource");
        resource.setType(DocumentType.valueOf("POLICY_DOCUMENT"));
        resource.setUrl("http://test.com");

        resourceRequest = new ResourceRequest();
        resourceRequest.setName("Test Resource");
        resourceRequest.setType(DocumentType.valueOf("POLICY_DOCUMENT"));
        resourceRequest.setUrl("http://test.com");

        resourceResponse = new ResourceResponse();
        resourceResponse.setId(1L);
        resourceResponse.setName("Test Resource");
        resourceResponse.setType(DocumentType.valueOf("POLICY_DOCUMENT"));
        resourceResponse.setUrl("http://test.com");
    }

    @Test
    void testGetAllResources() {
        when(resourceService.getAllResources()).thenReturn(Arrays.asList(resource));
        List<ResourceResponse> responses = resourceController.getAllResources();
        assertEquals(1, responses.size());
        assertEquals("Test Resource", responses.get(0).getName());
        verify(resourceService, times(1)).getAllResources();
    }

    @Test
    void testGetResourceById() {
        when(resourceService.getResourceById(1L)).thenReturn(ResponseEntity.ok(mock(Resource.class)));
        ResponseEntity<Resource> response = resourceController.getResourceById(1L);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(resourceService, times(1)).getResourceById(1L);
    }

    @Test
    void testCreateResource() {
        when(resourceService.createResource(any(Resources.class))).thenReturn(resource);
        ResponseEntity<ResourceResponse> response = resourceController.createResource(resourceRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Resource", response.getBody().getName());
        verify(resourceService, times(1)).createResource(any(Resources.class));
    }

    @Test
    void testUpdateResource() {
        when(resourceService.updateResource(eq(1L), any(Resources.class))).thenReturn(resource);
        ResponseEntity<ResourceResponse> response = resourceController.updateResource(1L, resourceRequest);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test Resource", response.getBody().getName());
        verify(resourceService, times(1)).updateResource(eq(1L), any(Resources.class));
    }

    @Test
    void testDeleteResource() {
        doNothing().when(resourceService).deleteResource(1L);
        ResponseEntity<Void> response = resourceController.deleteResource(1L);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(resourceService, times(1)).deleteResource(1L);
    }
}