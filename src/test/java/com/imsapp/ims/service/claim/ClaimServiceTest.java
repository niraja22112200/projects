package com.imsapp.ims.service.claim;

import com.imsapp.ims.dto.claim.ClaimRequest;
import com.imsapp.ims.dto.claim.ClaimResponse;
import com.imsapp.ims.entity.claim.Claim;
import com.imsapp.ims.exception.ClaimNotFoundException;
import com.imsapp.ims.repository.claim.ClaimRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;


 class ClaimServiceTest{
    @Mock
private ClaimRepository claimRepository;

@InjectMocks
private ClaimService claimService;

@BeforeEach
void setUp() {
    MockitoAnnotations.openMocks(this);
}

@Test
void testGetAllClaims() {
    Pageable pageable = PageRequest.of(0, 10);
    Claim claim = new Claim();
    Page<Claim> claims = new PageImpl<>(List.of(claim));
    when(claimRepository.findAll(pageable)).thenReturn(claims);

    Page<ClaimResponse> response = claimService.getAllClaims(pageable);

    assertEquals(1, response.getTotalElements());
    verify(claimRepository, times(1)).findAll(pageable);
}

@Test
void testGetClaimsByStatus() {
    Pageable pageable = PageRequest.of(0, 10);
    String status = "Pending";
    Claim claim = new Claim();
    Page<Claim> claims = new PageImpl<>(List.of(claim));
    when(claimRepository.findByStatus(status, pageable)).thenReturn(claims);

    Page<ClaimResponse> response = claimService.getClaimsByStatus(status, pageable);

    assertEquals(1, response.getTotalElements());
    verify(claimRepository, times(1)).findByStatus(status, pageable);
}

@Test
void testCreateClaim() {
    ClaimRequest claimRequest = new ClaimRequest();
    claimRequest.setPolicy(Long.valueOf("234567"));
    claimRequest.setStatus("Pending");
    claimRequest.setAmount(1000.0);
    claimRequest.setDateFiled(LocalDate.parse("2023-03-27"));

    Claim claim = new Claim();
    when(claimRepository.save(any(Claim.class))).thenReturn(claim);

    ClaimResponse response = claimService.createClaim(claimRequest);

    assertNotNull(response);
    verify(claimRepository, times(1)).save(any(Claim.class));
}

@Test
void testUpdateClaim() {
    Long id = 1L;
    ClaimRequest claimRequest = new ClaimRequest();
    claimRequest.setPolicy(Long.valueOf("76776123"));
    claimRequest.setStatus("Approved");
    claimRequest.setAmount(1500.0);
    claimRequest.setDateFiled(LocalDate.parse("2023-03-27"));

    Claim claim = new Claim();
    when(claimRepository.findById(id)).thenReturn(Optional.of(claim));
    when(claimRepository.save(any(Claim.class))).thenReturn(claim);

    ClaimResponse response = claimService.updateClaim(id, claimRequest);

    assertNotNull(response);
    verify(claimRepository, times(1)).findById(id);
    verify(claimRepository, times(1)).save(any(Claim.class));
}

@Test
void testUpdateClaimNotFound() {
    Long id = 1L;
    ClaimRequest claimRequest = new ClaimRequest();
    when(claimRepository.findById(id)).thenReturn(Optional.empty());

    assertThrows(ClaimNotFoundException.class, () -> claimService.updateClaim(id, claimRequest));
    verify(claimRepository, times(1)).findById(id);
}

@Test
void testDeleteClaim() {
    Long id = 1L;
    Claim claim = new Claim();
    when(claimRepository.findById(id)).thenReturn(Optional.of(claim));
    doNothing().when(claimRepository).deleteById(id);

    claimService.deleteClaim(id);

    verify(claimRepository, times(1)).findById(id);
    verify(claimRepository, times(1)).deleteById(id);
}

@Test
void testDeleteClaimNotFound() {
    Long id = 1L;
    when(claimRepository.findById(id)).thenReturn(Optional.empty());

    assertThrows(ClaimNotFoundException.class, () -> claimService.deleteClaim(id));
    verify(claimRepository, times(1)).findById(id);
}
}

