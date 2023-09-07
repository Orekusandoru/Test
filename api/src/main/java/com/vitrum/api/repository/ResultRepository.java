package com.vitrum.api.repository;

import com.vitrum.api.entity.Result;
import com.vitrum.api.entity.Task;
import com.vitrum.api.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ResultRepository extends JpaRepository<Result, Long> {

    Optional<List<Result>>  findByStudent(User student);
    Optional<Result> findByIdAndTask(Long id, Task task);
}
