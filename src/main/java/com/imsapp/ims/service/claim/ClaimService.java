package com.imsapp.ims.service.claim;

import com.imsapp.ims.dto.claim.ClaimRequest;
import com.imsapp.ims.dto.claim.ClaimResponse;
import com.imsapp.ims.entity.claim.Claim;
import com.imsapp.ims.exception.ClaimNotFoundException;
import com.imsapp.ims.repository.claim.ClaimRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
public class ClaimService {


    private ClaimRepository claimRepository;

    @Autowired
    public ClaimService(ClaimRepository claimRepository) {
        this.claimRepository = claimRepository;
    }

    public Page<ClaimResponse> getAllClaims(Pageable pageable) {
        Page<Claim> claims = claimRepository.findAll(pageable);
        return claims.map(this::convertToResponse);
    }

    public Page<ClaimResponse> getClaimsByStatus(String status, Pageable pageable) {
        Page<Claim> claims = claimRepository.findByStatus(status, pageable);
        return claims.map(this::convertToResponse);
    }

    public ClaimResponse createClaim(ClaimRequest claimRequest) {
        Claim claim = new Claim();
        claim.setPolicy(claimRequest.getPolicy());
        claim.setStatus(claimRequest.getStatus());
        claim.setAmount(claimRequest.getAmount());
        claim.setDateFiled(claimRequest.getDateFiled());
        Claim savedClaim = claimRepository.save(claim);
        return convertToResponse(savedClaim);
    }

    public ClaimResponse updateClaim(Long id, ClaimRequest claimRequest) {
        Optional<Claim> optionalClaim = claimRepository.findById(id);
        if (optionalClaim.isPresent()) {
            Claim claim = optionalClaim.get();
            claim.setPolicy(claimRequest.getPolicy());
            claim.setStatus(claimRequest.getStatus());
            claim.setAmount(claimRequest.getAmount());
            claim.setDateFiled(claimRequest.getDateFiled());
            Claim updatedClaim = claimRepository.save(claim);
            return convertToResponse(updatedClaim);
        } else {
            throw new ClaimNotFoundException("Claim with ID " + id + " not found");
        }

    }

    public void deleteClaim(Long id) {

        Optional<Claim> optionalClaim = claimRepository.findById(id);
        if (optionalClaim.isPresent()) {
            claimRepository.deleteById(id);
        } else {
            throw new ClaimNotFoundException("Claim with ID " + id + " not found");
        }

    }

    private ClaimResponse convertToResponse(Claim claim) {
        ClaimResponse response = new ClaimResponse();
        response.setId(claim.getId());
        response.setPolicy(claim.getPolicy());
        response.setStatus(claim.getStatus());
        response.setAmount(claim.getAmount());
        response.setDateFiled(claim.getDateFiled());
        return response;
    }
}
