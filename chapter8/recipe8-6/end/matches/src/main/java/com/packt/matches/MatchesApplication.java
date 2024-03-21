package com.packt.matches;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.function.Supplier;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.messaging.Message;

import com.packt.matches.domain.MatchEvent;

@SpringBootApplication
public class MatchesApplication {

	public static void main(String[] args) {
		SpringApplication.run(MatchesApplication.class, args);
	}

	@Bean
	public Supplier<Message<MatchEvent>> matchEvents() {
		Random random = new Random();
		return () -> {
			MatchEvent matchEvent = MatchEvent.builder()
					.withMatchId(1L)
					.withType(random.nextInt(0, 10))
					.withEventTime(LocalDateTime.now())
					.withDescription("random event")
					.withPlayer1(random.nextLong(1000, 2000))
					.withPlayer2(random.nextLong(1000, 2000))
					.build();

			MessageBuilder<MatchEvent> messageBuilder = MessageBuilder.withPayload(matchEvent);
			if (matchEvent.type() == 2) {
				messageBuilder.setHeader("eventType", "football.goal");
			} else {
				messageBuilder.setHeader("eventType", "football.event");
			}
			return messageBuilder.build();
		};
	}

}
