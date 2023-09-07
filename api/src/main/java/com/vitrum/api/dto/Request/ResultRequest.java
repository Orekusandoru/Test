package com.vitrum.api.dto.Request;

import com.vitrum.api.entity.Task;
import com.vitrum.api.entity.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ResultRequest {

    private String answer;
    private int score;
    private Task task;
    private User student;
}
