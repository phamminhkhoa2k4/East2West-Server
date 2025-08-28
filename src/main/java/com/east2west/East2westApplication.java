package com.east2west;




import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
@SpringBootApplication
public class East2westApplication {




	public static void main(String[] args) {
		SpringApplication.run(East2westApplication.class, args);
	}
}
