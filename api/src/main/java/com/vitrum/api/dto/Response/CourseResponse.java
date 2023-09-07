package com.vitrum.api.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CourseResponse {

    private Long id;
    private Long userId;
    private String name;
    private String description;
    private List<TopicResponse> topics = new ArrayList<>();
    private List<UserProfileResponse> students = new ArrayList<>();
}
