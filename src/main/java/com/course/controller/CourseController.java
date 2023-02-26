package com.course.controller;

import com.course.command.GetCoursesBaseCommand;
import com.course.model.CourseBin;
import com.course.model.CourseDto;
import com.course.model.Sort;
import com.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("courses")
public class CourseController {

    //    https://fullstackdeveloper.guru/2022/04/20/how-to-do-server-side-load-balancing-using-spring-cloud-gateway-and-netflix-eureka/

    private final Environment environment;

    private final CourseService courseService;
    private final GetCoursesBaseCommand getCoursesBaseCommand;

    @GetMapping("/{id}")
    public ResponseEntity<CourseDto> getCourse(@PathVariable Long id) {
        return new ResponseEntity<>(courseService.findCourseById(id), HttpStatus.OK);
    }

    //    https://www.bezkoder.com/spring-boot-pagination-filter-jpa-pageable/
    @GetMapping()
    public ResponseEntity<List<CourseDto>> getPagingCourses(@RequestParam(name = "page", required = false, defaultValue = "0") int page,
                                                            @RequestParam(name = "size", required = false, defaultValue = "10") int size,
                                                            @RequestParam(name = "sortedBy", required = false) String sortedBy,
                                                            @RequestParam(name = "sort", required = false, defaultValue = "ASC") Sort sort) {
        CourseBin courseBin = CourseBin.builder()
                .page(page)
                .size(size)
                .sortBy(sortedBy)
                .sort(sort)
                .build();
        getCoursesBaseCommand.init(courseBin);
        final List<CourseDto> courseDtos = getCoursesBaseCommand.doExecute();
        return new ResponseEntity<>(courseDtos, HttpStatus.OK);
    }

    @PutMapping("/{id}/rating")
    public ResponseEntity<CourseDto> putRatingCourse(@RequestBody CourseDto courseDto, @PathVariable Long id) {
        var port = environment.getProperty("local.server.port");
        log.info("Running on port: [{}]", port);
        return new ResponseEntity<>(courseService.putRatingCourse(courseDto, id), HttpStatus.OK);
    }

    @PutMapping("/{id}/rating-event")
    public ResponseEntity<CourseDto> putRatingEventCourse(@RequestBody CourseDto courseDto, @PathVariable Long id) {
        var port = environment.getProperty("local.server.port");
        log.info("Running on port: [{}]", port);
        return new ResponseEntity<>(courseService.putRatingEventCourse(courseDto, id), HttpStatus.OK);
    }
}
