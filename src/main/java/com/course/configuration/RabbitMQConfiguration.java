package com.course.configuration;

import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    /* https://stackoverflow.com/questions/53706538/spring-amqp-rabbitmq-rpc-priority-queue */

    @Value("${rating.status.exchange}")
    private String ratingStatusExchange;

    @Value("${rating.status.queue}")
    private String ratingStatusQueue;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange(ratingStatusExchange);
    }

    @Bean
    public MessageConverter jackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate ratingStatusTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setRoutingKey(queueStatus().getName());
        rabbitTemplate.setMessageConverter(jackson2MessageConverter());
        return rabbitTemplate;
    }
    @Bean(name = "ratingStatusAsyncRabbitTemplate")
    public AsyncRabbitTemplate ratingStatusAsyncRabbitTemplate() {
        return new AsyncRabbitTemplate(ratingStatusTemplate());
    }

    @Bean
    Queue queueStatus() {
        return new Queue(ratingStatusQueue);
    }
}