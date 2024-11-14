package com.acme.saga.applicant.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.acme.saga.applicant.model.Loan;
import com.acme.saga.applicant.dto.UpdateLimitResponseDTO;
import com.acme.saga.applicant.service.ApplicantService;


@RestController
public class ApplicantController {

    @Autowired private ApplicantService applicantService;

    @PutMapping(path = "/updateloanlimit", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UpdateLimitResponseDTO> updateLoanLimit( @RequestBody Loan loan ) {
        return applicantService.updateLoanLimit( loan );
    }
    
}