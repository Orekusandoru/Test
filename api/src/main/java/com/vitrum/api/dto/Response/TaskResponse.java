package com.vitrum.api.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TaskResponse {

    private Long id;
    private String name;
    private String description;
    private LocalDate creationDate;
    private LocalDate dueDate;
    private String taskType;
    private int maxScore;
    private Long topicId;
}
