package com.vitrum.api.controller;

import com.vitrum.api.dto.Request.ResultRequest;
import com.vitrum.api.dto.Response.ResultResponse;
import com.vitrum.api.entity.*;
import com.vitrum.api.service.CourseService;
import com.vitrum.api.service.ResultService;
import com.vitrum.api.service.TaskService;
import com.vitrum.api.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/module/result")
@RequiredArgsConstructor
public class ResultController {

    private final ResultService service;
    private final TaskService taskService;
    private final TopicService topicService;
    private final CourseService courseService;

    @PostMapping("/{courseName}/{topicName}/{taskName}")
    public ResponseEntity<ResultResponse> addAnswerToTask(
            @PathVariable String courseName,
            @PathVariable String topicName,
            @PathVariable String taskName,
            @RequestBody ResultRequest resultRequest,
            @AuthenticationPrincipal UserDetails user
    ) {
        Task task = getTaskByPath(courseName, topicName, taskName);
        resultRequest.setTask(task);
        resultRequest.setStudent((User) user);
        return ResponseEntity.ok(service.addAnswerToTask(resultRequest));
    }

    @GetMapping
    public List<ResultResponse> getAllUserResults(
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        return service.getAllUserResults((User) userDetails);
    }

    @PutMapping("/addScore/{courseName}/{topicName}/{taskName}/{id}")
    public ResponseEntity<?> addScoreToResult(
            @PathVariable String courseName,
            @PathVariable String topicName,
            @PathVariable String taskName,
            @PathVariable Long id,
            @RequestBody ResultRequest resultRequest
    ) {
        Result result = getResultByPath(courseName, topicName, taskName, id);
        service.addScoreToResult(result, resultRequest.getScore());
        return ResponseEntity.status(HttpStatus.OK).body("Updated");
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteResultById(
            @PathVariable Long id
    ) {
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted");
    }

    private Result getResultByPath(String courseName, String topicName, String taskName, Long id) {
        Task task = getTaskByPath(courseName, topicName, taskName);
        return service.getResultByIdAndTask(id, task);
    }

    private Task getTaskByPath(String courseName, String topicName, String taskName) {
        Course course = courseService.getCourseByName(courseName);
        Topic topic = topicService.getTopicByNameAndCourse(topicName, course);
        return taskService.getTaskByNameAndTopic(taskName, topic);
    }
}
