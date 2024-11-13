package com.acme.saga.sagamodel.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acme.saga.sagamodel.helpers.Utils;
import com.acme.saga.sagamodel.model.Applicant;
import com.acme.saga.sagamodel.model.ApplicantRepository;

@Service
public class ApplicantServiceImpl implements ApplicantService {


    @Autowired ApplicantRepository applicantRepository;    

    @Override
    public Applicant findApplicantById( Integer id ) {

        Optional<Applicant> theApplicant = applicantRepository.findById( id );

        if(theApplicant.isPresent()) {
            return theApplicant.get();
        }
        else
        {
            throw new RuntimeException("Applicant not found with id: " + id);
        }
    }


    @Override
    public Applicant saveApplicant( Applicant applicant ) {
        return applicantRepository.save( applicant );
    }

    @Override
    public List<Applicant> fetchApplicantList() {
        return Utils.toList(applicantRepository.findAll());
    }

    @Override
    public Applicant updateApplicant(Applicant applicant, Integer Id) {   

        Applicant oApplicant = applicantRepository.findById( Id ).get();

        if( Objects.nonNull( applicant.getLimit() )) {
            oApplicant.setLimit( applicant.getLimit() );
        }  

        if( Objects.nonNull( applicant.getApproved() )) {
            oApplicant.setApproved( applicant.getApproved() );
        }    

        if( Objects.nonNull( applicant.getLimitUpdateDate() )) {
            oApplicant.setLimitUpdateDate( applicant.getLimitUpdateDate() );
        }

        if( Objects.nonNull( applicant.getComment() )  && !"".equalsIgnoreCase( applicant.getComment() )) {
            oApplicant.setComment( applicant.getComment() );
        } 

        return applicantRepository.save( oApplicant );
    }

    @Override
    public void deleteApplicantById(Integer Id) {
        applicantRepository.deleteById( Id );
    }

}