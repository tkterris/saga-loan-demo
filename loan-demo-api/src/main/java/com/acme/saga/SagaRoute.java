package com.acme.saga;

import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestParamType;
import org.apache.camel.model.dataformat.JsonLibrary;

import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.openapitools.jackson.nullable.JsonNullableModule;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.component.gson.GsonDataFormat;
import org.apache.camel.model.DataFormatDefinition;
import org.apache.camel.CamelContext;
import org.apache.camel.model.SagaPropagation;
import org.apache.camel.component.jackson.JacksonConstants;
import com.google.gson.FieldNamingPolicy;
import lombok.extern.slf4j.Slf4j;
import com.acme.saga.model.Loan;
import com.acme.saga.dto.CreateLoanResponseDTO;
import org.apache.camel.component.saga.SagaEndpoint;
import java.util.HashMap;
import java.util.Objects;

@Slf4j
@Component
public class SagaRoute extends RouteBuilder {
  
    @Value("${saga-demo.loan.create.endpoint}")
    private String loanCreateEndpointURI;

    @Value("${saga-demo.loan.delete.endpoint}")
    private String deleteLoanEndpointURI;

    @Value("${saga-demo.applicant.update-limit.endpoint}")
    private String updateLimitEndpointURI;

    // map of requestLoanId <String>, newLoanId <Integer>
    // need a cache for cross referencing reference loan id vs created loan id on callback from lra
    private HashMap<String, Integer> inProcessLoans;


    @Override
    public void configure() throws Exception {

        log.info("starting SagaRoute configure...");

        if(Objects.isNull(inProcessLoans))
            inProcessLoans = new HashMap<String, Integer>();

        // entrypoint to saga route
        rest()
            .post("/saga/route")
                .description("creates a loan")
                .id("createLoanApi")
                .consumes("application/json")
                .produces("application/json")
                .type(Loan.class)       
                .param()
                    .name("loan")
                    .type(RestParamType.body)
                    .required(false)
                    .description("Loan to add")
                .endParam()
                .to("direct:saga");

        // saga implementation for create loan
        from("direct:saga")
            .log("entering saga route rest endpoint...")   
            .saga()
                // if propagation is enabled, it causes a disconnect with LRA coordinator          
                .compensation("direct:deleteLoan")
                .completion("direct:completeLoan")
                .process( exchange -> {
                    Loan loan = exchange.getIn().getBody(Loan.class);
                    exchange.getIn().setHeader("originalLoan", loan.toString());
                })
                .option("originalLoanId", simple("${body.id}"))
                .to("direct:addLoan")
                .to("direct:updateLoanLimit")
            .setBody(header("Long-Running-Action"))
            .end();

        // createloan takes Loan in body
        // returns ResponseEntity<CreateLoanResponseDTO>
        from("direct:addLoan")
            .log("invoked addLoan endpoint...")
            .process( exchange -> {
                Loan loan = (Loan) exchange.getIn().getBody(Loan.class);
                log.info("loan request id: " + loan.getId().toString());
                log.info("loan->id: " + loan.getId().toString() + 
                         ", amount: " + loan.getAmount().toString() +
                         ", applicantId: " + loan.getApplicantId().toString() +
                         ", approved: " + loan.getApproved().toString() +
                         ", loanRequestDate: " + loan.getLoanRequestDate().toString());  
                exchange.setVariable("loanRequest", loan);
                exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);                      
            })
            .setHeader(Exchange.HTTP_METHOD, constant("POST")) // Set HTTP method
            .setHeader("Content-Type", constant("application/json")) // Set Content-Type header
            .convertBodyTo(String.class)
            .log("loan endpoint is: " + loanCreateEndpointURI)
            // re-use incoming body(type Loan)
            .to(loanCreateEndpointURI+"?bridgeEndpoint=true")
            .process( exchange -> {
                CreateLoanResponseDTO lResponse = exchange.getIn().getBody(CreateLoanResponseDTO.class);

                log.info("createLoan DTO response: " + lResponse.toString());

                // cache the in process loan
                // will be needed in the event a failure occurs and compensate is triggered
                inProcessLoans.put(exchange.getVariable("loanRequest", Loan.class).getId().toString(), lResponse.getLoanId());

                log.info("new loan id: " + 
                        lResponse.getLoanId().toString() + 
                        " with reference loan id: " +
                        exchange.getVariable("loanRequest", Loan.class).getId().toString() + 
                        " added to inprocess cache...");
            })
            .convertBodyTo(Loan.class)
            .log("Loan added...");    


        // delete loan just takes id in the path, no body
        // returns ResponseEntity<DeleteLoanResponseDTO>
        from("direct:deleteLoan")
            .log("invoking deleteLoan...")
            // add current loan to list of unfinished loans
            .process( exchange -> {

                String originalLoanId = exchange.getIn().getHeader("originalLoanId").toString();

                log.info("Deleting loan with id: " + originalLoanId);
                log.info("Retrieving loan from inProcess cache...");
                Integer loanId = inProcessLoans.get(originalLoanId);     // retrieve loan from inprocess cache

                exchange.getMessage().setHeader("loanIdNew", loanId);
                exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);                           
            })
            .setHeader(Exchange.HTTP_METHOD, constant("POST")) // Set HTTP method
            // DELETE request has NO response BODY 
            // this is probably throwing off LRA
            .setHeader("Content-Type", constant("application/json")) // Set Content-Type header
            .toD(deleteLoanEndpointURI + "/" + "${header.loanIdNew}" + "?bridgeEndpoint=true")            
            // per Rob --- .setHeader(Exchange.HTTP_URI, simple(deleteLoanEndpointURI + "/" + "${header.loanIdNew}"))
            // per Rob --- .to("http://set-by-header?bridgeEndpoint=true")
            // per Rob --- toD is inefficient.  it creates a 2nd endpoint handler
            .setBody(simple("deletion successful"))
            .log("deleteLoan invocation completed successfully...");

        // updateloanlimit takes loan as body
        // returns UpdateLimitResponseDTO
        from("direct:updateLoanLimit")
            .log("invoked updateLoanLimit endpoint...")
            .process( exchange -> {
                exchange.getIn().setBody(exchange.getVariable("loanRequest", Loan.class));
                Loan loan = (Loan) exchange.getIn().getBody(Loan.class);
                log.info("loan request id: " + loan.getId().toString());
                log.info("loan->id: " + loan.getId().toString() + 
                         ", amount: " + loan.getAmount().toString() +
                         ", applicantId: " + loan.getApplicantId().toString() +
                         ", approved: " + loan.getApproved().toString() +
                         ", loanRequestDate: " + loan.getLoanRequestDate().toString());  
                exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);                           
            })
            .setHeader(Exchange.HTTP_METHOD, constant("PUT")) // Set HTTP method
            .setHeader("Content-Type", constant("application/json")) // Set Content-Type header
            .convertBodyTo(String.class)
            .log("updateLoanLimit endpoint is: " + updateLimitEndpointURI)
            // re-use incoming body(type Loan)
            .to(updateLimitEndpointURI+"?bridgeEndpoint=true")
            .convertBodyTo(Loan.class)
            .log("invoked updateLoanLimit...");

        from("direct:completeLoan")
            .log("Saga loan process has completed...");

    }


    @Bean
    CamelContextConfiguration contextConfiguration() {
        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext context) {
            // Enable Jackson JSON type converter.
                context.getGlobalOptions().put(JacksonConstants.ENABLE_TYPE_CONVERTER, "true");
            // Allow Jackson JSON to convert to pojo types also
            // (by default Jackson only converts to String and other simple types)
                getContext().getGlobalOptions().put(JacksonConstants.TYPE_CONVERTER_TO_POJO, "true");
            }
    
            @Override
            public void afterApplicationStart(CamelContext camelContext) {
    
            }
        };
    }   

}
