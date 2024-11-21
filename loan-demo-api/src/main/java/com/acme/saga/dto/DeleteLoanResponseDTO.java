package com.acme.saga.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeleteLoanResponseDTO {
    
    private Integer loanId;    
    private Date loanDeletionDate;
    private HttpStatus status;
    private String comment;

}
