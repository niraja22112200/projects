package com.imsapp.ims.repository.claim;

import com.imsapp.ims.entity.claim.Claim;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ClaimRepository extends JpaRepository<Claim, Long> {

    @Query(value = "SELECT * FROM claims WHERE status = :status",
            countQuery = "SELECT count(*) FROM claims WHERE status = :status",
            nativeQuery = true)
    Page<Claim> findByStatus(String status, Pageable pageable);
}





