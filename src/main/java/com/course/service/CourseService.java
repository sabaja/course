package com.course.service;

import com.course.model.CourseDto;

import java.util.List;

public interface CourseService {
    CourseDto findCourseById(Long id);

    List<CourseDto> getCourses();

    CourseDto putRatingCourses(CourseDto courseDto, Long id);
}
