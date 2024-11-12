package com.acme.saga.sagamodel.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acme.saga.sagamodel.helpers.Utils;
import com.acme.saga.sagamodel.model.ApplicantRepository;
import com.acme.saga.sagamodel.model.Loan;
import com.acme.saga.sagamodel.model.LoanRepository;


@Service
public class LoanServiceImpl implements LoanService {

    @Autowired LoanRepository loanRepository;    

    @Override
    public Loan saveLoan( Loan loan ) {
        return loanRepository.save( loan );
    }


    @Override
    public List<Loan> fetchLoanList() {
        return Utils.toList(loanRepository.findAll());
    }


    @Override
    public Loan updateLoan(Loan loan, Integer Id) {   

        Loan oLoan = loanRepository.findById( Id ).get();

        if( Objects.nonNull( loan.getAmount() )) {
            oLoan.setAmount( loan.getAmount() );
        }         

        if( Objects.nonNull( loan.getApplicantId() )) {
            oLoan.setApplicantId( loan.getApplicantId() );
        }    

        if( Objects.nonNull( loan.getApproved() )) {
            oLoan.setApproved( loan.getApproved() );
        }            

        if( Objects.nonNull( loan.getLoanRequestDate() )) {
            oLoan.setLoanRequestDate( loan.getLoanRequestDate() );
        }

        if( Objects.nonNull( loan.getLoanCancelDate() )) {
            oLoan.setLoanCancelDate( loan.getLoanCancelDate() );
        }

        if( Objects.nonNull( loan.getLoanApprovalDate() )) {
            oLoan.setLoanApprovalDate( loan.getLoanApprovalDate() );
        }
    
        if( Objects.nonNull( loan.getComment())  && !"".equalsIgnoreCase( loan.getComment() )) {
            oLoan.setComment( loan.getComment() );
        }         

        return loanRepository.save( oLoan );

    }


    @Override
    public void deleteLoanById(Integer Id) {
        loanRepository.deleteById( Id );
    }

}