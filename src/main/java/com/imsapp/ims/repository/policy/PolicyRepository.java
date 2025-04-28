package com.imsapp.ims.repository.policy;

import com.imsapp.ims.entity.policy.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRepository extends JpaRepository<Policy, Long> {
    Policy findByPolicyNumber(String policyNumber);
}

