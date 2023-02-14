package com.course.mapper;

import com.course.entities.Course;
import com.course.model.CourseDto;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CourseMapperTest {

    private static final long ID = 1L;
    private static final String NAME = "name";
    private static final String DESCRIPTION = "description";
    private final CourseMapper courseMapper = Mappers.getMapper(CourseMapper.class);

    @Test
    void courseToCourseDto() {
        Course course = createCourse();
        final CourseDto courseDto = courseMapper.courseToCourseDto(course);
        assertNotNull(courseDto);
        assertEquals(ID, courseDto.getCourseId());
        assertEquals(NAME, courseDto.getName());
        assertEquals(DESCRIPTION, courseDto.getCourseDescription());
    }

    private Course createCourse() {
        final Course course = new Course();
        course.setId(ID);
        course.setName(NAME);
        course.setDescription(DESCRIPTION);
        return course;
    }
}