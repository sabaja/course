package com.course.repositories;

import com.course.entities.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Long> {

    Page<Course> findByNameContaining(String name, Pageable paging);

    @Query("select c from Course c where upper(c.description) like upper(?1)")
    Optional<Course> findByDescriptionLikeIgnoreCase(String description);
}