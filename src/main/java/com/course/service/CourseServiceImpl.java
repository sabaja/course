package com.course.service;

import com.course.entities.Course;
import com.course.event.RatingEventMessage;
import com.course.event.handler.RatingEventClient;
import com.course.mapper.CourseMapper;
import com.course.model.CourseDto;
import com.course.model.RatingDto;
import com.course.repositories.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@Service
public class CourseServiceImpl implements CourseService {

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private CourseRepository courseRepository;
    @Autowired
    private RatingEventClient ratingEventClient;

    @Autowired
    private RatingWebClient ratingWebClient;

    @Override
    public CourseDto findCourseById(Long id) {
        if (id != null && id.compareTo(0L) > 0) {
            final Optional<Course> courseResult = retreiveCourseFromDB(id);
            return courseResult.map(courseMapper::courseToCourseDto)
                    .map(this::mappingRatingValue)
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
                    .map(this::mappingRatingValue)
                    .toList();
        }
        return List.of(createEmptyCourse());
    }

    @Override
    public CourseDto putRatingCourse(CourseDto courseDto, Long id) {
        if (isCourseRatingRequestConsistent(courseDto, id)) {
            return modifyRatingCourse(courseDto, id);
        }
        return createEmptyCourse();
    }

    @Override
    public CourseDto putRatingEventCourse(CourseDto courseDto, Long id) {
        if (isCourseRatingRequestConsistent(courseDto, id)) {
            return modifyRatingCourse(courseDto, id, Boolean.TRUE);
        }
        return createEmptyCourse();
    }

    private CourseDto modifyRatingCourse(CourseDto courseDto, Long id) {
        return modifyRatingCourse(courseDto, id, false);
    }

    private CourseDto modifyRatingCourse(CourseDto courseDto, Long id, Boolean isEvent) {
        RatingDto ratingDto = Boolean.FALSE.compareTo(isEvent) == 0 ? modifyRatingDtoByWebClient(courseDto, id) :
                modifyRatingDtoByEventClient(courseDto, id);
        courseDto.setRatingValue(ratingDto.getRatingValue());
        return courseDto;
    }

    private RatingDto modifyRatingDtoByWebClient(CourseDto courseDto, Long id) {
        log.info("[WEBCLIENT] Updating rating of courseId: [{}] with value: [{}]", id, courseDto.getRatingValue());
        return Optional.ofNullable(this.ratingWebClient.modifyRatingValue(createRatingDto(courseDto), id))
                .orElseGet(() -> RatingDto.builder().build());
    }

    private RatingDto modifyRatingDtoByEventClient(CourseDto courseDto, Long id) {
        log.info("[EVENTCLIENT] Updating rating of courseId: [{}] with value: [{}]", id, courseDto.getRatingValue());
        return Optional.ofNullable(this.ratingEventClient.sendRatingUpdateWithFuture(createRatingDto(courseDto)))
                .map((message) -> createRatingDtoByRatingEventMessage(message, id))
                .orElseGet(() -> RatingDto.builder().build());
    }

    private RatingDto createRatingDtoByRatingEventMessage(RatingEventMessage ratingEventMessage, Long id) {
        return RatingDto.builder()
                .courseId(id)
                .ratingValue(ratingEventMessage.getRatingValue())
                .build();
    }

    private RatingDto createRatingDto(CourseDto courseDto) {
        return RatingDto.builder()
                .ratingValue(courseDto.getRatingValue())
                .courseId(courseDto.getCourseId())
                .build();
    }

    private Optional<Course> retreiveCourseFromDB(Long id) {
        return courseRepository.findById(id);
    }

    private boolean isCourseRatingRequestConsistent(CourseDto courseDto, Long id) {
        return Optional.ofNullable(courseDto).map(CourseDto::getCourseId).orElseGet(() -> -1L).compareTo(id != null ? id : 0) == 0;
    }

    private CourseDto mappingRatingValue(CourseDto courseDto) {
        final Long courseId = courseDto.getCourseId();
        final RatingEventMessage ratingEventMessage = ratingEventClient.sendRatingStausWithFuture(createRatingEvent(courseId));
        courseDto.setRatingValue(ratingEventMessage.getRatingValue());
        return courseDto;
    }

    private RatingDto createRatingEvent(Long courseId) {
        return RatingDto.builder()
                .courseId(courseId)
                .build();
    }

    private CourseDto createEmptyCourse() {
        CourseDto courseDto = new CourseDto();
        courseDto.setCourseId(0L);
        courseDto.setCourseDescription(Strings.EMPTY);
        courseDto.setCourseDescription(Strings.EMPTY);
        return courseDto;
    }
}
