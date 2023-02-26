package com.course.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder(toBuilder = true)
public class CourseBin {
    private Long courseId;
    private String name;
    private String courseDescription;
    private Double ratingValue;
    private int page;
    private int size;
    private String sortBy;
    private Sort sort;
}
