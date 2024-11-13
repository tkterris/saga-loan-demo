package com.acme.saga.applicant.service;

import java.util.List;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.http.ResponseEntity;
import com.acme.saga.applicant.helpers.Utils;
import com.acme.saga.sagamodel.model.Applicant;
import com.acme.saga.applicant.dto.UpdateLimitResponseDTO;
import org.springframework.web.client.RestTemplate;

@Service
public class ApplicantServiceImpl implements ApplicantService {

    @Autowired
    private RestTemplate restTemplate;

    @Value("${applicant.get.endpoint}")
    private String applicantEndpointURI;


    @Override
    public ResponseEntity<UpdateLimitResponseDTO> updateLoanLimit( Loan loan ) {

        UpdateLimitResponseDTO updateLimitResponse = new UpdateLimitResponseDTO();

        ResponseEntity<Applicant> aResponse = restTemplate.getForEntity(applicantEndpointURI, Applicant.class);
        Applicant rApplicant = aResponse.getBody();
        ResponseEntity<UpdateLimitResponseDTO> uResponse = ResponseEntity.status(HttpStatus.OK)
                                                                        .header("custom-key", "custom-value");

        if (Objects.nonNull(uResponse)) {
            // fill out UpdateLimitResponseDTO
            updateLimitResponse.builder()
                               .applicantId(rApplicant.getId())
                               .loanId(loan.getId())
                               .originalLimitAmount()
                               .requestAmount()
                               .remainingAmount()
                               .approved()
                               .loanComment(loan.getComment())
                               .applicantComment(rApplicant.getComment());
            uResponse.setBody( updateLimitResponse );
            return uResponse;
        } else {
            throw new ResourceNotFoundException("");
        }        


    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    public class ResourceNotFoundException extends RuntimeException {
        // ...
    }    


}