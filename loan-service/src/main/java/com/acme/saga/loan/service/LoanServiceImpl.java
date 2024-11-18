package com.acme.saga.loan.service;

import java.util.List;
import java.util.Objects;
import java.time.Instant;
import java.util.Date;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import com.acme.saga.loan.helpers.Utils;
import com.acme.saga.loan.model.Applicant;
import com.acme.saga.loan.model.Loan;
import com.acme.saga.loan.dto.CreateLoanResponseDTO;
import com.acme.saga.loan.dto.DeleteLoanResponseDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.beans.factory.annotation.Value;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class LoanServiceImpl implements LoanService {

    private RestTemplate restTemplate;

    @Value("${loan.create.endpoint}")
    private String loanCreateEndpointURI;

    @Value("${loan.delete.endpoint}")
    private String loanDeleteEndpointURI;

    @Override
    /**
     * 
     * @param loan
     * @return
     * @throws ResourceNotFoundException
     */
    public ResponseEntity<CreateLoanResponseDTO> createLoan( Loan loan ) 
        throws EntityCreateException {

            restTemplate = new RestTemplate();

            ResponseEntity<Loan> lResponse = null;
            CreateLoanResponseDTO createLoanResponse = null;

            try {
                lResponse = createLoanEntity( loan );               
            }
            catch(RuntimeException e) {
                log.error("Unable to create Loan with ID: " + loan.getId().toString());
                throw new EntityCreateException("Unable to create loan with ID: " + loan.getId().toString());
            }

        // Handle the response
        if (lResponse.getStatusCode().is2xxSuccessful()) {
            log.info("Loan with ID: " + loan.getId().toString() + " added...");
        } else {
            log.error("Create on Loan entity failed, loan ID: " + loan.getId().toString());
            throw new EntityCreateException("Create Loan failed for Loan ID: " + 
                                            loan.getId().toString());
        }               

        // setup response entity for return 
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("acme-loan-create", "success");
        responseHeaders.add("acme-loan-id", loan.getId().toString());

        createLoanResponse = CreateLoanResponseDTO.builder()
                             .loanId(loan.getId())
                             .requestAmount(loan.getAmount())
                             .applicantId(loan.getApplicantId())
                             .loanCreationDate(Date.from(Instant.now()))
                             .comment(loan.getComment() + " - successfully added...")
                             .build();

        ResponseEntity<CreateLoanResponseDTO> cResponse = new ResponseEntity<>(createLoanResponse, responseHeaders, HttpStatus.OK);

        return cResponse;

    }

    @Override
    public ResponseEntity<DeleteLoanResponseDTO> deleteLoan( Integer loanId ) 
        throws EntityNotFoundException {

            restTemplate = new RestTemplate();

            ResponseEntity<DeleteLoanResponseDTO> lResponse = null;
            DeleteLoanResponseDTO deleteLoanResponse = null;

            try {
                lResponse = deleteLoanEntity( loanId );               
            }
            catch(RuntimeException e) {
                log.error("Unable to delete Loan with ID: " + loanId.toString());
                throw new EntityNotFoundException("Unable to delete loan with ID: " + loanId.toString());
            }

            // Handle the response
            if (lResponse.getStatusCode().is2xxSuccessful()) {
                log.info("Loan with ID: " + loanId.toString() + " deleted...");
            }
            else if (lResponse.getStatusCode().equals(HttpStatus.NOT_FOUND)) {
                return lResponse;
            }
            else {
                log.error("Deletion of Loan entity failed, loan ID: " + loanId.toString());
                throw new EntityNotFoundException("Delete Loan failed for Loan ID: " + 
                                                loanId.toString());
            }               

            // setup response entity for return 
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("acme-loan-delete", "success");
            responseHeaders.add("acme-loan-id", loanId.toString());

            deleteLoanResponse = DeleteLoanResponseDTO.builder()
                                .loanId(loanId)
                                .loanDeletionDate(Date.from(Instant.now()))
                                .comment("Loan with ID: " + loanId.toString() + " - successfully deleted...")
                                .build();

            ResponseEntity<DeleteLoanResponseDTO> dResponse = new ResponseEntity<>(deleteLoanResponse, responseHeaders, HttpStatus.OK);

            return dResponse;

    }

    public ResponseEntity<Loan> createLoanEntity( Loan loan ) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Loan> requestEntity = new HttpEntity<>(loan, headers);

        ResponseEntity<Loan> response = restTemplate.exchange(
            loanCreateEndpointURI, // CRUD update API endpoint
            HttpMethod.POST,
            requestEntity,
            Loan.class
        );        

        if(Objects.nonNull(response))
            log.info("createLoan: successful creation of loan with ID: " + loan.getId().toString());

        return response;

    }

    public ResponseEntity<DeleteLoanResponseDTO> deleteLoanEntity( Integer loanId ) {
     
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        //HttpEntity<Loan> requestEntity = new HttpEntity<>(new Loan(), headers);      
        ResponseEntity<DeleteLoanResponseDTO> response = null; 


        response = restTemplate.exchange(
            loanCreateEndpointURI + "/" + loanId.toString(), // CRUD update API endpoint
            HttpMethod.DELETE,
            null,
            DeleteLoanResponseDTO.class
        );   

        log.info("Back from delete template call.  Http Status: " + response.getStatusCode().toString());

        if(Objects.nonNull(response))
            log.info("deleteLoan: successful deletion of loan with ID: " + loanId.toString());

        return response;        

    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class EntityNotFoundException extends RuntimeException {
        public EntityNotFoundException(String message) {
            super(message);
        }
    }  

    /**
     * 
     */
    @ResponseStatus(HttpStatus.NOT_MODIFIED)
    public class EntityCreateException extends RuntimeException {
        public EntityCreateException(String message) {
            super(message);
        }
    }         

}