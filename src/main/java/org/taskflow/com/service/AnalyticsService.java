package org.taskflow.com.service;

import java.util.Map;

public interface AnalyticsService {
    Map<String, Long> getTaskStatusCountForProject(Long projectId);

    Map<String, Long> getTaskStatusCountForUser(Long userId);

    Double getAverageCompletionTimeForProject(Long projectId);

    Double getAverageCompletionTimeForUser(Long userId);
}
