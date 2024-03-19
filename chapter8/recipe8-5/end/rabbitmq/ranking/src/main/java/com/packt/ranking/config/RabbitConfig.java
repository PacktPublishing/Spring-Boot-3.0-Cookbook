package com.packt.ranking.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.packt.ranking.service.GoalProcessor;

@Configuration
public class RabbitConfig {

    @Value("${rabbitmq.exchange.name:match-events}")
    private String topicExchangeName;

    @Value("${rabbitmq.queue.name:goals}")
    private String queueName;

    @Bean
    public TopicExchange matchEventsExchange() {
        TopicExchange exchange = new TopicExchange(topicExchangeName, true, false);
        return exchange;
    }

    @Bean
    public Queue goalsQueue() {
        return new Queue(queueName, true);
    }

    @Bean
    public Binding goalsBinding(TopicExchange matchEventsExchange, Queue goalsQueue) {
        return BindingBuilder.bind(goalsQueue).to(matchEventsExchange).with("football.goals.#");
        // return new Binding(queueName, Binding.DestinationType.QUEUE, topicExchangeName, "#", null);
    }

    @Bean
    MessageListenerAdapter listenerAdapter(GoalProcessor goalProcessor) {
        return new MessageListenerAdapter(goalProcessor, "processGoal");
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
            MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        return container;
    }
}
