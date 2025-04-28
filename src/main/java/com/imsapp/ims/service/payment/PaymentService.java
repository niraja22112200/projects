package com.imsapp.ims.service.payment;

import com.imsapp.ims.entity.payment.Payment;
import com.imsapp.ims.entity.policy.Policy;
import com.imsapp.ims.exception.PaymentProcessingException;
import com.imsapp.ims.exception.PolicyNotFoundException;
import com.imsapp.ims.repository.payment.PaymentRepository;
import com.imsapp.ims.repository.policy.PolicyRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class PaymentService {


    private PaymentRepository paymentRepository;


     PolicyRepository policyRepository;
    public static final String POLICY_NOT_FOUND  ="Policy not found for policy number: ";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public PaymentService(PaymentRepository paymentRepository, PolicyRepository policyRepository) {
        this.paymentRepository = paymentRepository;
        this.policyRepository = policyRepository;
    }

    @Transactional
    public Payment payPremium(String policyNumber, Double amount) {
        try{
        Policy policy = policyRepository.findByPolicyNumber(policyNumber);
        if (policy == null) {
            throw new PolicyNotFoundException(POLICY_NOT_FOUND + policyNumber);
        }

        Payment payment = new Payment();
        payment.setPolicy(policy);
        payment.setAmount(amount);
        payment.setDatePaid(LocalDate.now());

        return paymentRepository.save(payment);


        } catch (PolicyNotFoundException e) {
            throw e;
        } catch (Exception e) {
            throw new PaymentProcessingException("An error occurred while processing the payment", e.getMessage());
        }

    }
    public Double checkBalancePremium(String policyNumber) {
        Policy policy = policyRepository.findByPolicyNumber(policyNumber);
        if (policy == null) {
            throw new PolicyNotFoundException(POLICY_NOT_FOUND+ policyNumber);
        }

        List<Payment> payments = paymentRepository.findByPolicy(policy);
        Double totalPaid = payments.stream().mapToDouble(Payment::getAmount).sum();

        return policy.getTotalPremiumAmount() - totalPaid;
    }

    private void validateAmount(Policy policy, Double amount) {
        if (amount == null || amount <= 0) {
            throw new IllegalArgumentException("Amount must be positive and non-null");
        }

        List<Payment> payments = paymentRepository.findByPolicy(policy);
        Double totalPaid = payments.stream().mapToDouble(Payment::getAmount).sum();

        if (totalPaid + amount > policy.getTotalPremiumAmount()) {
            throw new IllegalArgumentException("Paid amount exceeds the total policy amount");
        }
    }
    public List<Payment> getPaymentsByPolicy(String policyNumber) {
        Policy policy = policyRepository.findByPolicyNumber(policyNumber);
        if (policy == null) {
            throw new PolicyNotFoundException(POLICY_NOT_FOUND + policyNumber);
        }
        return paymentRepository.findByPolicy(policy);
    }
    @Transactional
    public Payment updatePayment(Long paymentId, Double newAmount) {
        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new PaymentProcessingException("Payment not found for ID: " + paymentId,"Not valid payment"));

        validateAmount(payment.getPolicy(), newAmount);
        payment.setAmount(newAmount);
        return paymentRepository.save(payment);
    }
    public Double getTotalPaidAmount(String policyNumber) {
        Policy policy = policyRepository.findByPolicyNumber(policyNumber);
        if (policy == null) {
            throw new PolicyNotFoundException(POLICY_NOT_FOUND + policyNumber);
        }
        List<Payment> payments = paymentRepository.findByPolicy(policy);
        return payments.stream().mapToDouble(Payment::getAmount).sum();
    }
    public boolean isPolicyFullyPaid(String policyNumber) {
        Policy policy = policyRepository.findByPolicyNumber(policyNumber);
        if (policy == null) {
            throw new PolicyNotFoundException(POLICY_NOT_FOUND + policyNumber);
        }
        Double totalPaid = getTotalPaidAmount(policyNumber);
        return totalPaid >= policy.getTotalPremiumAmount();
    }



}

