package com.vitrum.api.service;

import com.vitrum.api.dto.Request.CourseRequest;
import com.vitrum.api.dto.Response.CourseResponse;
import com.vitrum.api.dto.Response.TopicResponse;
import com.vitrum.api.dto.Response.UserProfileResponse;
import com.vitrum.api.entity.Course;
import com.vitrum.api.entity.Topic;
import com.vitrum.api.entity.User;
import com.vitrum.api.repository.CourseRepository;
import com.vitrum.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseService {

    private final CourseRepository repository;
    private final TopicService topicService;
    private final UserRepository userRepository;

    public List<CourseResponse> getAllCourses() {
        List<Course> courses = repository.findAll();

        return courses.stream().map(course -> getCourseResponse(course, getTopicResponses(course))).collect(Collectors.toList());
    }

    public CourseResponse getCourseResponseByName(String name) {
        Optional<Course> optionalCourse = repository.findByName(name);

        if (optionalCourse.isPresent()) {
            Course course = optionalCourse.get();
            List<TopicResponse> topicResponses = getTopicResponses(course);

            return getCourseResponse(course, topicResponses);
        }

        return null;
    }

    public Course getCourseByName(String name) {
        Optional<Course> optionalCourse = repository.findByName(name);
        return optionalCourse.orElse(null);
    }

    public CourseResponse createCourse(CourseRequest courseRequest, UserDetails userDetails) {
        Course existingCourse = repository.findByName(courseRequest.getName()).orElse(null);
        if (existingCourse != null) {
            throw new IllegalArgumentException("Course with the same name already exists.");
        }
        var course = Course.builder()
                .name(courseRequest.getName())
                .description(courseRequest.getDescription())
                .teacher((User) userDetails)
                .build();
        repository.save(course);
        return CourseResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .userId(course.getTeacher().getId())
                .build();
    }

    public void deleteById(Long id) {
        Course course = repository.findById(id).orElse(null);
        if (course != null) {
            List<Topic> topics = course.getTopics();
            for (Topic topic : topics) {
                topicService.deleteById(topic.getId());
            }
            repository.delete(course);
        }
    }

    public void enrollUserToCourse(Long courseId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Course course = repository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Course not found"));

        if (!course.getStudents().contains(user)) {
            course.addStudent(user);
            repository.save(course);
        }
    }


    private CourseResponse getCourseResponse(Course course, List<TopicResponse> topicResponses) {
        return CourseResponse.builder()
                .id(course.getId())
                .name(course.getName())
                .description(course.getDescription())
                .userId(course.getTeacher().getId())
                .topics(topicResponses)
                .students(getUserProfileResponse(course))
                .build();
    }

    private List<TopicResponse> getTopicResponses(Course course) {
        List<Topic> topics = course.getTopics();
        return topics.stream()
                .map(this::mapTopicToTopicResponse)
                .collect(Collectors.toList());
    }

    private List<UserProfileResponse> getUserProfileResponse(Course course) {
        List<User> students = course.getStudents();
        return students.stream()
                .map(this::mapUserToUserProfileResponse)
                .collect(Collectors.toList());
    }

    private UserProfileResponse mapUserToUserProfileResponse(User user) {
        return UserProfileResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    private TopicResponse mapTopicToTopicResponse(Topic topic) {
        return TopicResponse.builder()
                .id(topic.getId())
                .name(topic.getName())
                .description(topic.getDescription())
                .additionalResources(topic.getAdditionalResources())
                .courseId(topic.getCourse().getId())
                .build();
    }
}
