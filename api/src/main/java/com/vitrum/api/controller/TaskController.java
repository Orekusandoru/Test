package com.vitrum.api.controller;

import com.vitrum.api.dto.Request.TaskRequest;
import com.vitrum.api.dto.Response.TaskResponse;
import com.vitrum.api.entity.Course;
import com.vitrum.api.entity.Topic;
import com.vitrum.api.service.CourseService;
import com.vitrum.api.service.TaskService;
import com.vitrum.api.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/module/task")
@RequiredArgsConstructor
public class TaskController {

    private final TaskService service;
    private final TopicService topicService;
    private final CourseService courseService;

    @PostMapping({"/{courseName}/{topicName}"})
    public ResponseEntity<?> addTaskToTopic(
            @PathVariable String courseName,
            @PathVariable String topicName,
            @RequestBody TaskRequest taskRequest
    ) {
        taskRequest.setCourseName(courseName);
        taskRequest.setTopicName(topicName);
        Course course = courseService.getCourseByName(courseName);
        Topic topic = topicService.getTopicByNameAndCourse(topicName, course);
        try {
            return ResponseEntity.ok(service.createTask(taskRequest, topic));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public List<TaskResponse> getAllTasks() {
        return service.getAllTasks();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTaskById(
            @PathVariable Long id
    ) {
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted");
    }
}
