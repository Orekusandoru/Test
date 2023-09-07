package com.vitrum.api.dto.Response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TopicResponse {

    private Long id;
    private String name;
    private String description;
    private String additionalResources;
    private Long courseId;
}
