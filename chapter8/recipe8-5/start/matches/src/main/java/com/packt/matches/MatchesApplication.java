package com.packt.matches;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.function.Supplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.packt.matches.domain.MatchEvent;

@SpringBootApplication
public class MatchesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatchesApplication.class, args);
	}

	@Bean
	public Supplier<MatchEvent> matchEvents() {
		Random random = new Random();
		return () -> {
			return MatchEvent.builder()
					.withMatchId(1L)
					.withType(random.nextInt(0, 10))
					.withEventTime(LocalDateTime.now())
					.withDescription("random event")
					.withPlayer1(null)
					.withPlayer2(null)
					.build();
		};
	}

}
