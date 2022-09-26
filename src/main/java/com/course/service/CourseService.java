package com.course.service;

import com.course.model.CourseDto;

import java.util.List;

public interface CourseService {
    CourseDto findCourseById(Long id);

    List<CourseDto> getCourses();

    CourseDto putRatingCourse(CourseDto courseDto, Long id);

    CourseDto putRatingEventCourse(CourseDto courseDto, Long id);
}
