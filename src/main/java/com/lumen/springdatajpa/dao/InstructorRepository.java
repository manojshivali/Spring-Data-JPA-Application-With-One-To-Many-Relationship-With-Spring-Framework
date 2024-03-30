package com.lumen.springdatajpa.dao;

import com.lumen.springdatajpa.entity.Course;
import com.lumen.springdatajpa.entity.Instructor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


public interface InstructorRepository extends CrudRepository<Instructor, Integer> {

    @Query("select i from Instructor i JOIN FETCH i.courses JOIN FETCH i.instructorDetail where i.id = :id")
    Instructor findInstructorByIdJoinFetch(@Param("id") Integer id);


}
