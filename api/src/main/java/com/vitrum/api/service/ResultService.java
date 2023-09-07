package com.vitrum.api.service;

import com.vitrum.api.dto.Request.ResultRequest;
import com.vitrum.api.dto.Response.ResultResponse;
import com.vitrum.api.entity.Result;
import com.vitrum.api.entity.Task;
import com.vitrum.api.entity.User;
import com.vitrum.api.repository.ResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ResultService {

    private final ResultRepository repository;

    public List<ResultResponse> getAllUserResults(User user) {
        Optional<List<Result>> results = repository.findByStudent(user);
        List<ResultResponse> resultResponses = new ArrayList<>();
        if (results.isPresent()) {
            resultResponses = results.get().stream().map(this::getResultResponse).collect(Collectors.toList());
        }
        return resultResponses;
    }

    public ResultResponse addAnswerToTask(ResultRequest resultRequest) {
        var result = Result.builder()
                .answer(resultRequest.getAnswer())
                .score(-1)
                .task(resultRequest.getTask())
                .creationDate(LocalDate.now())
                .student(resultRequest.getStudent())
                .build();
        repository.save(result);
        return getResultResponse(result);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public void addScoreToResult(Result result, int score) {
        result.setScore(score);
        repository.save(result);
    }

    public Result getResultByIdAndTask(Long id, Task task) {
        Optional<Result> opResult = repository.findByIdAndTask(id, task);
        return opResult.orElse(null);
    }

    private ResultResponse getResultResponse(Result result) {
        return ResultResponse.builder()
                .id(result.getId())
                .score(result.getScore())
                .answer(result.getAnswer())
                .taskId(result.getTask().getId())
                .studentId(result.getStudent().getId())
                .build();
    }
}
