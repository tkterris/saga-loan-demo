package com.acme.saga.converts;

import org.apache.camel.Converter;
import org.apache.camel.Exchange;
import org.apache.camel.TypeConverter;
import org.apache.camel.TypeConverters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter; 
import org.springframework.stereotype.Component;
import com.acme.saga.model.Loan;
import java.io.InputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.io.ByteArrayInputStream;


@Converter
public final class LoanConverter {

	//private final ObjectMapper mapper;

	@Converter
//	public InputStream toInputStream(byte[] data, Exchange exchange) {
    public static Loan toLoan(byte[] data, Exchange exchange) {
		TypeConverter converter = exchange.getContext().getTypeConverter();
		Loan loan = null;
		String jsonString = null;
		ObjectMapper mapper = new ObjectMapper();

		try {
			//loan = (Loan) exchange.getIn().getBody(Loan.class);
			ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
			jsonString = ow.writeValueAsString(data);
			loan = mapper.readValue(jsonString, Loan.class);
		}
		catch(com.fasterxml.jackson.core.JsonProcessingException e) {

		}

		// Loan loan = Loan.builder()
		// 			.Id()
		// 			.amount()
		// 			.applicantId()
		// 			.approved()
		// 			.loanRequestDate()
		// 			.loanCancelDate()
		// 			.loanApprovalDate()
		// 			.comment()
		// 			.build();

		return loan;
	}

	@Converter	
	public static InputStream toInputStream(Loan data, Exchange exchange) {

		TypeConverter converter = exchange.getContext().getTypeConverter();
		byte[] jsonBytes = null;
		InputStream jsonStream = null;
		ObjectMapper mapper = new ObjectMapper();

		try {
			ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
			jsonBytes = ow.writeValueAsBytes(data);
			// loan = mapper.readValue(jsonString, Loan.class);
			jsonStream = new ByteArrayInputStream(jsonBytes);			
		}
		catch(com.fasterxml.jackson.core.JsonProcessingException e) {

		}
		catch(IOException e) {

		}

		return jsonStream;

	}
}
