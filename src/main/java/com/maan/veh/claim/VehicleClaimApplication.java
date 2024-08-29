package com.maan.veh.claim;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
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
