package org.taskflow.com.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.taskflow.com.service.AnalyticsService;

import java.util.Map;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(AnalyticsController.class)
public class AnalyticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AnalyticsService analyticsService;

    @BeforeEach
    void setUp() {
        when(analyticsService.getTaskStatusCountForProject(1L))
                .thenReturn(Map.of("OPEN", 5L, "CLOSED", 3L));
        when(analyticsService.getAverageCompletionTimeForProject(1L))
                .thenReturn(10.5);

        when(analyticsService.getTaskStatusCountForUser(2L))
                .thenReturn(Map.of("IN_PROGRESS", 2L, "COMPLETED", 7L));
        when(analyticsService.getAverageCompletionTimeForUser(2L))
                .thenReturn(15.0);
    }

    @Test
    void getProjectAnalytics_shouldReturnAnalyticsData() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v1/analytics/projects/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.taskStatusCount.OPEN").value(5))
                .andExpect(jsonPath("$.taskStatusCount.CLOSED").value(3))
                .andExpect(jsonPath("$.averageCompletionTime").value(10.5));
    }

    @Test
    void getUserAnalytics_shouldReturnAnalyticsData() throws Exception {
        ResultActions result = mockMvc.perform(get("/api/v1/analytics/users/{id}", 2L)
                .contentType(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
                .andExpect(jsonPath("$.taskStatusCount.IN_PROGRESS").value(2))
                .andExpect(jsonPath("$.taskStatusCount.COMPLETED").value(7))
                .andExpect(jsonPath("$.averageCompletionTime").value(15.0));
    }

    @Test
    void exportProjectAnalyticsToCSV_shouldReturnCSVFile() throws Exception {
        mockMvc.perform(get("/api/v1/analytics/projects/{id}/export", 1L)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=project_analytics.csv"))
                .andExpect(content().contentType("text/csv"));
    }
}
