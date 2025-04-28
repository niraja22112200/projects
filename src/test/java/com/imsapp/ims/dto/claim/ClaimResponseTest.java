package com.imsapp.ims.dto.claim;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ClaimResponseTest {

    @Test
    void testClaimResponseFields() {
        ClaimResponse claimResponse = new ClaimResponse();
        claimResponse.setId(1L);
        claimResponse.setPolicy(123L);
        claimResponse.setStatus("Pending");
        claimResponse.setAmount(1000.0);
        claimResponse.setDateFiled(LocalDate.now());

        assertEquals(1L, claimResponse.getId());
        assertEquals(123L, claimResponse.getPolicy());
        assertEquals("Pending", claimResponse.getStatus());
        assertEquals(1000.0, claimResponse.getAmount());
        assertEquals(LocalDate.now(), claimResponse.getDateFiled());
    }

    @Test
    void testClaimResponseNotNull() {
        ClaimResponse claimResponse = new ClaimResponse();
        claimResponse.setId(1L);
        claimResponse.setPolicy(123L);
        claimResponse.setStatus("Pending");
        claimResponse.setAmount(1000.0);
        claimResponse.setDateFiled(LocalDate.now());

        assertNotNull(claimResponse.getId());
        assertNotNull(claimResponse.getPolicy());
        assertNotNull(claimResponse.getStatus());
        assertNotNull(claimResponse.getAmount());
        assertNotNull(claimResponse.getDateFiled());
    }
}
