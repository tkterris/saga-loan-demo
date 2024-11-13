package com.acme.saga.loandemo.dto;

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
public class LoanDTO {
    
    private Integer id;    
    private BigDecimal amount;
    private Integer applicantId;
    private Boolean approved;
    private Date loanRequestDate;
    private Date loanCancelDate;
    private Date loanApprovalDate;
    private String comment;

}
