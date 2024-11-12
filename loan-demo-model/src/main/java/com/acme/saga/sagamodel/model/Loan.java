package com.acme.saga.sagamodel.model;

import java.io.Serializable;
import java.math.BigDecimal;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Entity
@Table(name = "loan")
@Data
@RequiredArgsConstructor
public class Loan implements Serializable {
   
    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Integer Id;  
    
     @Column(name="amount")
    private BigDecimal amount;
    
    @Column(name="applicantid")
    private Integer applicantId;

    @Column(name="approved")
    private Boolean approved;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="loanrequestdate")
    private java.util.Date loanRequestDate;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="loancanceldate")
    private java.util.Date loanCancelDate;
 
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name="loanapprovaldate")   
    private java.util.Date loanApprovalDate;

    @Column(name="comment")
    private String comment;

}
