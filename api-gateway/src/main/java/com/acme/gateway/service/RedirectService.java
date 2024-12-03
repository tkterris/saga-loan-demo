package com.acme.gateway.service;

import java.util.List;
import com.acme.gateway.dto.CreateLoanResponseDTO;
import org.springframework.http.ResponseEntity;
import com.acme.gateway.model.Loan;


public interface RedirectService {
    
    ResponseEntity<CreateLoanResponseDTO> createLoan( Loan loan );
    
}
