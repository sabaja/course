package com.course.event.handler;


import com.course.event.RatingEventMessage;
import com.course.model.RatingDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;


@Slf4j
@Component
public class RatingEventClient {

    private final AsyncRabbitTemplate asyncRabbitTemplate;

    private final DirectExchange directExchange;

    @Value("${rating.routing.key}")
    private String routingKey;

    public RatingEventClient(AsyncRabbitTemplate asyncRabbitTemplate, DirectExchange directExchange) {
        this.asyncRabbitTemplate = asyncRabbitTemplate;
        this.directExchange = directExchange;
    }

    @Scheduled(fixedDelay = 3000, initialDelay = 1500)
    public RatingEventMessage sendWithFuture(RatingDto dto) {
        RatingEventMessage ratingEventMessage = new RatingEventMessage();
        log.info("Client starts sending Rating request");
        ListenableFuture<RatingEventMessage> listenableFuture =
                asyncRabbitTemplate.convertSendAndReceiveAsType(
                        directExchange.getName(),
                        routingKey,
                        dto,
                        new ParameterizedTypeReference<>() {
                        });
        // non blocking part
        try {
            ratingEventMessage = listenableFuture.get();
            log.info("Client received Rating Message: {}", ratingEventMessage);
        } catch (ExecutionException | InterruptedException e) {
            log.error("Cannot get response.", e);
        }
        return ratingEventMessage;
    }
}