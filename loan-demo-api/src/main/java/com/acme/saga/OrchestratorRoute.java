package com.acme.saga;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;
import org.openapitools.model.*;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;

@Component
public class OrchestratorRoute {
    
    @Bean
    @Primary
    public Jackson2ObjectMapperBuilder customObjectMapper() {
        return new Jackson2ObjectMapperBuilder()
                // other configs are possible
                .modules(new JsonNullableModule());
    }

    
    @Override
    public void configure() throws Exception {
     
         /**
        GET /saga/applicant : searches applicants
        **/
        rest()
            .post("/saga/createloan")
                .description("creates a loan request")
                .id("createLoanApi")
                .consumes("application/json")
                .type(LoanRequest.class)           
                .param()
                    .name("loanRequest")
                    .type(RestParamType.body)
                    .required(false)
                    .description("Loan request")
                .endParam()
                .to("direct:sagaCreateLoan");

        from("direct:sagaCreateLoan")
            .saga()
            .compensation("direct:deleteLoan")
            .completion("direct:completeLoan")
                .log("Executing saga #${header.id} with LRA ${header.Long-Running-Action}")
                .to("direct:createLoan")
                .to("direct:updateLimit")
                //.setHeader("payFor", constant("train"))
                .log("loan created for saga #${header.id} with loan details: ${body}")
            .setBody(header("Long-Running-Action"))
            .end();            

        from("direct:createLoan")

            // ensure loan id in header
            .log("Create loan...");    

        from("direct:deleteLoan")
            // use loan id
            .log("Delete loan...");

        from("direct:updateLimit")
            // need success and fail scenarios here
            .log("Update limit...");
    }

}
