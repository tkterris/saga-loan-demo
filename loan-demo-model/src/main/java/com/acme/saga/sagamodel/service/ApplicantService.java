package com.acme.saga.sagamodel.service;

import java.util.List;
import com.acme.saga.sagamodel.model.Applicant;

public interface ApplicantService {

    Applicant saveApplicant( Applicant applicant );
    List<Applicant> fetchApplicantList();
    Applicant updateApplicant( Applicant applicant, Integer Id );
    void deleteApplicantById( Integer Id );

}
