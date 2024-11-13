package com.acme.saga.applicant.service;

import java.util.List;
import com.acme.saga.sagamodel.model.Applicant;

public interface ApplicantService {

    ResponseEntity<UpdateLimitResponseDTO> updateLoanLimit( Loan loan );

}
