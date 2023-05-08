package com.pvsrg.covidapi;

import com.pvsrg.covidapi.service.cases.CasesPullingService;
import com.pvsrg.covidapi.service.country.CountryPullingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ConfigurationPropertiesScan
public class CovidapiApplication {

	public static void main(String[] args) {
		SpringApplication.run(CovidapiApplication.class, args);
	}

	@Bean
	public CommandLineRunner commandLineRunner(CountryPullingService pullingService,
											   CasesPullingService casesPullingService) {
		return args -> {
			pullingService.pullCountries();
			casesPullingService.pullCases();
		};
	}

}
