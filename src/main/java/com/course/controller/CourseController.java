package com.course.controller;

import com.course.model.CourseDto;
import com.course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable Long id) {
        return new ResponseEntity<>(courseService.findCourseById(id), HttpStatus.OK);
    }

    @GetMapping("/list")
    public ResponseEntity<List<CourseDto>> getCourses() {
        return new ResponseEntity<>(courseService.getCourses(), HttpStatus.OK);
    }

    @PutMapping("/course/rating/{id}")
    public ResponseEntity<CourseDto> putRatingCourse(@RequestBody CourseDto courseDto, @PathVariable Long id) {
        return new ResponseEntity<>(courseService.putRatingCourses(courseDto, id), HttpStatus.OK);
    }
}
