package com.acme.saga.sagamodel.service;

import java.util.List;
import com.acme.saga.sagamodel.model.Loan;
import org.springframework.http.ResponseEntity;
import com.acme.saga.sagamodel.model.Applicant;
import com.acme.saga.sagamodel.dto.DeleteLoanResponseDTO;


public interface LoanService {

    Loan saveLoan( Loan loan );
    List<Loan> fetchLoanList();
    Loan updateLoan( Loan loan, Integer Id );
    ResponseEntity<DeleteLoanResponseDTO> deleteLoanById( Integer Id );    
    Loan findLoanById( Integer Id );
}
