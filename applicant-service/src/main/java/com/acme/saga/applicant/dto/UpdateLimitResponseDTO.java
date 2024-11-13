package com.acme.saga.applicant.dto;

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
public class UpdateLimitResponseDTO {
    
    private Integer applicantId;
    private Integer loanId;    
    private BigDecimal originalLimitAmount;
    private BigDecimal requestAmount;
    private BigDecimal remainingAmount;
    private Boolean approved;
    private String loanComment;
    private String applicantComment;

}
