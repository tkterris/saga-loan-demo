package com.acme.saga.loan.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.acme.saga.loan.model.Loan;
import com.acme.saga.loan.dto.CreateLoanResponseDTO;
import com.acme.saga.loan.dto.DeleteLoanResponseDTO;
import com.acme.saga.loan.service.LoanService;


@RestController
public class LoanController {

    @Autowired private LoanService loanService;

    @PostMapping(path = "/createloan", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateLoanResponseDTO> createLoan( @RequestBody Loan loan ) {
        return loanService.createLoan( loan );
    }


    @DeleteMapping( path="/deleteloan/{id}", produces = MediaType.APPLICATION_JSON_VALUE )
    public ResponseEntity<DeleteLoanResponseDTO> deleteLoan( @PathVariable( "id" ) Integer Id ) {
        return loanService.deleteLoan( Id );
    } 
    
}

