package com.acme.saga.service;

import org.springframework.http.ResponseEntity;
import com.acme.saga.model.Loan;
import com.acme.saga.dto.CreateLoanResponseDTO;
import com.acme.saga.exception.SagaInvocationException;


public interface CreateLoanService {
    
    ResponseEntity<CreateLoanResponseDTO> createLoan( Loan loan ) throws SagaInvocationException;

}
