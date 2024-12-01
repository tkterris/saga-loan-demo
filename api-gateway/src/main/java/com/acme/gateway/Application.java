package com.acme.gateway;

import reactor.core.publisher.Mono;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// tag::code[]
@SpringBootApplication
@EnableConfigurationProperties(UriConfiguration.class)
@RestController
public class Application {

    @Value("${gateway.endpoint}")
    String endpointURI;


	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	// tag::route-locator[]
	@Bean
	public RouteLocator myRoutes(RouteLocatorBuilder builder, UriConfiguration uriConfiguration) {
		String httpUri = uriConfiguration.getHttpEndpoint();
		return builder.routes()
			.route(p -> p
				.path("/get")
				.filters(f -> f.addRequestHeader("Hello", "World"))
				.uri(httpUri))
			.route(p -> p
				.host("*.circuitbreaker.com")
				.filters(f -> f
					.circuitBreaker(config -> config
						.setName("mycmd")
						.setFallbackUri("forward:/fallback")))
				.uri(httpUri))
			.build();
	}
	// end::route-locator[]

	// tag::fallback[]
	@RequestMapping("/fallback")
	public Mono<String> fallback() {
		return Mono.just("fallback");
	}
	// end::fallback[]
}

// tag::uri-configuration[]
@ConfigurationProperties
class UriConfiguration {
	
	private String httpEndpoint = endpointURI;

	public String getHttpEndpoint() {
		return httpEndpoint;
	}

	public void setHttpEndpoint(String httpEndpoint) {
		this.httpEndpoint = httpEndpoint;
	}
}
// end::uri-configuration[]
// end::code[]
