package com.packt.matches.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Service;

import com.packt.matches.domain.MatchEvent;

import reactor.core.publisher.Mono;

@Service
public class MatchService {

    private StreamBridge streamBridge;
    private final String bindingName;

    public MatchService(StreamBridge streamBridge,
            @Value("${spring.cloud.stream.bindings.matchEvents.destination}") String bindingName) {
        this.streamBridge = streamBridge;
        this.bindingName = bindingName;
    }

    public Mono<MatchEvent> createEvent(MatchEvent matchEvent) {
        MessageBuilder<MatchEvent> messageBuilder = MessageBuilder.withPayload(matchEvent);
        messageBuilder.setHeader("eventType", matchEvent.getType());
        if (matchEvent.getType() == 2) {
            messageBuilder.setHeader("bindingRoutingKey", "'goals.#'");
        }
        Message<MatchEvent> message = messageBuilder.build();
        if (streamBridge.send(bindingName, message)) {
            // if (streamBridge.send("matchevents", matchEvent)) {
            return Mono.just(matchEvent);
        } else {
            return Mono.empty();
        }
    }
}
