package com.imsapp.ims.service.resouces;


import com.imsapp.ims.entity.resources.Resources;
import com.imsapp.ims.exception.ResourceNotFoundException;
import com.imsapp.ims.repository.resources.ResourceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResourceService {


    private ResourceRepository resourceRepository;
      static final String RESOURCES_NOT_FOUND="Resource not found with id ";

    @Autowired
    public ResourceService(ResourceRepository resourceRepository) {
        this.resourceRepository = resourceRepository;
    }

    public List<Resources> getAllResources() {
        return resourceRepository.findAll();
    }

    public ResponseEntity<Resource> getResourceById(Long id) {
        Resources resource = resourceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(RESOURCES_NOT_FOUND + id));

        // Assuming the resource URL points to the file name in the static folder
        String fileName = resource.getUrl();
        Resource fileResource = new ClassPathResource("static/" + fileName);

        if (!fileResource.exists()) {
            throw new ResourceNotFoundException("Document not found: " + fileName);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"");
           return ResponseEntity.ok().headers(headers).body(fileResource);

    }




    public Resources createResource(Resources resource) {
        return resourceRepository.save(resource);
    }

    public Resources updateResource(Long id, Resources resourceDetails) {
        Resources resource = resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RESOURCES_NOT_FOUND+ id));
        resource.setName(resourceDetails.getName());
        resource.setType(resourceDetails.getType());
        resource.setUrl(resourceDetails.getUrl());
        return resourceRepository.save(resource);
    }

    public void deleteResource(Long id) {
        Resources resource = resourceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(RESOURCES_NOT_FOUND+ id));
        resourceRepository.delete(resource);
    }
    public Resource createClassPathResource(String path) {
        return new ClassPathResource(path);
    }

}


