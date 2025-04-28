package com.imsapp.ims.controller.claim;
import com.imsapp.ims.dto.claim.ClaimRequest;
import com.imsapp.ims.dto.claim.ClaimResponse;
import com.imsapp.ims.service.claim.ClaimService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/ims/claims")
public class ClaimController {

    private ClaimService claimService;

    @Autowired
    public ClaimController(ClaimService claimService) {
        this.claimService = claimService;
    }
    @GetMapping("/all")
    public Page<ClaimResponse> getAllClaims(@RequestParam int page,
                                            @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);
        return claimService.getAllClaims(pageable);

    }
        @Operation(summary = "Get claims by status")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved claims"),
            @ApiResponse(responseCode = "400", description = "Invalid status parameter"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @GetMapping
    public ResponseEntity<Page<ClaimResponse>> getClaims(@RequestParam String status,
                                         @RequestParam int page,
                                         @RequestParam int size) {
        Pageable pageable = PageRequest.of(page, size);


            Optional<Page<ClaimResponse>> claims = Optional.ofNullable(claimService.getClaimsByStatus(status, pageable));
            return claims.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.ok(Page.empty()));


        }

    @Operation(summary = "Create a new claim")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created claim"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public ClaimResponse createClaim(@RequestBody ClaimRequest claimRequest) {
        return claimService.createClaim(claimRequest);
    }

    @Operation(summary = "Update an existing claim")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated claim"),
            @ApiResponse(responseCode = "404", description = "Claim not found"),
            @ApiResponse(responseCode = "400", description = "Invalid request body"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PutMapping("/{id}")
    public ClaimResponse updateClaim(@PathVariable Long id,
                                     @RequestBody ClaimRequest claimRequest) {
        return claimService.updateClaim(id, claimRequest);
    }

    @Operation(summary = "Delete a claim")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted claim"),
            @ApiResponse(responseCode = "404", description = "Claim not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @DeleteMapping("/{id}")
    public void deleteClaim(@PathVariable Long id) {
        claimService.deleteClaim(id);
    }

}
