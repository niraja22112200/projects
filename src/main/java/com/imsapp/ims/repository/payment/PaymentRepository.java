package com.imsapp.ims.repository.payment;

import com.imsapp.ims.entity.payment.Payment;
import com.imsapp.ims.entity.policy.Policy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    List<Payment> findByPolicy(Policy policy);

}

