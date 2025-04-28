package com.imsapp.ims.entity.payment;

import com.imsapp.ims.entity.policy.Policy;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Amount is mandatory")
    @Min(value = 0, message = "Amount must be positive")
    private Double amount;

    private LocalDate datePaid;

    @ManyToOne
    @JoinColumn(name = "policyNumber")
    private Policy policy;


}

