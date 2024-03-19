package com.packt.matches.service;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.packt.matches.config.RabbitConfig;
import com.packt.matches.domain.MatchEvent;

@Service
public class MatchService {

    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;
    private String topicExchangeName;

    public MatchService(RabbitTemplate rabbitTemplate,
            @Value("${rabbitmq.exchange.name:match-events}") String topicExchangeName) {
        this.rabbitTemplate = rabbitTemplate;
        this.topicExchangeName = topicExchangeName;
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
    }

    public void createEvent(MatchEvent matchEvent) throws JsonProcessingException {

        String serializedMatchEvent = objectMapper.writeValueAsString(matchEvent);
        if (matchEvent.getType() == 2) {
            rabbitTemplate.convertAndSend(topicExchangeName, "football.goals.sample", serializedMatchEvent);
        } else {
            rabbitTemplate.convertAndSend(topicExchangeName, "football", serializedMatchEvent);
        }
    }

}
