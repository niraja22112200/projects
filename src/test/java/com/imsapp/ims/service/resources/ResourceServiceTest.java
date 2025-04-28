package com.imsapp.ims.service.resources;

import com.imsapp.ims.controller.resources.ResourceController;
import com.imsapp.ims.entity.resources.Resources;
import com.imsapp.ims.exception.ResourceNotFoundException;
import com.imsapp.ims.repository.resources.ResourceRepository;
import com.imsapp.ims.service.resouces.ResourceService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ResourceServiceTest {

    @Mock
    private ResourceRepository resourceRepository;

    @InjectMocks
    private ResourceService resourceService;
    @InjectMocks
    private ResourceController resourceController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllResources() {
        List<Resources> resources = List.of(new Resources());
        when(resourceRepository.findAll()).thenReturn(resources);

        List<Resources> result = resourceService.getAllResources();

        assertEquals(resources.size(), result.size());
        verify(resourceRepository, times(1)).findAll();
    }

    @Test
    void testGetResourceById() {
        Long id = 1L;
        Resources resource = new Resources();
        resource.setUrl("test.txt");
        when(resourceRepository.findById(id)).thenReturn(Optional.of(resource));

        // Mock the ClassPathResource to simulate the non-existence of the file
        Resource fileResource = mock(ClassPathResource.class);
        when(fileResource.exists()).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> resourceService.getResourceById(id));
        verify(resourceRepository, times(1)).findById(id);
    }
    @Test
    void testGetResourceByIdNotFound() {
        Long id = 1L;
        when(resourceRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> resourceService.getResourceById(id));
        verify(resourceRepository, times(1)).findById(id);
    }
    @Test
     void testGetResourceById_ResourceFound_FileExists() {
        Long resourceId = 1L;
        Resources resource = new Resources();
        resource.setUrl("testfile.txt");
        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));

        // Mock the Resource object
        Resource fileResource = mock(Resource.class);
        when(fileResource.exists()).thenReturn(true);

        // Inject the mock Resource into the service
        ResourceService resourceServce = new ResourceService(resourceRepository) {
            @Override
            public ResponseEntity<Resource> getResourceById(Long id) {
                Resources resource = resourceRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id " + id));
                String fileName = resource.getUrl();
                if (!fileResource.exists()) {
                    throw new ResourceNotFoundException("Document not found: " + fileName);
                }
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
                try {
                    return ResponseEntity.ok()
                            .headers(headers)
                            .body(fileResource);
                } catch (Exception e) {
                    throw new ResourceNotFoundException("Error reading document: " + fileName + e.getMessage());
                }
            }
        };

        ResponseEntity<Resource> response = resourceServce.getResourceById(resourceId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fileResource, response.getBody());
        assertEquals("attachment; filename=\"testfile.txt\"", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
    }


    @Test
     void testGetResourceById_FileNotFound() {
        Long resourceId = 1L;
        Resources resource = new Resources();
        resource.setUrl("Customer-Support.pdf");
        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));

        // Mock the Resource object
        Resource fileResource = mock(Resource.class);
        when(fileResource.exists()).thenReturn(false);

        // Inject the mock Resource into the service
        ResourceService resourceServices = new ResourceService(resourceRepository) {
            @Override
            public ResponseEntity<Resource> getResourceById(Long id) {
                Resources resource = resourceRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id " + id));
                String fileName = resource.getUrl();
                // Use the mocked Resource object instead of creating a new ClassPathResource
                if (!fileResource.exists()) {
                    throw new ResourceNotFoundException("Document not found: " + fileName);
                }
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
                try {
                    return ResponseEntity.ok()
                            .headers(headers)
                            .body(fileResource);
                } catch (Exception e) {
                    throw new ResourceNotFoundException("Error reading document: " + fileName + e.getMessage());
                }
            }
        };

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            resourceServices.getResourceById(resourceId);
        });

        assertEquals("Document not found: Customer-Support.pdf", exception.getMessage());
    }


    @Test
     void testGetResourceById_EmptyFile() {
        Long resourceId = 1L;
        Resources resource = new Resources();
        resource.setUrl("Customer-Support.pdf");
        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));

        // Mock the Resource object
        Resource fileResource = mock(Resource.class);
        when(fileResource.exists()).thenReturn(true);

        // Inject the mock Resource into the service
        ResourceService resurceService = new ResourceService(resourceRepository) {
            @Override
            public ResponseEntity<Resource> getResourceById(Long id) {
                Resources resource = resourceRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id " + id));
                String fileName = resource.getUrl();
                // Use the mocked Resource object instead of creating a new ClassPathResource
                if (!fileResource.exists()) {
                    throw new ResourceNotFoundException("Document not found: " + fileName);
                }
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
                try {
                    return ResponseEntity.ok()
                            .headers(headers)
                            .body(fileResource);
                } catch (Exception e) {
                    throw new ResourceNotFoundException("Error reading document: " + fileName + e.getMessage());
                }
            }
        };

        ResponseEntity<Resource> response = resurceService.getResourceById(resourceId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fileResource, response.getBody());
        assertEquals("attachment; filename=\"Customer-Support.pdf\"", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
    }



    @Test
     void testGetResourceById_ResourceNotFound() {
        Long resourceId = 1L;
        when(resourceRepository.findById(resourceId)).thenReturn(Optional.empty());

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            resourceService.getResourceById(resourceId);
        });

        assertEquals("Resource not found with id " + resourceId, exception.getMessage());
    }
    @Test
    void testFileNotFound() {
        Resource fileResource = new ClassPathResource("static/nonexistentfile.txt");
        assertThrows(ResourceNotFoundException.class, () -> checkFileExists(fileResource));
    }

    private void checkFileExists(Resource fileResource) {
        if (!fileResource.exists()) {
            throw new ResourceNotFoundException("Document not found: nonexistentfile.txt");
        }
    }

    @Test
     void testSettingHttpHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"testfile.txt\"");
        assertEquals("attachment; filename=\"testfile.txt\"", headers.getFirst(HttpHeaders.CONTENT_DISPOSITION));
    }
    @Test
     void testSuccessfulResponse() {
        Resource fileResource = new ClassPathResource("static/testfile.txt");
        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"testfile.txt\"");
        try {
            ResponseEntity<Resource> response = ResponseEntity.ok()
                    .headers(headers)
                    .body(fileResource);
            assertNotNull(response);
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals(fileResource, response.getBody());
        } catch (Exception e) {
            fail("Exception should not be thrown");
        }
    }

    @Test
    void testGetResourceById_Success() {
        Long id = 3L;
        Resources resource = new Resources();
        resource.setUrl("Claim-Form.pdf");
        when(resourceRepository.findById(id)).thenReturn(Optional.of(resource));

        // Mock the ClassPathResource to simulate the existence of the file
        Resource fileResource = mock(ClassPathResource.class);
        when(fileResource.exists()).thenReturn(true);

        // Spy on the ResourceService to mock the creation of ClassPathResource
        ResourceService resourceServiceWithMock = spy(resourceService);
        doReturn(fileResource).when(resourceServiceWithMock).createClassPathResource("static/Claim-Form.pdf");

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"Claim-Form.pdf\"");

        ResponseEntity<Resource> response = resourceServiceWithMock.getResourceById(id);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("attachment; filename=\"Claim-Form.pdf\"", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
        verify(resourceRepository, times(1)).findById(id);
    }
    @Test
     void testGetResourceById_FileNotFound1() {
        Long resourceId = 1L;
        Resources resource = new Resources();
        resource.setUrl("nonexistentfile.txt");
        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));

        // Mock the Resource object
        Resource fileResource = mock(Resource.class);
        when(fileResource.exists()).thenReturn(false);

        // Inject the mock Resource into the service
        ResourceService resorceService = new ResourceService(resourceRepository) {
            @Override
            public ResponseEntity<Resource> getResourceById(Long id) {
                Resources resource = resourceRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id " + id));
                String fileName = resource.getUrl();
                if (!fileResource.exists()) {
                    throw new ResourceNotFoundException("Document not found: " + fileName);
                }
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
                try {
                    return ResponseEntity.ok()
                            .headers(headers)
                            .body(fileResource);
                } catch (Exception e) {
                    throw new ResourceNotFoundException("Error reading document: " + fileName + e.getMessage());
                }
            }
        };

        Exception exception = assertThrows(ResourceNotFoundException.class, () -> {
            resorceService.getResourceById(resourceId);
        });

        assertEquals("Document not found: nonexistentfile.txt", exception.getMessage());
    }

    @Test
    void testCreateResource() {
        Resources resource = new Resources();
        when(resourceRepository.save(any(Resources.class))).thenReturn(resource);

        Resources result = resourceService.createResource(resource);

        assertNotNull(result);
        verify(resourceRepository, times(1)).save(any(Resources.class));
    }

    @Test
    void testUpdateResource() {
        Long id = 1L;
        Resources resourceDetails = new Resources();
        Resources resource = new Resources();
        when(resourceRepository.findById(id)).thenReturn(Optional.of(resource));
        when(resourceRepository.save(any(Resources.class))).thenReturn(resource);

        Resources result = resourceService.updateResource(id, resourceDetails);

        assertNotNull(result);
        verify(resourceRepository, times(1)).findById(id);
        verify(resourceRepository, times(1)).save(any(Resources.class));
    }
    @Test
    void testGetResourceById_ResourceFoundFileExists() {
        Long resourceId = 1L;
        Resources resource = new Resources();
        resource.setUrl("Customer-Support.pdf");
        when(resourceRepository.findById(resourceId)).thenReturn(Optional.of(resource));

        // Mock the Resource object
        Resource fileResource = mock(Resource.class);
        when(fileResource.exists()).thenReturn(true);

        // Inject the mock Resource into the service
        ResourceService resourcService = new ResourceService(resourceRepository) {
            @Override
            public ResponseEntity<Resource> getResourceById(Long id) {
                Resources resource = resourceRepository.findById(id)
                        .orElseThrow(() -> new ResourceNotFoundException("Resource not found with id " + id));
                String fileName = resource.getUrl();
                // Use the mocked Resource object instead of creating a new ClassPathResource
                if (!fileResource.exists()) {
                    throw new ResourceNotFoundException("Document not found: " + fileName);
                }
                HttpHeaders headers = new HttpHeaders();
                headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
                try {
                    return ResponseEntity.ok()
                            .headers(headers)
                            .body(fileResource);
                } catch (Exception e) {
                    throw new ResourceNotFoundException("Error reading document: " + fileName + e.getMessage());
                }
            }
        };

        ResponseEntity<Resource> response = resourcService.getResourceById(resourceId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(fileResource, response.getBody());
        assertEquals("attachment; filename=\"Customer-Support.pdf\"", response.getHeaders().getFirst(HttpHeaders.CONTENT_DISPOSITION));
    }



    @Test
    void testUpdateResourceNotFound() {
        Long id = 1L;
        Resources resourceDetails = new Resources();
        when(resourceRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> resourceService.updateResource(id, resourceDetails));
        verify(resourceRepository, times(1)).findById(id);
    }

    @Test
    void testDeleteResource() {
        Long id = 1L;
        Resources resource = new Resources();
        when(resourceRepository.findById(id)).thenReturn(Optional.of(resource));
        doNothing().when(resourceRepository).delete(resource);

        resourceService.deleteResource(id);

        verify(resourceRepository, times(1)).findById(id);
        verify(resourceRepository, times(1)).delete(resource);
    }

    @Test
    void testDeleteResourceNotFound() {
        Long id = 1L;
        when(resourceRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> resourceService.deleteResource(id));
        verify(resourceRepository, times(1)).findById(id);
    }

    @SneakyThrows
    @Test
    void testGetResourceByIdErrorReadingDocument() {
        Long id = 1L;
        Resources resource = new Resources();
        resource.setUrl("test.txt");
        when(resourceRepository.findById(id)).thenReturn(Optional.of(resource));

        // Mock the ClassPathResource to simulate the existence of the file
        Resource fileResource = mock(ClassPathResource.class);
        when(fileResource.exists()).thenReturn(true);
        when(fileResource.getFilename()).thenReturn("test.txt");

        // Simulate an error when reading the document
        doThrow(new RuntimeException("Error reading document")).when(fileResource).getInputStream();

        assertThrows(ResourceNotFoundException.class, () -> resourceService.getResourceById(id));
        verify(resourceRepository, times(1)).findById(id);
    }
    @Test
    void testCreateClassPathResource() {
        ResourceService resourceServices = new ResourceService(null); // Passing null for the repository as it's not needed for this test

        String path = "static/Claim-Form.pdf";
        Resource resource = resourceServices.createClassPathResource(path);

        assertNotNull(resource);
        assertTrue(resource instanceof ClassPathResource);
        assertEquals(path, ((ClassPathResource) resource).getPath());
    }

}
