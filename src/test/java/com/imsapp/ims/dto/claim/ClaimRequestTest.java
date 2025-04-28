package com.imsapp.ims.dto.claim;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClaimRequestTest {

    @Test
    void testClaimRequestFields() {
        ClaimRequest claimRequest = new ClaimRequest();
        claimRequest.setPolicy(123L);
        claimRequest.setStatus("Pending");
        claimRequest.setAmount(1000.0);
        claimRequest.setDateFiled(LocalDate.now());

        assertEquals(123L, claimRequest.getPolicy());
        assertEquals("Pending", claimRequest.getStatus());
        assertEquals(1000.0, claimRequest.getAmount());
        assertEquals(LocalDate.now(), claimRequest.getDateFiled());
    }

    @Test
    void testClaimRequestNotNull() {
        ClaimRequest claimRequest = new ClaimRequest();
        claimRequest.setPolicy(123L);
        claimRequest.setStatus("Pending");
        claimRequest.setAmount(1000.0);
        claimRequest.setDateFiled(LocalDate.now());

        assertNotNull(claimRequest.getPolicy());
        assertNotNull(claimRequest.getStatus());
        assertNotNull(claimRequest.getAmount());
        assertNotNull(claimRequest.getDateFiled());
    }
}

