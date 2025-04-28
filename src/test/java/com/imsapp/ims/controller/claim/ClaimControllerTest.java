package com.imsapp.ims.controller.claim;

import com.imsapp.ims.dto.claim.ClaimRequest;
import com.imsapp.ims.dto.claim.ClaimResponse;
import com.imsapp.ims.service.claim.ClaimService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Method;
import java.time.LocalDate;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
 class ClaimControllerTest {

    @Mock
    private ClaimService claimService;

    @InjectMocks
    private ClaimController claimController;

    private ClaimRequest claimRequest;
    private ClaimResponse claimResponse;
    private Page<ClaimResponse> claimResponsePage;

    @BeforeEach
    void setUp() {
        claimRequest = new ClaimRequest();
        claimRequest.setPolicy(123L);
        claimRequest.setStatus("Pending");
        claimRequest.setAmount(1000.0);
        claimRequest.setDateFiled(LocalDate.now());

        claimResponse = new ClaimResponse();
        claimResponse.setId(1L);
        claimResponse.setPolicy(123L);
        claimResponse.setStatus("Pending");
        claimResponse.setAmount(1000.0);
        claimResponse.setDateFiled(LocalDate.now());

        claimResponsePage = new PageImpl<>(Collections.singletonList(claimResponse));
    }

    @Test
    void testGetAllClaims() {
        Pageable pageable = PageRequest.of(0, 10);
        when(claimService.getAllClaims(pageable)).thenReturn(claimResponsePage);
        Page<ClaimResponse> response = claimController.getAllClaims(0, 10);
        assertEquals(1, response.getTotalElements());
        verify(claimService, times(1)).getAllClaims(pageable);
    }

    @Test
    void testGetClaims() {
        Pageable pageable = PageRequest.of(0, 10);
        when(claimService.getClaimsByStatus("Pending", pageable)).thenReturn(claimResponsePage);
        ResponseEntity<Page<ClaimResponse>> response = claimController.getClaims("Pending", 0, 10);

        assertEquals(1, response.getBody().getTotalElements());
        verify(claimService, times(1)).getClaimsByStatus("Pending", pageable);
    }
    @Test
    void testGetClaimsWithResults() throws Exception {
        String status = "approved";
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        Page<ClaimResponse> claimsPage = new PageImpl<>(Collections.singletonList(new ClaimResponse()));

        when(claimService.getClaimsByStatus(status, pageable)).thenReturn(claimsPage);

        Method method = ClaimController.class.getDeclaredMethod("getClaims", String.class, int.class, int.class);
        method.setAccessible(true);

        ResponseEntity<Page<ClaimResponse>> response = (ResponseEntity<Page<ClaimResponse>>) method.invoke(claimController, status, page, size);
        assertEquals(claimsPage, response.getBody());
    }

    @Test
    void testGetClaimsNoResults() throws Exception {
        String status = "approved";
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        when(claimService.getClaimsByStatus(status, pageable)).thenReturn(Page.empty());

        Method method = ClaimController.class.getDeclaredMethod("getClaims", String.class, int.class, int.class);
        method.setAccessible(true);

        ResponseEntity<Page<ClaimResponse>> response = (ResponseEntity<Page<ClaimResponse>>) method.invoke(claimController, status, page, size);
        assertEquals(Page.empty(), response.getBody());
    }

    @Test
    void testCreateClaim() {
        when(claimService.createClaim(any(ClaimRequest.class))).thenReturn(claimResponse);
        ClaimResponse response = claimController.createClaim(claimRequest);
        assertEquals("Pending", response.getStatus());
        verify(claimService, times(1)).createClaim(any(ClaimRequest.class));
    }

    @Test
    void testUpdateClaim() {
        when(claimService.updateClaim(eq(1L), any(ClaimRequest.class))).thenReturn(claimResponse);
        ClaimResponse response = claimController.updateClaim(1L, claimRequest);
        assertEquals("Pending", response.getStatus());
        verify(claimService, times(1)).updateClaim(eq(1L), any(ClaimRequest.class));
    }

    @Test
    void testDeleteClaim() {
        doNothing().when(claimService).deleteClaim(1L);
        claimController.deleteClaim(1L);
        verify(claimService, times(1)).deleteClaim(1L);
    }
    @Test
    void testGetClaims_Success() {
        String status = "approved";
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        ClaimResponse claimRespons = new ClaimResponse();
        Page<ClaimResponse> claimPage = new PageImpl<>(Collections.singletonList(claimRespons));
        when(claimService.getClaimsByStatus(status, pageable)).thenReturn(claimPage);

        ResponseEntity<Page<ClaimResponse>> response = claimController.getClaims(status, page, size);

        assertEquals(ResponseEntity.ok(claimPage), response);
        verify(claimService, times(1)).getClaimsByStatus(status, pageable);
    }

    @Test
    void testGetClaims_EmptyPage() {
        String status = "pending";
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        when(claimService.getClaimsByStatus(status, pageable)).thenReturn(Page.empty());

        ResponseEntity<Page<ClaimResponse>> response = claimController.getClaims(status, page, size);

        assertEquals(ResponseEntity.ok(Page.empty()), response);
        verify(claimService, times(1)).getClaimsByStatus(status, pageable);
    }

    @Test
    void testGetClaims_NullPage() {
        String status = "rejected";
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);

        when(claimService.getClaimsByStatus(status, pageable)).thenReturn(null);

        ResponseEntity<Page<ClaimResponse>> response = claimController.getClaims(status, page, size);

        assertEquals(ResponseEntity.ok(Page.empty()), response);
        verify(claimService, times(1)).getClaimsByStatus(status, pageable);
    }

}

