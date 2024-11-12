package com.acme.saga.sagamodel.service;

import java.util.List;
import com.acme.saga.sagamodel.model.Loan;

import com.acme.saga.sagamodel.model.Applicant;

public interface LoanService {

    Loan saveLoan( Loan loan );
    List<Loan> fetchLoanList();
    Loan updateLoan( Loan loan, Integer Id );
    void deleteLoanById( Integer Id );    
    
}
