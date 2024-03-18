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
			MatchEvent matchEvent = new MatchEvent();
			matchEvent.setMatchId(1L);
			matchEvent.setType(random.nextInt(0, 10));
			matchEvent.setEventTime(LocalDateTime.now());
			matchEvent.setDescription("random event");
			matchEvent.setPlayer1(null);
			matchEvent.setPlayer2(null);

			MessageBuilder<MatchEvent> messageBuilder = MessageBuilder.withPayload(matchEvent);
			if (matchEvent.getType() == 2) {
				messageBuilder.setHeader("eventType", "football.goals.sample");
			} else {
				messageBuilder.setHeader("eventType", "football.event");
			}
			return messageBuilder.build();
		};
	}

}
