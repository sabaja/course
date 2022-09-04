package com.course.event;


import lombok.*;

import java.io.Serializable;

/**
 * Event received when a rating has been added to the course.
 */
@RequiredArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class RatingEventMessage implements Serializable {

    private Double ratingValue;
    private Long courseId;
}
