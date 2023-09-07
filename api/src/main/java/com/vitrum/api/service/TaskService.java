package com.vitrum.api.service;

import com.vitrum.api.dto.Request.TaskRequest;
import com.vitrum.api.dto.Response.TaskResponse;
import com.vitrum.api.entity.Result;
import com.vitrum.api.entity.Task;
import com.vitrum.api.entity.Topic;
import com.vitrum.api.repository.ResultRepository;
import com.vitrum.api.repository.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository repository;
    private final ResultRepository resultRepository;

    public List<TaskResponse> getAllTasks() {
        List<Task> tasks = repository.findAll();
        return tasks.stream().map(this::getTaskResponse).collect(Collectors.toList());
    }

    public Task getTaskByNameAndTopic(String name, Topic topic) {
        Optional<Task> opTask = repository.findByNameAndTopic(name, topic);
        return opTask.orElse(null);
    }

    public TaskResponse createTask(TaskRequest taskRequest, Topic topic) {
        Task existingTask = repository.findByNameAndTopic(taskRequest.getName(), topic).orElse(null);
        if (existingTask != null) {
            throw new IllegalArgumentException("Task with the same name already exists in this topic.");
        }

        LocalDate dueDate = LocalDate.parse(
                taskRequest.getDueDate(),
                DateTimeFormatter.ofPattern("yyyy-MM-dd")
        );
        var task = Task.builder()
                .name(taskRequest.getName())
                .description(taskRequest.getDescription())
                .taskType(taskRequest.getTaskType())
                .maxScore(taskRequest.getMaxScore())
                .dueDate(dueDate)
                .creationDate(LocalDate.now())
                .topic(topic)
                .build();
        repository.save(task);
        return getTaskResponse(task);
    }

    public void deleteById(Long id) {
        Task task = repository.findById(id).orElse(null);
        if (task != null) {
            List<Result> results = task.getResults();
            resultRepository.deleteAll(results);
            repository.delete(task);
        }
    }

    private TaskResponse getTaskResponse(Task task) {
        return TaskResponse.builder()
                .id(task.getId())
                .name(task.getName())
                .description(task.getDescription())
                .maxScore(task.getMaxScore())
                .taskType(task.getTaskType())
                .creationDate(task.getCreationDate())
                .dueDate(task.getDueDate())
                .topicId(task.getTopic().getId())
                .build();
    }

}
