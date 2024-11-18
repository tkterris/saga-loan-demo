package com.acme.saga.loan.service;

import java.util.List;
import com.acme.saga.loan.dto.CreateLoanResponseDTO;
import com.acme.saga.loan.dto.DeleteLoanResponseDTO;
import org.springframework.http.ResponseEntity;
import com.acme.saga.loan.model.Loan;


public interface LoanService {

    ResponseEntity<CreateLoanResponseDTO> createLoan( Loan loan );
    ResponseEntity<DeleteLoanResponseDTO> deleteLoan( Integer loanId );
    
}