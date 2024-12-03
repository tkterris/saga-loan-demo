package com.acme.gateway.service;

import java.util.List;
import java.util.Objects;
import java.time.Instant;
import java.util.Date;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import com.acme.gateway.helpers.Utils;
import com.acme.gateway.model.Loan;
import com.acme.gateway.dto.CreateLoanResponseDTO;
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
public class RedirectServiceImpl implements RedirectService {
    

    private RestTemplate restTemplate;

    @Value("${gateway.endpoint}")
    private String createLoanSagaEndpointURI;


    /**
     * 
     * forwards HTTPS post to HTTP post (createLoan saga endpoint)
     * 
     * @param loan
     * @return
     */
    public ResponseEntity<CreateLoanResponseDTO> createLoan( Loan loan ) {

        if(Objects.isNull(restTemplate))
            restTemplate = new RestTemplate();

        ResponseEntity<Loan> lResponse = null;
        CreateLoanResponseDTO createLoanResponse = null;

        try {
            lResponse = createLoanEntity( loan );               
        }
        catch(RuntimeException e) {
            log.error("Failed in http forward to createLoan --- loan with ID: " + loan.getId().toString());
            throw new HttpForwardFailedException("Unable to forward loan with ID: " + loan.getId().toString());
        }

        // Handle the response
        if (lResponse.getStatusCode().is2xxSuccessful()) {
            log.info("Loan with ID: " + loan.getId().toString() + " forwarded...");
        } else {
            log.error("Forward on Loan entity failed, loan ID: " + loan.getId().toString());
            throw new HttpForwardFailedException("HTTP forward failed for Loan ID: " + 
                                            loan.getId().toString());
        }               

        // setup response entity for return 
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("acme-create-loan", "success");
        responseHeaders.add("acme-loan-id", loan.getId().toString());

        createLoanResponse = CreateLoanResponseDTO.builder()
                            .loanId(lResponse.getBody().getId())
                            .requestAmount(loan.getAmount())
                            .applicantId(loan.getApplicantId())
                            .loanCreationDate(Date.from(Instant.now()))
                            .comment(loan.getComment() + " - successfully forwarded...")
                            .build();

        ResponseEntity<CreateLoanResponseDTO> cResponse = new ResponseEntity<>(createLoanResponse, responseHeaders, HttpStatus.OK);

        log.info("exiting createLoan, CreateLoanResponseDTO is: " + createLoanResponse.toString());
        
        return cResponse;

    }


    /**
     * 
     * @param loan
     * @return
     */
    public ResponseEntity<Loan> createLoanEntity( Loan loan ) {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Loan> requestEntity = new HttpEntity<>(loan, headers);

        ResponseEntity<Loan> response = restTemplate.exchange(
            createLoanSagaEndpointURI, // createLoan saga endpoint
            HttpMethod.POST,
            requestEntity,
            Loan.class
        );        

        if(Objects.nonNull(response))
            log.info("createLoan forward: successful forwarding of loan with ID: " + loan.getId().toString());

        return response;
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class HttpForwardFailedException extends RuntimeException {
        public HttpForwardFailedException(String message) {
            super(message);
        }
    }  

}
