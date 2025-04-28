package com.imsapp.ims.entity.policy;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.imsapp.ims.entity.payment.Payment;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Entity
@Data
public class Policy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Policy number is mandatory")
    @Size(max = 20, message = "Policy number must be less than 20 characters")

    private String policyNumber;

    @NotBlank(message = "Policy type is mandatory")
    private String policyType;

    @NotBlank(message = "Coverage details are mandatory")
    private String coverageDetails;

    @NotBlank(message = "Contact information is mandatory")
    private String contactInfo;

    private Double totalPremiumAmount;

    @OneToMany(mappedBy = "policy")
    @JsonIgnore
    private List<Payment> payments;

}
