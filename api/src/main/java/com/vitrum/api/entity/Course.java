package com.vitrum.api.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "course")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;
    private String description;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @ManyToMany(mappedBy = "courses")
    private List<User> students = new ArrayList<>();

    @OneToMany(mappedBy = "course")
    private List<Topic> topics = new ArrayList<>();

    public void addStudent(User student) {
        students.add(student);
        student.getCourses().add(this);
    }

    public void removeStudent(User student) {
        students.remove(student);
        student.getCourses().remove(this);
    }

    public void enrollInCourse(User student) {
        students.add(student);
        student.getCourses().add(this);
    }

}
