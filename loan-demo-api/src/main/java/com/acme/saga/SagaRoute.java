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
import org.apache.camel.component.jackson.JacksonConstants;
import com.google.gson.FieldNamingPolicy;
import lombok.extern.slf4j.Slf4j;
import com.acme.saga.model.Loan;

@Slf4j
@Component
public class SagaRoute extends RouteBuilder {
  
    @Value("${saga-demo.loan.create.endpoint}")
    private String loanCreateEndpointURI;

    @Value("${saga-demo.loan.delete.endpoint}")
    private String deleteLoanEndpointURI;

    @Value("${saga-demo.applicant.update-limit.endpoint}")
    private String updateLimitEndpointURI;

    //@Bean
    // @Primary
    // public Jackson2ObjectMapperBuilder customObjectMapper() {
    //     return new Jackson2ObjectMapperBuilder()
    //             // other configs are possible
    //             .modules(new JsonNullableModule());
    // }

    @Override
    public void configure() throws Exception {

        log.info("starting SagaRoute configure...");

        CamelContext context = getContext();

        // Enable Jackson JSON type converter for more types.
        context.getGlobalOptions().put("CamelJacksonEnableTypeConverter", "true");
        // Allow Jackson JSON to convert to pojo types also
        // (by default, Jackson only converts to String and other simple types)
        context.getGlobalOptions().put("CamelJacksonTypeConverterToPojo", "true");

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
                .to("direct:addLoan")
                .to("direct:updateLoanLimit")
            .compensation("direct:deleteLoan")
            .completion("direct:completeLoan")
            .setBody(header("Long-Running-Action"))
            .end();

        // createloan takes Loan in body
        // returns ResponseEntity<CreateLoanResponseDTO>
        from("direct:addLoan")
            .log("invoked addLoan endpoint...")
            .process( exchange -> {
                Loan loan = (Loan) exchange.getIn().getBody(Loan.class);
                log.info("loan->id: " + loan.getId().toString() + 
                         ", amount: " + loan.getAmount().toString() +
                         ", applicantId: " + loan.getApplicantId().toString() +
                         ", approved: " + loan.getApproved().toString() +
                         ", loanRequestDate: " + loan.getLoanRequestDate().toString());  
                exchange.setVariable("loanRequest", loan);
                //org.apache.camel.TypeConverter tc = exchange.getContext().getTypeConverter();
                //String str_value = tc.convertTo(String.class, exchange.getIn().getBody());
                //exchange.getOut().setBody(loan);
                exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);                           
            })
            // setting up for creatLoan invocation
            //.unmarshal().json(JsonLibrary.Jackson)
           // .unmarshal().json(JsonLibrary.Jackson, Loan.class)
            .setHeader(Exchange.HTTP_METHOD, constant("POST")) // Set HTTP method
            .setHeader("Content-Type", constant("application/json")) // Set Content-Type header
            //.unmarshal().json(JsonLibrary.Jackson)
            .convertBodyTo(String.class)
            .log("loan endpoint is: " + loanCreateEndpointURI)
            // re-use incoming body(type Loan)
            .to(loanCreateEndpointURI+"?bridgeEndpoint=true")
            .convertBodyTo(Loan.class)
            .log("Loan added...");    


        // delete loan just takes id in the path, no body
        // returns ResponseEntity<DeleteLoanResponseDTO>
        from("direct:deleteLoan")
        .log("invoking deleteLoan...")
        .process( exchange -> {
            exchange.getIn().setBody(exchange.getVariable("loanRequest", Loan.class));
            Loan loan = (Loan) exchange.getIn().getBody(Loan.class);
            log.info("loan->id: " + loan.getId().toString() + 
                     ", amount: " + loan.getAmount().toString() +
                     ", applicantId: " + loan.getApplicantId().toString() +
                     ", approved: " + loan.getApproved().toString() +
                     ", loanRequestDate: " + loan.getLoanRequestDate().toString());  
            exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);                           
        })
        .setHeader(Exchange.HTTP_METHOD, constant("DELETE")) // Set HTTP method
        .setHeader("Content-Type", constant("application/json")) // Set Content-Type header
        //.unmarshal().json(JsonLibrary.Jackson)
        .convertBodyTo(String.class)
        .log("deleteLoan endpoint is: " + deleteLoanEndpointURI)
        // re-use incoming body(type Loan)
        .to(deleteLoanEndpointURI+"?bridgeEndpoint=true")
        .convertBodyTo(Loan.class)  
        .log("invoked deleteLoan...");

        // updateloanlimit takes loan as body
        // returns UpdateLimitResponseDTO
        from("direct:updateLoanLimit")
            .log("invoked updateLoanLimit endpoint...")
            .process( exchange -> {
                exchange.getIn().setBody(exchange.getVariable("loanRequest", Loan.class));
                Loan loan = (Loan) exchange.getIn().getBody(Loan.class);
                log.info("loan->id: " + loan.getId().toString() + 
                         ", amount: " + loan.getAmount().toString() +
                         ", applicantId: " + loan.getApplicantId().toString() +
                         ", approved: " + loan.getApproved().toString() +
                         ", loanRequestDate: " + loan.getLoanRequestDate().toString());  
                exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, 200);                           
            })
            .setHeader(Exchange.HTTP_METHOD, constant("PUT")) // Set HTTP method
            .setHeader("Content-Type", constant("application/json")) // Set Content-Type header
            //.unmarshal().json(JsonLibrary.Jackson)
            .convertBodyTo(String.class)
            .log("updateLoanLimit endpoint is: " + updateLimitEndpointURI)
            // re-use incoming body(type Loan)
            .to(updateLimitEndpointURI+"?bridgeEndpoint=true")
            .convertBodyTo(Loan.class)
            .log("invoked updateLoanLimit...");

        from("direct:completeLoan")
            .transform().header(Exchange.SAGA_LONG_RUNNING_ACTION)
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
