package com.acme.saga.applicant.service;

import java.util.List;
import com.acme.saga.applicant.dto.UpdateLimitResponseDTO;
import org.springframework.http.ResponseEntity;
import com.acme.saga.applicant.model.Loan;

public interface ApplicantService {

    ResponseEntity<UpdateLimitResponseDTO> updateLoanLimit( Loan loan );

}
