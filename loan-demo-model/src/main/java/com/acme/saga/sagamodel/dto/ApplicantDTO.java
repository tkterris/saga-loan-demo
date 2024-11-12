package com.acme.saga.sagamodel.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApplicantDTO {

    private Integer id;
    private BigDecimal limit;
    private Boolean approved;
    private Date limitUpdateDate;
    private String comment;    

}