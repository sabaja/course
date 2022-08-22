package com.course.model;

import lombok.Data;

@Data
public class CourseDto {
    private Long courseId;

    private String name;

    private String courseDescription;
}
