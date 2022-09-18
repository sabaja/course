package com.course.service;

import com.course.entities.Course;
import com.course.event.RatingEventMessage;
import com.course.event.handler.RatingEventClient;
import com.course.mapper.CourseMapper;
import com.course.model.CourseDto;
import com.course.repositories.CourseRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @InjectMocks
    private CourseServiceImpl service;

    @Mock
    private CourseRepository repository;

    @Mock
    private RatingEventClient ratingEventClient;

    @Mock
    private CourseMapper courseMapper = Mappers.getMapper(CourseMapper.class);

    @Test
    void findCourseById_shouldReturnCourse() {
        final Course course = createCourse();
        when(repository.findById(anyLong())).thenReturn(Optional.of(course));
        final CourseDto dto = createCourseDto();
        when(courseMapper.courseToCourseDto(any())).thenReturn(dto);
        when(ratingEventClient.sendRatingStausWithFuture(any())).thenReturn(creaRatingEventMessage());

        final CourseDto courseDto = service.findCourseById(1L);
        verify(repository, times(1)).findById(anyLong());
        verify(courseMapper, times(1)).courseToCourseDto(any());
        verify(ratingEventClient, times(1)).sendRatingStausWithFuture(any());
        assertNotNull(courseDto);
    }

    @Test
    void getCourses_shouldReturnCourses() {
        final List<Course> courses = List.of(createCourse());
        when(repository.findAll()).thenReturn(courses);
        final CourseDto dto = createCourseDto();
        when(courseMapper.courseToCourseDto(any())).thenReturn(dto);
        when(ratingEventClient.sendRatingStausWithFuture(any())).thenReturn(creaRatingEventMessage());
        final List<CourseDto> courseDtos = service.getCourses();
        verify(repository, times(1)).findAll();
        verify(courseMapper, times(1)).courseToCourseDto(any());
        verify(ratingEventClient, times(1)).sendRatingStausWithFuture(any());
        assertNotNull(courseDtos);
    }

    private Course createCourse() {
        final Course course = new Course();
        course.setId(1L);
        course.setName("name");
        course.setDescription("description");
        return course;
    }

    private RatingEventMessage creaRatingEventMessage() {
        RatingEventMessage ratingEventMessage = new RatingEventMessage();
        ratingEventMessage.setRatingValue(0.0D);
        ratingEventMessage.setCourseId(0L);
        return ratingEventMessage;
    }

    private CourseDto createCourseDto() {
        final CourseDto courseDto = new CourseDto();
        courseDto.setCourseId(1L);
        courseDto.setName("name");
        courseDto.setCourseDescription("description");

        return courseDto;

    }
}