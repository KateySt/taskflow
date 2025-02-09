package org.taskflow.com.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.taskflow.com.repository.TaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AnalyticsServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @InjectMocks
    private AnalyticsServiceImpl analyticsService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getTaskStatusCountForProject_shouldReturnStatusCountMap() {
        Long projectId = 1L;

        List<Object[]> mockResults = new ArrayList<>();
        mockResults.add(new Object[]{"OPEN", 5L});
        mockResults.add(new Object[]{"CLOSED", 3L});
        when(taskRepository.countTasksByStatusForProject(projectId)).thenReturn(mockResults);

        Map<String, Long> result = analyticsService.getTaskStatusCountForProject(projectId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(5L, result.get("OPEN"));
        assertEquals(3L, result.get("CLOSED"));

        verify(taskRepository).countTasksByStatusForProject(projectId);
    }

    @Test
    void getTaskStatusCountForUser_shouldReturnStatusCountMap() {
        Long userId = 2L;

        List<Object[]> mockResults = new ArrayList<>();
        mockResults.add(new Object[]{"IN_PROGRESS", 2L});
        mockResults.add(new Object[]{"COMPLETED", 7L});
        when(taskRepository.countTasksByStatusForUser(userId)).thenReturn(mockResults);

        Map<String, Long> result = analyticsService.getTaskStatusCountForUser(userId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(2L, result.get("IN_PROGRESS"));
        assertEquals(7L, result.get("COMPLETED"));

        verify(taskRepository).countTasksByStatusForUser(userId);
    }

    @Test
    void getAverageCompletionTimeForProject_shouldReturnAverageTime() {
        Long projectId = 1L;

        when(taskRepository.averageCompletionTimeForProject(projectId)).thenReturn(10.5);

        Double result = analyticsService.getAverageCompletionTimeForProject(projectId);

        assertNotNull(result);
        assertEquals(10.5, result);

        verify(taskRepository).averageCompletionTimeForProject(projectId);
    }

    @Test
    void getAverageCompletionTimeForUser_shouldReturnAverageTime() {
        Long userId = 2L;

        when(taskRepository.averageCompletionTimeForUser(userId)).thenReturn(15.0);

        Double result = analyticsService.getAverageCompletionTimeForUser(userId);

        assertNotNull(result);
        assertEquals(15.0, result);

        verify(taskRepository).averageCompletionTimeForUser(userId);
    }
}
