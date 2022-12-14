package com.course.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
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

   /*
      https://stackoverflow.com/questions/53706538/spring-amqp-rabbitmq-rpc-priority-queue
      https://www.javainuse.com/messaging/rabbitmq/exchange
    */


    @Value("${rating.status.exchange}")
    private String ratingStatusExchange;

    @Value("${rating.update.exchange}")
    private String ratingUpdateExchange;

    @Value("${rating.status.queue}")
    private String ratingStatusQueue;

    @Value("${rating.update.queue}")
    private String ratingUpdateQueue;

    @Value("${rating.status.routing.key}")
    private String ratingStatusRoutingKey;

    @Value("${rating.update.routing.key}")
    private String ratingUpdateRoutingKey;

    @Autowired
    private ConnectionFactory connectionFactory;

    @Bean("ratingStatusExchange")
    public DirectExchange directRatingStatusExchange() {
        return new DirectExchange(ratingStatusExchange);
    }

    @Bean("ratingUpdateExchange")
    public DirectExchange directRatingUpdateExchange() {
        return new DirectExchange(ratingUpdateExchange);
    }

    @Bean
    public MessageConverter jackson2MessageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate ratingStatusTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setRoutingKey(ratingStatusRoutingKey);
        rabbitTemplate.setMessageConverter(jackson2MessageConverter());
        rabbitTemplate.setExchange(directRatingStatusExchange().getName());
        return rabbitTemplate;
    }

    @Bean
    public RabbitTemplate ratingUpdateTemplate() {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setRoutingKey(ratingUpdateRoutingKey);
        rabbitTemplate.setMessageConverter(jackson2MessageConverter());
        rabbitTemplate.setExchange(directRatingUpdateExchange().getName());
        return rabbitTemplate;
    }

    @Bean(name = "ratingStatusAsyncRabbitTemplate")
    public AsyncRabbitTemplate ratingStatusAsyncRabbitTemplate() {
        return new AsyncRabbitTemplate(ratingStatusTemplate());
    }

    @Bean(name = "ratingUpdateAsyncRabbitTemplate")
    public AsyncRabbitTemplate ratingUpdateAsyncRabbitTemplate() {
        return new AsyncRabbitTemplate(ratingUpdateTemplate());
    }

    @Bean
    Queue statusQueue() {
        return new Queue(ratingStatusQueue);
    }

    @Bean
    Queue updateQueue() {
        return new Queue(ratingUpdateQueue);
    }

    @Bean
    Binding statusBinding() {
        return BindingBuilder.bind(statusQueue()).to(directRatingStatusExchange()).with(ratingStatusRoutingKey);
    }

    @Bean
    Binding updateBinding() {
        return BindingBuilder.bind(updateQueue()).to(directRatingUpdateExchange()).with(ratingUpdateRoutingKey);
    }
}