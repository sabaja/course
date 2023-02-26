package com.course.mapper;

import com.course.entities.Course;
import com.course.model.CourseBin;
import com.course.model.CourseDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(target = "courseId", source = "id")
    @Mapping(target = "courseDescription", source = "description")
    @Mapping(target = "ratingValue", ignore = true)
    CourseDto courseToCourseDto(Course course);


    CourseBin courseDtoToCourseBin(CourseDto dto);
}
