package com.acme.saga.sagamodel.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.dao.EmptyResultDataAccessException;
import com.acme.saga.sagamodel.helpers.Utils;
import com.acme.saga.sagamodel.model.ApplicantRepository;
import com.acme.saga.sagamodel.model.Loan;
import com.acme.saga.sagamodel.model.LoanRepository;
import com.acme.saga.sagamodel.dto.DeleteLoanResponseDTO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoanServiceImpl implements LoanService {

    @Autowired LoanRepository loanRepository;    

    @Override
    public Loan findLoanById( Integer Id ) {
        Optional<Loan> rLoan = loanRepository.findById( Id );

        if(rLoan.isPresent())
            return rLoan.get();
        else
            return null;
    }


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
    public ResponseEntity<DeleteLoanResponseDTO> deleteLoanById(Integer Id) {
        // setup response entity for return 
        HttpHeaders responseHeaders = new HttpHeaders();
        
        responseHeaders.add("acme-loan-id", Id.toString());

        DeleteLoanResponseDTO rDTO = DeleteLoanResponseDTO.builder()
                                                          .loanId(Id)
                                                          .build();
        ResponseEntity<DeleteLoanResponseDTO> rEntity = null;

        loanRepository.deleteById( Id );

        responseHeaders.add("acme-loan-delete", "success");
        rDTO.setComment("Loan with ID: " + Id.toString() + " deleted...");
        rDTO.setStatus(HttpStatus.OK);
        rEntity = new ResponseEntity<>(rDTO, responseHeaders, HttpStatus.OK);

        return rEntity;
    }

}