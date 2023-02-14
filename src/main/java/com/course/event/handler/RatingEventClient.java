package com.course.event.handler;


import com.course.event.RatingEventMessage;
import com.course.model.RatingDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.AsyncRabbitTemplate;
import org.springframework.amqp.rabbit.RabbitConverterFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.concurrent.ExecutionException;


@Slf4j
@Component
public class RatingEventClient {
    @Autowired
    @Qualifier("ratingStatusAsyncRabbitTemplate")
    private AsyncRabbitTemplate ratingStatusAsyncRabbitTemplate;

    @Autowired
    @Qualifier("ratingStatusExchange")
    private DirectExchange directRatingStatusExchange;

    @Value("${rating.status.routing.key}")
    private String routingRatingStatusKey;

    @Autowired
    @Qualifier("ratingUpdateAsyncRabbitTemplate")
    private AsyncRabbitTemplate ratingUpdateAsyncRabbitTemplate;

    @Autowired
    @Qualifier("ratingUpdateExchange")
    private DirectExchange directUpdateStatusExchange;

    @Value("${rating.update.routing.key}")
    private String routingRatingUpdateKey;

    @Scheduled(fixedDelay = 3000, initialDelay = 1500)
    public RatingEventMessage sendRatingStausWithFuture(RatingDto dto) {
        RatingEventMessage ratingEventMessage = new RatingEventMessage();
        log.info("Client starts sending Rating request");

        try {
            RabbitConverterFuture<RatingEventMessage> listenableFuture =
                    ratingStatusAsyncRabbitTemplate.convertSendAndReceiveAsType(
                            directRatingStatusExchange.getName(),
                            routingRatingStatusKey,
                            dto,
                            new ParameterizedTypeReference<>() {
                            });
            // non blocking part

            ratingEventMessage = listenableFuture.get();
            log.info("Client received Rating Message: {}", ratingEventMessage);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Cannot get response.", e);
            Thread.currentThread().interrupt();
        }
        return ratingEventMessage;
    }


    @Scheduled(fixedDelay = 3000, initialDelay = 1500)
    public RatingEventMessage sendRatingUpdateWithFuture(RatingDto dto) {
        RatingEventMessage ratingEventMessage = new RatingEventMessage();
        log.info("Client starts sending Rating request");
        try {
            RabbitConverterFuture<RatingEventMessage> listenableFuture =
                    ratingUpdateAsyncRabbitTemplate.convertSendAndReceiveAsType(
                            directUpdateStatusExchange.getName(),
                            routingRatingUpdateKey,
                            dto,
                            new ParameterizedTypeReference<>() {
                            });
            // non blocking part

            ratingEventMessage = listenableFuture.get();
            log.info("Client received Rating Message: {}", ratingEventMessage);
        } catch (InterruptedException | ExecutionException e) {
            log.error("Cannot get response.", e);
            Thread.currentThread().interrupt();
        }
        return ratingEventMessage;
    }
}