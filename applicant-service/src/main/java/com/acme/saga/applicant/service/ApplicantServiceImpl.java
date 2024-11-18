package com.acme.saga.applicant.service;

import java.util.List;
import java.util.Objects;
import java.time.Instant;
import java.util.Date;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import com.acme.saga.applicant.helpers.Utils;
import com.acme.saga.applicant.model.Applicant;
import com.acme.saga.applicant.model.Loan;
import com.acme.saga.applicant.dto.UpdateLimitResponseDTO;
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
public class ApplicantServiceImpl implements ApplicantService {

    private RestTemplate restTemplate;

    @Value("${applicant.get.endpoint}")
    private String applicantGetEndpointURI;

    @Value("${applicant.update.endpoint}")
    private String applicantUpdateEndpointURI;

    @Override
    /**
     * 
     * @param loan
     * @return
     * @throws ResourceNotFoundException
     * @throws InsufficientFundsException
     */
    public ResponseEntity<UpdateLimitResponseDTO> updateLoanLimit( Loan loan ) 
        throws ResourceNotFoundException, InsufficientFundsException {

        restTemplate = new RestTemplate();

        ResponseEntity<Applicant> aResponse = null;

        double dLimit = 0.00, 
               dBalance = 0.00,
               dLimitUsed = 0.00;

        BigDecimal bBalance = null;

        DecimalFormat df = new DecimalFormat("######0.00");

        UpdateLimitResponseDTO updateLimitResponse = null;
 
        // retrieve applicant for loan
        //applicantGetEndpointURI += ("/" + loan.getApplicantId().toString());
        try {
            aResponse = restTemplate.getForEntity(applicantGetEndpointURI + "/" + loan.getApplicantId().toString(), Applicant.class);
        }
        catch(RuntimeException e) {
            throw new ResourceNotFoundException("Could not find Applicant with ID: " + loan.getApplicantId().toString());
        }

        Applicant rApplicant = aResponse.getBody();

        // handle response
        if(Objects.isNull(rApplicant)) {
            log.error("Unable to retrieve Applicant for ID: " + loan.getApplicantId().toString() +
                      ", for Loan ID: " + loan.getId().toString());
            throw new ResourceNotFoundException("Unable to retrieve applicant id: " + 
                                                loan.getApplicantId().toString() +
                                                ", for loan id: " + loan.getId().toString());
        }

        // setup response entity for return 
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.add("acme-loan-update", "success");
        responseHeaders.add("acme-loan-id", loan.getId().toString());
        responseHeaders.add("acme-applicant-id", rApplicant.getId().toString());


        log.info("loan object: " + loan.toString());
        log.info("applicant object: " + rApplicant.toString());

        // calculate balance         
        if(Objects.nonNull(rApplicant.getLimit()))                                                               
            dLimit = rApplicant.getLimit().doubleValue();
        if(Objects.nonNull(rApplicant.getLimitUsed()))
            dLimitUsed = rApplicant.getLimitUsed().doubleValue();
        dBalance = dLimit - dLimitUsed;

        bBalance = new BigDecimal(dBalance);

        // check loan amount is <= balance - request
        if(loan.getAmount().compareTo(bBalance) > 0) {
            log.error("Insufficient funds for Loan ID: " + loan.getId().toString());
            log.error("    Applicant ID: " + rApplicant.getId().toString());
            log.error("    Loan request: " + df.format(loan.getAmount()));
            log.error("    Applicant balance: " + df.format(bBalance));
            throw new InsufficientFundsException("Loan amount: " +
                                                    df.format(loan.getAmount()) +
                                                    ", is greater than applicant balance: " +
                                                    df.format(bBalance));
        }

        // now update with new totals
        dLimitUsed += loan.getAmount().doubleValue();
        dBalance = dLimit - dLimitUsed;       
        bBalance = new BigDecimal(dBalance);

        log.info("dLimitUsed: " + dLimitUsed+"");
        log.info("dBalance: " + dBalance+"");


        // fill out UpdateLimitResponseDTO, included in the response
        updateLimitResponse = UpdateLimitResponseDTO.builder()
                            .applicantId(rApplicant.getId())
                            .loanId(loan.getId())
                            .originalLimitAmount(rApplicant.getLimit())
                            .requestAmount(loan.getAmount())
                            .remainingAmount(new BigDecimal(df.format(bBalance)).setScale(2, RoundingMode.HALF_UP))
                            .approved(true)
                            .loanComment(loan.getComment())
                            .applicantComment(rApplicant.getComment())
                            .build();

        // update limitUsed for Applicant
        rApplicant.setLimitUsed(new BigDecimal(dLimitUsed));
        rApplicant.setLimitUpdateDate(Date.from(Instant.now()));
        
        aResponse = updateApplicant(rApplicant.getId(), rApplicant);

        // Handle the response
        if (aResponse.getStatusCode().is2xxSuccessful()) {
            log.info("Applicant with ID: " + rApplicant.getId().toString() + " updated...");
        } else {
            log.error("Update on Applicant entity failed, Applicant ID: " + rApplicant.getId().toString());
            throw new EntityUpdateException("Limit used update failed for Applicant ID: " + 
                                            rApplicant.getId().toString());
        }            

        ResponseEntity<UpdateLimitResponseDTO> uResponse = new ResponseEntity<>(updateLimitResponse, responseHeaders, HttpStatus.OK);

        return uResponse;

    }

    /**
     * 
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException {
        public ResourceNotFoundException(String message) {
            super(message);
        }
    }    

    /**
     * 
     */
    @ResponseStatus(HttpStatus.NOT_MODIFIED)
    public class EntityUpdateException extends RuntimeException {
        public EntityUpdateException(String message) {
            super(message);
        }
    }        

    /**
     * 
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public class InsufficientFundsException extends RuntimeException {
        public InsufficientFundsException(String message) {
            super(message);
        }
    }    

    /**
     * 
     * @param applicantId
     * @param applicant
     * @return
     */
    private ResponseEntity<Applicant> updateApplicant(Integer applicantId, Applicant applicant) {

        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");
        HttpEntity<Applicant> requestEntity = new HttpEntity<>(applicant, headers);
        ResponseEntity<Applicant> response = restTemplate.exchange(
            applicantUpdateEndpointURI + "/" + applicant.getId().toString(), // CRUD update API endpoint
            HttpMethod.PUT,
            requestEntity,
            Applicant.class
        );

        if(Objects.nonNull(response))
            log.info("updateApplicant: successful update on loan balance for Applicant ID: " + applicant.getId().toString());

         return response;

    }
}