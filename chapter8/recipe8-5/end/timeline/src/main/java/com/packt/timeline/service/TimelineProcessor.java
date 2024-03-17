package com.packt.timeline.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.packt.timeline.domain.MatchEvent;

@Service
public class TimelineProcessor {

    private final ObjectMapper mapper;
    private static final Logger logger = LoggerFactory.getLogger(TimelineProcessor.class);

    public TimelineProcessor() {
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new Jdk8Module());
    }

    public void processEvent(String message) throws JsonMappingException, JsonProcessingException {
        MatchEvent matchEvent = mapper.readValue(message, MatchEvent.class);
        logger.info("Received event: {}", matchEvent);
    }

}
