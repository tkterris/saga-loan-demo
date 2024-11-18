package com.acme.saga.sagamodel.controller;


import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;

import com.acme.saga.sagamodel.model.Loan;
import com.acme.saga.sagamodel.service.LoanService;
import com.acme.saga.sagamodel.dto.DeleteLoanResponseDTO;

import com.acme.saga.sagamodel.model.Applicant;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class LoanController {

    @Autowired private LoanService loanService;


    @PostMapping(path = "/loan", produces = MediaType.APPLICATION_JSON_VALUE)
    public Loan saveLoan( @Validated @RequestBody Loan loan )
    {
        return loanService.saveLoan( loan );
    }

    @GetMapping( path = "/loan" )
    public List<Loan> fetchLoanList() {
        return loanService.fetchLoanList();
    }

    @PutMapping("/loan/{id}")
    public Loan updateLoan( @PathVariable( "id" ) Integer Id, 
                                @RequestBody Loan loan ) {       
        return loanService.updateLoan(loan, Id);
    }

    @DeleteMapping( "/loan/{id}" )
    public ResponseEntity<DeleteLoanResponseDTO> deleteLoan( @PathVariable( "id" ) Integer Id ) {
        return loanService.deleteLoanById( Id );
    } 

}