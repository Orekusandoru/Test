package com.vitrum.api.repository;

import com.vitrum.api.entity.Course;
import com.vitrum.api.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TopicRepository extends JpaRepository<Topic, Long> {
    Optional<List<Topic>> findByCourse(Course course);
    Optional<Topic> findByNameAndCourse(String name, Course course);
}
