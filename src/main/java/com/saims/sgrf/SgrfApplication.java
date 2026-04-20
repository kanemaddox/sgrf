package com.saims.sgrf;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;


@EnableJpaAuditing

@SpringBootApplication
public class SgrfApplication {

	public static void main(String[] args) {
		SpringApplication.run(SgrfApplication.class, args);
	}

}
