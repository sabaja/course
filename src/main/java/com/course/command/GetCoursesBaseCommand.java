package com.course.command;

import com.course.model.CourseBin;
import com.course.model.CourseDto;
import com.course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class GetCoursesBaseCommand extends BaseCommand<List<CourseDto>, CourseBin> {

    @Autowired
    private CourseService courseService;
    private CourseBin bin;

    @Override
    public void init(CourseBin element) {
        this.bin = element;
    }

    @Override
    public List<CourseDto> doExecute() {
        return courseService.getCourses(bin);
    }
}
