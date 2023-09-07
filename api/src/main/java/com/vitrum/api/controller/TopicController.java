package com.vitrum.api.controller;

import com.vitrum.api.dto.Request.TopicRequest;
import com.vitrum.api.dto.Response.TopicResponse;
import com.vitrum.api.entity.Course;
import com.vitrum.api.service.CourseService;
import com.vitrum.api.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/module/topic")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService service;
    private final CourseService courseService;

    @PostMapping("/{courseName}")
    public ResponseEntity<?> addTopicToCourse(
            @PathVariable String courseName,
            @RequestBody TopicRequest topicRequest
    ) {
        topicRequest.setCourseName(courseName);
        Course course = courseService.getCourseByName(topicRequest.getCourseName());
        try {
            return ResponseEntity.ok(service.createTopic(topicRequest, course));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping
    public List<TopicResponse> getAllTopics() {
        return service.getAllTopics();
    }

    @GetMapping("/{courseName}")
    public List<TopicResponse> getAllTopicsByCourse(
            @PathVariable String courseName
    ) {
        Course course = courseService.getCourseByName(courseName);
        return service.getAllTopicsByCourse(course);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTopicById(
            @PathVariable Long id
    ) {
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted");
    }
}
