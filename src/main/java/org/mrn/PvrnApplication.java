package org.mrn;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;

@EnableScheduling
@EnableJpaRepositories("org.mrn.jpa.repo")
@EntityScan({ "org.mrn.jpa.model", "org.mrn.service.builder" })
@ComponentScan
@SpringBootApplication
public class PvrnApplication {
	
	private static final long SHUT_DOWN_TIME = 16200000; // 4.5 Hours

	public static void main(String[] args) {
		SpringApplication.run(PvrnApplication.class, args);
	}

	@Scheduled(fixedRate = SHUT_DOWN_TIME, initialDelay = SHUT_DOWN_TIME)
	public void shutdownApp() {
	    System.exit(0);
	}

	@Bean(name = "multipartResolver")
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setMaxUploadSize(-1);
		return multipartResolver;
	}
}