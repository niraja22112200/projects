package com.imsapp.ims.controller.resources;

import com.imsapp.ims.dto.resources.ResourceRequest;
import com.imsapp.ims.dto.resources.ResourceResponse;
import com.imsapp.ims.entity.resources.Resources;
import com.imsapp.ims.service.resouces.ResourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/ims/resources")
public class ResourceController {


    private ResourceService resourceService;

    @Autowired
    public ResourceController(ResourceService resourceService) {
        this.resourceService = resourceService;
    }

    @Operation(summary = "Get all resources")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the resources",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResourceResponse.class)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping
    public List<ResourceResponse> getAllResources() {
        return resourceService.getAllResources().stream()
                .map(this::convertToResponse)
                .toList();
    }

    @Operation(summary = "Get a resource by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found the resource",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResourceResponse.class)) }),
            @ApiResponse(responseCode = "404", description = "Resource not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<Resource> getResourceById(@PathVariable Long id) {
        return resourceService.getResourceById(id);
    }



    @Operation(summary = "Create a new resource")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Resource created",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResourceResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PostMapping
    public ResponseEntity<ResourceResponse> createResource(@RequestBody ResourceRequest resourceRequest) {
        Resources resource = convertToEntity(resourceRequest);
        Resources createdResource = resourceService.createResource(resource);
        return ResponseEntity.ok(convertToResponse(createdResource));
    }

    @Operation(summary = "Update an existing resource")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Resource updated",
                    content = { @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ResourceResponse.class)) }),
            @ApiResponse(responseCode = "400", description = "Invalid input",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Resource not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @PutMapping("/{id}")
    public ResponseEntity<ResourceResponse> updateResource(@PathVariable Long id, @RequestBody ResourceRequest resourceRequest) {
        Resources resourceDetails = convertToEntity(resourceRequest);
        Resources updatedResource = resourceService.updateResource(id, resourceDetails);
        return ResponseEntity.ok(convertToResponse(updatedResource));
    }

    @Operation(summary = "Delete a resource")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Resource deleted",
                    content = @Content),
            @ApiResponse(responseCode = "404", description = "Resource not found",
                    content = @Content),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable Long id) {
        resourceService.deleteResource(id);
        return ResponseEntity.noContent().build();
    }

    private ResourceResponse convertToResponse(Resources resource) {
        ResourceResponse resourceResponse = new ResourceResponse();
        resourceResponse.setId(resource.getId());
        resourceResponse.setName(resource.getName());
        resourceResponse.setType(resource.getType());
        resourceResponse.setUrl(resource.getUrl());
        return resourceResponse;
    }

    private Resources convertToEntity(ResourceRequest resourceRequest) {
        Resources resource = new Resources();
        resource.setName(resourceRequest.getName());
        resource.setType(resourceRequest.getType());
        resource.setUrl(resourceRequest.getUrl());
        return resource;
    }
}

