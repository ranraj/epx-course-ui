package com.cisco.epx.course.app.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.cisco.epx.course.app.entity.Course;

@Repository
public interface CourseRepository extends CrudRepository<Course, Long> {
    
    List<Course> findByName(String name);
    
}
