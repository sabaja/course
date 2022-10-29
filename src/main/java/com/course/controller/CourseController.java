package com.course.controller;

import com.course.model.CourseDto;
import com.course.service.CourseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("courses")
public class CourseController {

    //    https://fullstackdeveloper.guru/2022/04/20/how-to-do-server-side-load-balancing-using-spring-cloud-gateway-and-netflix-eureka/
    @Autowired
    private Environment environment;

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
        var port = environment.getProperty("local.server.port");
        log.info("Running on port: [{}]", port);
        return new ResponseEntity<>(courseService.putRatingCourse(courseDto, id), HttpStatus.OK);
    }

    @PutMapping("/course/rating-event/{id}")
    public ResponseEntity<CourseDto> putRatingEventCourse(@RequestBody CourseDto courseDto, @PathVariable Long id) {
        var port = environment.getProperty("local.server.port");
        log.info("Running on port: [{}]", port);
        return new ResponseEntity<>(courseService.putRatingEventCourse(courseDto, id), HttpStatus.OK);
    }
}
