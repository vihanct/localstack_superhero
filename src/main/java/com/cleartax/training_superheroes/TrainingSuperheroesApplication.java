package com.cleartax.training_superheroes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling  // Enable scheduling
public class TrainingSuperheroesApplication {
	public static void main(String[] args) {
		SpringApplication.run(TrainingSuperheroesApplication.class, args);
	}

}
