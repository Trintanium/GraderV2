package com.example.graderworkerapplication.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String SUBMISSION_QUEUE = "submissionQueue";
    public static final String RESULT_QUEUE = "resultQueue";
    public static final String EXCHANGE = "graderExchange";
    public static final String ROUTING_KEY = "submissionRoutingKey";

    @Bean
    public Queue submissionQueue() {
        return new Queue(SUBMISSION_QUEUE, true);
    }

    @Bean
    public Queue resultQueue() {
        return new Queue(RESULT_QUEUE, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE);
    }

    @Bean
    public Binding submissionBinding() {
        return BindingBuilder.bind(submissionQueue())
                .to(exchange())
                .with(ROUTING_KEY);
    }

    // JSON converter for sending/receiving messages
    @Bean
    public Jackson2JsonMessageConverter messageConverter() {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter();
        // Optional: ignore type headers to avoid package mismatches
        converter.setAlwaysConvertToInferredType(true);
        return converter;
    }

    // Configure RabbitTemplate to use the Jackson converter
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(messageConverter());
        return template;
    }
}