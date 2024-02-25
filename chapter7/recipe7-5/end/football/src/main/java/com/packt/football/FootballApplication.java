package com.packt.football;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ImportRuntimeHints;
import com.packt.football.config.FootballRuntimeHints;

@EnableCaching
@SpringBootApplication
@ImportRuntimeHints(FootballRuntimeHints.class)
public class FootballApplication {

	public static void main(String[] args) {
		SpringApplication.run(FootballApplication.class, args);
	}

}
