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
@Table(name = "applicant")
@Data
@RequiredArgsConstructor
public class Applicant implements Serializable {
    
    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name="id")
    private Integer Id;    

    @Column(name="\"limit\"")
    private BigDecimal limit;

    @Column(name="approved")
    private Boolean approved;

    @Column(name="limitupdatedate")
    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date limitUpdateDate;

    @Column(name="comment")
    private String comment;

    @Column(name="limitused")
    private BigDecimal limitUsed;   

}
