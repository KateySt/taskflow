package org.taskflow.com.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.taskflow.com.repository.TaskRepository;
import org.taskflow.com.service.AnalyticsService;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AnalyticsServiceImpl implements AnalyticsService {
    private final TaskRepository taskRepository;

    @Override
    public Map<String, Long> getTaskStatusCountForProject(Long projectId) {
        List<Object[]> results = taskRepository.countTasksByStatusForProject(projectId);
        return mapResultsToStatusCount(results);
    }

    @Override
    public Map<String, Long> getTaskStatusCountForUser(Long userId) {
        List<Object[]> results = taskRepository.countTasksByStatusForUser(userId);
        return mapResultsToStatusCount(results);
    }

    @Override
    public Double getAverageCompletionTimeForProject(Long projectId) {
        return taskRepository.averageCompletionTimeForProject(projectId);
    }

    @Override
    public Double getAverageCompletionTimeForUser(Long userId) {
        return taskRepository.averageCompletionTimeForUser(userId);
    }

    private Map<String, Long> mapResultsToStatusCount(List<Object[]> results) {
        return results.stream()
                .collect(Collectors.toMap(
                        result -> (String) result[0],
                        result -> (Long) result[1]
                ));
    }
}
