package com.vitrum.api.repository;

import com.vitrum.api.entity.Task;
import com.vitrum.api.entity.Topic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TaskRepository extends JpaRepository<Task, Long> {
//    Optional<List<Task>> findByTopic(Topic topic);
    Optional<Task> findByNameAndTopic(String name, Topic topic);
}
