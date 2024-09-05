package com.maan.veh.claim;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

@SpringBootApplication
@OpenAPIDefinition(
	    info = @Info(title = "Your API", version = "v1"),
	    servers = {@Server(url = "http://localhost:2024")},
	    security = {@SecurityRequirement(name = "bearerAuth")}
	)
	@SecurityScheme(
	    name = "bearerAuth",
	    type = SecuritySchemeType.HTTP,
	    scheme = "bearer",
	    bearerFormat = "JWT"
	)
public class VehicleClaimApplication {

	public static void main(String[] args) {
		SpringApplication.run(VehicleClaimApplication.class, args);
	}
	
	 @Bean
	    public GroupedOpenApi publicApi() {
	        return GroupedOpenApi.builder()
	            .group("v1")
	            .pathsToMatch("/**")
	            .build();
	    }
}
