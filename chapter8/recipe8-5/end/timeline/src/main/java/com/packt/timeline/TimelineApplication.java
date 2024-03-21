package com.packt.timeline;

import java.util.function.Consumer;
import java.util.function.Function;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import com.packt.timeline.domain.MatchEvent;

@SpringBootApplication
public class TimelineApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimelineApplication.class, args);
	}

	@Bean
	public Consumer<MatchEvent> processMatchEvent() {
		return value -> {
			System.out.println("Processing MatchEvent: " + value.type());
		};
	}

	
	// public void handle(MatchEvent matchEvent) {
	// 	System.out.println("Received: " + matchEvent);
	// }

}
