package org.mrn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("org.mrn.jpa.repo")
@EntityScan({"org.mrn.jpa.model", "org.mrn.service.builder"})
@ComponentScan
@SpringBootApplication
public class PvrnApplication {

	public static void main(String[] args) {
		SpringApplication.run(PvrnApplication.class, args);
	}
}
