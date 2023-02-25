package com.course.command;

import com.course.model.CourseDto;
import com.course.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
@RequiredArgsConstructor
public class GetCoursesBaseCommand extends BaseCommand<CourseDto> {

    final private CourseService courseService;
    final private CourseDto courseDto;


    @Override
    protected CourseDto doExecute() {
        return null;
    }
}
