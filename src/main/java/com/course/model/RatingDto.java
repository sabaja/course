package com.course.model;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder(toBuilder = true)
@Getter
public class RatingDto implements Serializable {
    private Double ratingValue;
    private Long courseId;
}

