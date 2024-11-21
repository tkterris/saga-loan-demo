package com.acme.saga.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CreateLoanResponseDTO {
    
    private Integer loanId;    
    private BigDecimal requestAmount;
    private Integer applicantId;
    private Date loanCreationDate;
    private Boolean approved;
    private String comment;

}
