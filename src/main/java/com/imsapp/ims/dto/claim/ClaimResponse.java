package com.imsapp.ims.dto.claim;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class ClaimResponse {
    private Long id;
    private Long policy;
    private String status;
    private Double amount;
    private LocalDate dateFiled;
}

