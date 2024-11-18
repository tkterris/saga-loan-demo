package com.acme.saga.model;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Loan implements Serializable {

    private Integer Id;  
    private BigDecimal amount;   
    private Integer applicantId;
    private Boolean approved;
    private java.util.Date loanRequestDate;
    private java.util.Date loanCancelDate;
    private java.util.Date loanApprovalDate;
    private String comment;

}
