package com.imsapp.ims.entity.claim;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.time.LocalDate;


@Entity
@Table(name = "claims", indexes = @Index(name = "idx_status", columnList = "status"))

@Data
public class Claim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Policy is mandatory")
    private Long policy ;

    @NotBlank(message = "Status is mandatory")
    private String status;

    @NotNull(message = "Amount is mandatory")
    @Positive(message = "Amount must be positive")
    private Double amount;

    @NotNull(message = "Date filed is mandatory")
    private LocalDate dateFiled;

}

