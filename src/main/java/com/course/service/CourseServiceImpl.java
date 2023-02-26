package com.course.service;

import com.course.entities.Course;
import com.course.event.RatingEventMessage;
import com.course.event.handler.RatingEventClient;
import com.course.mapper.CourseMapper;
import com.course.model.CourseBin;
import com.course.model.CourseDto;
import com.course.model.RatingDto;
import com.course.repositories.CourseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class CourseServiceImpl implements CourseService {

    private static final String ID = "id";
    private final CourseMapper courseMapper;
    private final CourseRepository courseRepository;
    private final RatingEventClient ratingEventClient;
    private final RatingWebClient ratingWebClient;

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
    public List<CourseDto> getCourses(CourseBin bin) {
        Page<Course> pageCourses;
        Pageable paging = PageRequest.of(bin.getPage(), bin.getSize(), createSort(bin));
        pageCourses = courseRepository.findAll(paging);
        return createCourseDtos(pageCourses);
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

    private Sort createSort(CourseBin bin) {
        final String sortBy = bin.getSortBy();
        com.course.model.Sort sort = bin.getSort();

        if (StringUtils.isNotBlank(sortBy)) {
            return sort.equals(com.course.model.Sort.DESC) ? Sort.by(sortBy).descending() : Sort.by(sortBy);
        }
        return sort.equals(com.course.model.Sort.DESC) ? Sort.by(ID).descending() : Sort.by(ID);
    }

    private List<CourseDto> createCourseDtos(Page<Course> pageCourses) {
        return Optional.ofNullable(pageCourses)
                .map(Slice::getContent)
                .stream()
                .filter(Objects::nonNull)
                .toList()
                .stream()
                .flatMap(courses -> courses.stream()
                        .map(courseMapper::courseToCourseDto)
                        .map(this::mappingRatingValue)
                ).toList();
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
                .map(message -> createRatingDtoByRatingEventMessage(message, id))
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
        final RatingEventMessage ratingEventMessage = ratingEventClient.sendRatingStatusWithFuture(createRatingEvent(courseId));
        courseDto.setRatingValue(ratingEventMessage != null ? ratingEventMessage.getRatingValue() : null);
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
