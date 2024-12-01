package com.acme.saga.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import com.acme.saga.model.Loan;
import com.acme.saga.dto.CreateLoanResponseDTO;
import com.acme.saga.service.CreateLoanService;

@RestController
public class CreateLoanController {

    @Autowired
    private CreateLoanService createLoanService;

    @PostMapping(path = "/createloan", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<CreateLoanResponseDTO> createLoan(@RequestBody Loan loan) {
        return createLoanService.createLoan(loan);
    }

}
