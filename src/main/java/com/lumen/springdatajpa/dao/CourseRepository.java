package com.lumen.springdatajpa.dao;

import com.lumen.springdatajpa.entity.Course;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CourseRepository extends CrudRepository<Course, Integer> {

    @Query("from Course where instructor.id = :id")
    public List<Course> findCoursesByInstructorId(@Param("id") Integer id);
}