package com.course.service;

import com.course.model.RatingDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class RatingWebClient {

    private final WebClient webClient;

    @Value("${web.client.rating.update.url}")
    private String updateUrl;

    public RatingWebClient(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .build();
    }

    /* https://www.amitph.com/spring-webclient-read-json-data/ */
    public RatingDto modifyRatingValue(RatingDto dto, Long courseId) {
        log.info("Client starts sending a modifying request to the Rating webClient by course id: {}", dto.getCourseId());
        Mono<RatingDto> ratingDtoMono = webClient.put()
                .uri(updateUrl, courseId)
                .body(Mono.just(dto), RatingDto.class)
                .retrieve()
                .bodyToMono(RatingDto.class).log();

        return ratingDtoMono
                .share()
                .block();
    }
}
