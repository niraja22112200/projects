package com.imsapp.ims.controller.policy;

import com.imsapp.ims.dto.policy.PolicyRequest;
import com.imsapp.ims.dto.policy.PolicyResponse;
import com.imsapp.ims.entity.policy.Policy;
import com.imsapp.ims.exception.PolicyNotFoundException;
import com.imsapp.ims.service.policy.PolicyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ims/policies")
public class PolicyController {

    private PolicyService policyService;
    private static final String POLICY_EXP = "Policy not found with id: " ;

    @Autowired
    public PolicyController(PolicyService policyService) {
        this.policyService = policyService;
    }
    @Operation(summary = "Get all policies", description = "Retrieve a list of all policies")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @GetMapping
    public List<PolicyResponse> getAllPolicies() {
        return policyService.getAllPolicies().stream().map(this::convertToResponse).toList();
    }

    @Operation(summary = "Get policy by ID", description = "Retrieve a policy by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved policy"),
            @ApiResponse(responseCode = "404", description = "Policy not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @GetMapping("/{id}")
    public ResponseEntity<PolicyResponse> getPolicyById(@PathVariable Long id) {
        Policy policy = policyService.getPolicyById(id).orElseThrow(() -> new PolicyNotFoundException(POLICY_EXP + id));
        return ResponseEntity.ok(convertToResponse(policy));
    }

    @Operation(summary = "Create a new policy", description = "Create a new policy with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created policy"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @PostMapping
    public PolicyResponse createPolicy( @Valid @RequestBody PolicyRequest policyRequest) {
        Policy policy = convertToEntity(policyRequest);
        return convertToResponse(policyService.savePolicy(policy));
    }
    @Operation(summary = "Update an existing policy", description = "Update the details of an existing policy")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated policy"),
            @ApiResponse(responseCode = "404", description = "Policy not found"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @PutMapping("/{id}")
    public ResponseEntity<PolicyResponse> updatePolicy(@PathVariable Long id, @Valid  @RequestBody PolicyRequest policyRequest) {
        Policy policy = policyService.getPolicyById(id).orElseThrow(() -> new PolicyNotFoundException(POLICY_EXP+ id));
        policy.setPolicyNumber(policyRequest.getPolicyNumber());
        policy.setPolicyType(policyRequest.getPolicyType());
        policy.setCoverageDetails(policyRequest.getCoverageDetails());
        policy.setContactInfo(policyRequest.getContactInfo());
        return ResponseEntity.ok(convertToResponse(policyService.savePolicy(policy)));

    }

    @Operation(summary = "Delete a policy", description = "Delete a policy by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted policy"),
            @ApiResponse(responseCode = "404", description = "Policy not found"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePolicy(@PathVariable Long id) {
        policyService.getPolicyById(id).orElseThrow(() -> new PolicyNotFoundException(POLICY_EXP + id));
        policyService.deletePolicy(id);
        return ResponseEntity.noContent().build();

    }
    private Policy convertToEntity(PolicyRequest policyRequest) {
        Policy policy = new Policy();
        policy.setPolicyNumber(policyRequest.getPolicyNumber());
        policy.setPolicyType(policyRequest.getPolicyType());
        policy.setCoverageDetails(policyRequest.getCoverageDetails());
        policy.setContactInfo(policyRequest.getContactInfo());
        policy.setTotalPremiumAmount(policyRequest.getTotalPremiumAmount());
        return policy;
    }

    private PolicyResponse convertToResponse(Policy policy) {
        PolicyResponse policyResponse = new PolicyResponse();
        policyResponse.setId(policy.getId());
        policyResponse.setPolicyNumber(policy.getPolicyNumber());
        policyResponse.setPolicyType(policy.getPolicyType());
        policyResponse.setCoverageDetails(policy.getCoverageDetails());
        policyResponse.setContactInfo(policy.getContactInfo());
        return policyResponse;
    }

}

