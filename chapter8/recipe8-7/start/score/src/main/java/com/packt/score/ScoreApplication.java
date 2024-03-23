package com.packt.score;

import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ScoreApplication {
	Logger logger = LoggerFactory.getLogger(ScoreApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(ScoreApplication.class, args);
	}

	@Bean
	public Consumer<MatchEvent> processGoals() {
		return value -> {
			logger.info("Processing a goal from player {} at {} ", value.player1(), value.eventTime());
		};
	}

}
