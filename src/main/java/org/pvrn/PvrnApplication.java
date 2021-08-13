package org.pvrn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories("org.pvrn.jpa.repo")
@EntityScan("org.pvrn.jpa.model")
@ComponentScan
@SpringBootApplication
public class PvrnApplication {

	public static void main(String[] args) {
		SpringApplication.run(PvrnApplication.class, args);
	}
}
