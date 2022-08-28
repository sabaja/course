package com.course.event;


import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * Event received when a rating has been added to the course.
 */
@RequiredArgsConstructor
@Getter
@ToString
@EqualsAndHashCode
public class RatingEventMessage implements Serializable {

    private Double ratingValue;
    private Long courseId;
}
