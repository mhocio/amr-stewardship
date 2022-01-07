package com.top.antibiotic;

import com.top.antibiotic.configuration.SwaggerConfiguration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.Environment;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
@EnableAsync
@Slf4j
@Import(SwaggerConfiguration.class)
public class AntibioticApplication {

	public static void main(String[] args) {
		SpringApplication.run(AntibioticApplication.class, args);
	}

	@Bean
	ApplicationRunner applicationRunner(Environment environment) {
		//https://www.baeldung.com/spring-profiles#3-context-parameter-in-webxml
		return args -> {
			log.info("message from application.properties " + environment.getProperty("message-from-application-properties"));
			log.info("active profiles: " + environment.getProperty("spring.profiles.active"));
		};
	}
}

