package com.acme.saga.applicant.model;

import java.io.Serializable;
import java.math.BigDecimal;
import lombok.Data;
import lombok.RequiredArgsConstructor;


@Data
@RequiredArgsConstructor
public class Applicant implements Serializable {
    
    private Integer Id;    
    private BigDecimal limit;
    private Boolean approved;
    private java.util.Date limitUpdateDate;
    private String comment;
    private BigDecimal limitUsed;   

}
