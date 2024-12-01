package com.acme.saga;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * external configuration binding
 *   via --spring.config.location=file:/path/to/myconfig.yml approach
 *   env var SPRING_CONFIG_LOCATION
 */

@SpringBootApplication
public class CreateLoanApplication {

	public static void main(String[] args) {
		SpringApplication.run(CreateLoanApplication.class, args);
	}

}
