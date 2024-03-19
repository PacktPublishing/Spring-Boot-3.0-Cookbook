package com.packt.ranking.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.packt.ranking.domain.MatchEvent;

@Service
public class GoalProcessor {

    private final ObjectMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(GoalProcessor.class);

    public GoalProcessor() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new Jdk8Module());
    }

    public void processGoal(String message) throws JsonMappingException, JsonProcessingException {
        MatchEvent matchEvent = mapper.readValue(message, MatchEvent.class);
        logger.info("Received goal event: {}", matchEvent);
    }

}
