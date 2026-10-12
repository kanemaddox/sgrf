package com.saims.sgrf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.scheduling.annotation.EnableAsync;


@EnableJpaAuditing
@EnableAsync
@SpringBootApplication
public class SgrfApplication /*extends SpringBootServletInitializer*/{
	
	/*
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(SgrfApplication.class);
	} para produccion*/
	
	public static void main(String[] args) {		
		String sgrfHome = System.getenv("SGRF_HOME");

        System.setProperty(
            "spring.config.additional-location",
            "file:" + sgrfHome + "/config/"
        );
        
		SpringApplication.run(SgrfApplication.class, args);

	}
}
