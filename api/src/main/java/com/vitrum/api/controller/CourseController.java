package com.vitrum.api.controller;

import com.vitrum.api.dto.Request.CourseRequest;
import com.vitrum.api.dto.Response.CourseResponse;
import com.vitrum.api.service.CourseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/module/course")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService service;

    @PostMapping
    public ResponseEntity<?> createCourse(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CourseRequest courseRequest
    ) {
        try {
            return ResponseEntity.ok(service.createCourse(courseRequest, userDetails));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/{courseId}/enroll/{userId}")
    public ResponseEntity<?> enrollUserToCourse(
            @PathVariable Long courseId,
            @PathVariable Long userId
    ) {
        try {
            service.enrollUserToCourse(courseId, userId);
            return ResponseEntity.ok("User enrolled in the course successfully.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @GetMapping
    public List<CourseResponse> getAllCourses() {
        return service.getAllCourses();
    }

    @GetMapping("/{name}")
    public ResponseEntity<CourseResponse> getCourseByName(
            @PathVariable("name") String name
    ) {
        CourseResponse courseResponse = service.getCourseResponseByName(name);

        if (courseResponse != null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(courseResponse);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(
            @PathVariable Long id
    ) {
        service.deleteById(id);
        return ResponseEntity.status(HttpStatus.OK).body("Deleted");
    }

}
