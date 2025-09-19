/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.example.graderbackend.config.RabbitMQConfig
 *  org.springframework.amqp.core.AmqpAdmin
 *  org.springframework.amqp.core.Binding
 *  org.springframework.amqp.core.BindingBuilder
 *  org.springframework.amqp.core.Exchange
 *  org.springframework.amqp.core.Queue
 *  org.springframework.amqp.core.TopicExchange
 *  org.springframework.amqp.rabbit.connection.ConnectionFactory
 *  org.springframework.amqp.rabbit.core.RabbitAdmin
 *  org.springframework.amqp.rabbit.core.RabbitTemplate
 *  org.springframework.amqp.support.converter.Jackson2JsonMessageConverter
 *  org.springframework.amqp.support.converter.MessageConverter
 *  org.springframework.beans.factory.annotation.Value
 *  org.springframework.context.annotation.Bean
 *  org.springframework.context.annotation.Configuration
 */
package com.example.graderbackend.config;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Exchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {
    @Value(value="${rabbitmq.queue.name}")
    private String submissionQueueName;
    @Value(value="${rabbitmq.queue.result}")
    private String resultQueueName;
    @Value(value="${rabbitmq.exchange.name}")
    private String exchangeName;
    @Value(value="${rabbitmq.routing.key}")
    private String submissionRoutingKey;

    @Bean
    public Queue submissionQueue() {
        return new Queue(this.submissionQueueName, true);
    }

    @Bean
    public Queue resultQueue() {
        return new Queue(this.resultQueueName, true);
    }

    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(this.exchangeName, true, false);
    }

    @Bean
    public Binding submissionBinding() {
        return BindingBuilder.bind((Queue)this.submissionQueue()).to(this.exchange()).with(this.submissionRoutingKey);
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter((MessageConverter)this.jackson2JsonMessageConverter());
        return template;
    }

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        RabbitAdmin admin = new RabbitAdmin(connectionFactory);
        admin.declareQueue(this.submissionQueue());
        admin.declareQueue(this.resultQueue());
        admin.declareExchange((Exchange)this.exchange());
        admin.declareBinding(this.submissionBinding());
        return admin;
    }
}

