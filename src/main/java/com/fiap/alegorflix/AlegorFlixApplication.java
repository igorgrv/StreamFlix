package com.fiap.alegorflix;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.config.EnableMongoAuditing;

@SpringBootApplication
@EnableMongoAuditing
public class AlegorFlixApplication {

	public static void main(String[] args) {
		SpringApplication.run(AlegorFlixApplication.class, args);
	}

}
