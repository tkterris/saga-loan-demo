package com.acme.saga.service;

import java.util.Objects;
import java.time.Instant;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import com.acme.saga.model.Loan;
import com.acme.saga.dto.CreateLoanResponseDTO;
import com.acme.saga.exception.SagaInvocationException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.beans.factory.annotation.Value;
import org.apache.camel.ProducerTemplate;
import org.apache.camel.FluentProducerTemplate;
import org.apache.camel.EndpointInject;
import org.apache.camel.CamelExecutionException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CreateLoanServiceImpl implements CreateLoanService {

    @Value("${saga-demo.saga.route.endpoint}")
    private String sagaRouteEndpointURI;

    /**
     * Inject Camel producer to use camel-geocoder to find location where we are
     */
    @EndpointInject(value = "direct:saga")
    private FluentProducerTemplate producerTemplate;

    /**
     * 
     * @param loan
     * @return
     * @throws SagaInvocationException
     */
    public ResponseEntity<CreateLoanResponseDTO> createLoan( Loan loan ) 
            throws SagaInvocationException {


        ResponseEntity<Loan> lResponse = null;
        CreateLoanResponseDTO createLoanResponse = null;

        try {
            lResponse = createLoanEntity( loan );            
        }
        catch( CamelExecutionException e ) {
            log.error("Camel route exception for ID: " + 
                loan.getId().toString() + 
                ", with message: " + 
                e.getMessage());
            throw new SagaInvocationException("Saga execution exception on create loand with ID: " + loan.getId().toString());
        }
        catch( RuntimeException e ) {
            log.error("Unable to create Loan with ID: " + 
                loan.getId().toString() + 
                ", with message: " + 
                e.getMessage());
            throw new SagaInvocationException("Saga runtime exception on create loan with ID: " + loan.getId().toString());
        }

        // Handle the response
        if (lResponse.getStatusCode().is2xxSuccessful()) {
            log.info("Loan with ID: " + loan.getId().toString() + " added...");
        } else {
            log.error("Create on Loan entity failed, loan ID: " + loan.getId().toString());
            throw new SagaInvocationException("Create Loan saga invocation failed for Loan ID: " + 
                                            loan.getId().toString());
        }               

        // setup response entity for return 
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("acme-loan-create", "success");
        responseHeaders.add("acme-loan-id", loan.getId().toString());

        createLoanResponse = CreateLoanResponseDTO.builder()
                            .loanId(lResponse.getBody().getId())
                            .requestAmount(loan.getAmount())
                            .applicantId(loan.getApplicantId())
                            .loanCreationDate(Date.from(Instant.now()))
                            .comment(loan.getComment() + " - saga create loan successful..")
                            .build();

        ResponseEntity<CreateLoanResponseDTO> cResponse = new ResponseEntity<>(createLoanResponse, responseHeaders, HttpStatus.OK);

        log.info("exiting saga createLoan, CreateLoanResponseDTO is: " + createLoanResponse.toString());
        
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

        ResponseEntity<Loan> response = producerTemplate
                                            .withBodyAs( loan, Loan.class )
                                            .withHeader("Content-Type", "application/json")
                                            .request();

        
        // ResponseEntity<Loan> response = restTemplate.exchange(
        //     sagaRouteEndpointURI, // CRUD update API endpoint
        //     HttpMethod.POST,
        //     requestEntity,
        //     Loan.class
        // );        

        if(Objects.nonNull(response))
            log.info("createLoan: successful creation of loan with ID: " + loan.getId().toString());

        return response;

    }


}
