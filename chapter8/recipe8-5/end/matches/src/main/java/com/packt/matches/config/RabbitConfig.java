package com.packt.matches.config;

import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    
    @Value("${rabbitmq.exchange.name:match-events}")
    String topicExchangeName;

    @Bean
    public TopicExchange matchEventsExchange() {
        TopicExchange exchange = new TopicExchange(topicExchangeName, true, false);
        return exchange;
    }
}
