package com.course.service;

import com.course.entities.Course;
import com.course.mapper.CourseMapper;
import com.course.model.CourseDto;
import com.course.repositories.CourseRepository;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseRepository courseRepository;

    @Override
    public CourseDto findCourseById(Long id) {
        if (id != null && id.compareTo(0L) > 0) {
            final Optional<Course> courseResult = courseRepository.findById(id);
            return courseResult.map(courseMapper::courseToCourseDto)
                    .orElseGet(this::createEmptyCourse);
        }
        return createEmptyCourse();
    }

    @Override
    public List<CourseDto> getCourses() {
        final List<Course> courses = courseRepository.findAll();
        if (CollectionUtils.isNotEmpty(courses)) {
            return courses.stream()
                    .filter(Objects::nonNull)
                    .map(courseMapper::courseToCourseDto)
                    .toList();
        }
        return List.of(createEmptyCourse());
    }

    private CourseDto createEmptyCourse() {
        CourseDto courseDto = new CourseDto();
        courseDto.setCourseId(0L);
        courseDto.setCourseDescription(Strings.EMPTY);
        courseDto.setCourseDescription(Strings.EMPTY);
        return courseDto;
    }
}
