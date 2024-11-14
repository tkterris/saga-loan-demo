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

import com.acme.saga.sagamodel.model.Applicant;
import com.acme.saga.sagamodel.service.ApplicantService;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;


@RestController
public class ApplicantController {

    @Autowired private ApplicantService applicantService;

    @GetMapping("/applicant/{id}")
    public Applicant fetchApplicantById( @PathVariable( "id" ) Integer id ) {
        return applicantService.findApplicantById( id );
    }


    @PostMapping(path = "/applicant", produces = MediaType.APPLICATION_JSON_VALUE)
    public Applicant saveApplicant( @Validated @RequestBody Applicant applicant )
    {
        return applicantService.saveApplicant( applicant );
    }

    @GetMapping( path = "/applicant" )
    public List<Applicant> fetchApplicantList() {
        return applicantService.fetchApplicantList();
    }

    @PutMapping("/applicant/{id}")
    public Applicant updateApplicant( @PathVariable( "id" ) Integer Id, 
                                @RequestBody Applicant applicant ) {       
        return applicantService.updateApplicant(applicant, Id);
    }

    @DeleteMapping( "/applicant/{id}" )
    public String deleteApplicant( @PathVariable( "id" ) Integer Id ) {
        applicantService.deleteApplicantById( Id );
        return "Deleted Successfully";
    } 
    
}