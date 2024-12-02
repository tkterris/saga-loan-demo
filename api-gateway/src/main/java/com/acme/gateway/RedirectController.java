package com.acme.gateway;

import com.acme.gateway.dto.CreateLoanResponseDTO;
import com.acme.gateway.model.Loan;
import org.springframework.http.MediaType;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import java.net.URI;

@RestController
public class RedirectController {

    @Value("${gateway.endpoint}")
    private String redirectURI;

    @PostMapping(path = "/redirect", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public  ResponseEntity<CreateLoanResponseDTO> redirectToHttp(@RequestBody Loan loan) {
        // Process the data...
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create(redirectURI));
        return new ResponseEntity<CreateLoanResponseDTO>(headers, HttpStatus.FOUND);
    }
    
}
