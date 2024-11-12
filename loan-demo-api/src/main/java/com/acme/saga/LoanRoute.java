package com.acme.saga;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;
import org.openapitools.model.*;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;

@Component
public class LoanRoute extends RouteBuilder {
    
    @Bean
    @Primary
    public Jackson2ObjectMapperBuilder customObjectMapper() {
        return new Jackson2ObjectMapperBuilder()
                // other configs are possible
                .modules(new JsonNullableModule());
    }

    @Override
    public void configure() throws Exception {
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JsonNullableModule());
 
         /**
        GET /saga/createloan/loan : retrieves a loan
        **/
        rest()
            .get("/saga/createloan/loan")
                .description("retrieves a loan")
                .id("getLoanApi")
                .produces("application/json")
                .outType(Loan[].class)
                .param()
                    .name("searchString")
                    .type(RestParamType.query)
                    .required(false)
                    .description("pass an optional search string for looking up loans")
                .endParam()
                .param()
                    .name("skip")
                    .type(RestParamType.query)
                    .required(false)
                    .description("number of records to skip for pagination")
                .endParam()
                .param()
                    .name("limit")
                    .type(RestParamType.query)
                    .required(false)
                    .description("maximum number of records to return")
                .endParam()
                .to("direct:searchLoans");

        /**
        POST /saga/createloan/loan : adds a loan
        **/
        rest()
            .post("/saga/createloan/loan")
                .description("adds a loan")
                .id("addLoanApi")
                .consumes("application/json")
                .type(Loan.class)
                
                .param()
                    .name("loan")
                    .type(RestParamType.body)
                    .required(false)
                    .description("Loan to add")
                .endParam()
                .to("direct:addLoan");

        /**
        DELETE /saga/createloan/loan : deletes a loan
        **/
        rest()
            .delete("/saga/createloan/loan")
                .description("deletes a loan")
                .id("deleteLoanApi")
                .consumes("application/json")
                .type(Loan.class)
                
                .param()
                    .name("loan")
                    .type(RestParamType.body)
                    .required(false)
                    .description("Loan to delete")
                .endParam()
                .to("direct:deleteLoan");

    }


}
