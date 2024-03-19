package com.packt.ranking;

import java.util.function.Consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RankingApplication {

	public static void main(String[] args) {
		SpringApplication.run(RankingApplication.class, args);
	}

	@Bean
	public Consumer<MatchEvent> processGoals() {
		return value -> {
			System.out.println("Processing event type: " + value.getType() + ". Goal?");
		};
	}

}
