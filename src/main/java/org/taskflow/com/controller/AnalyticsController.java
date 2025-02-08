package org.taskflow.com.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.taskflow.com.service.AnalyticsService;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/analytics")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Analytics", description = "Endpoints for retrieving analytics data")
public class AnalyticsController {

    private final AnalyticsService analyticsService;

    /**
     * Retrieves analytics data for a specific project, including task status counts and average completion time.
     *
     * @param id the ID of the project.
     * @return a map containing task status counts and average completion time for the project.
     */
    @Operation(
            summary = "Get project analytics",
            description = "Retrieve analytics data for a specific project, including task status counts and average completion time.",
            tags = {"Analytics"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Analytics data retrieved successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)
                    )
            )
    })
    @GetMapping("/projects/{id}")
    public Map<String, Object> getProjectAnalytics(@PathVariable Long id) {
        log.info("Fetching analytics for project {}", id);
        Map<String, Object> response = new HashMap<>();
        response.put("taskStatusCount", analyticsService.getTaskStatusCountForProject(id));
        response.put("averageCompletionTime", analyticsService.getAverageCompletionTimeForProject(id));
        return response;
    }

    /**
     * Retrieves analytics data for a specific user, including task status counts and average completion time.
     *
     * @param id the ID of the user.
     * @return a map containing task status counts and average completion time for the user.
     */
    @Operation(
            summary = "Get user analytics",
            description = "Retrieve analytics data for a specific user, including task status counts and average completion time.",
            tags = {"Analytics"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Analytics data retrieved successfully.",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Map.class)
                    )
            )
    })
    @GetMapping("/users/{id}")
    public Map<String, Object> getUserAnalytics(@PathVariable Long id) {
        log.info("Fetching analytics for user {}", id);
        Map<String, Object> response = new HashMap<>();
        response.put("taskStatusCount", analyticsService.getTaskStatusCountForUser(id));
        response.put("averageCompletionTime", analyticsService.getAverageCompletionTimeForUser(id));
        return response;
    }

    /**
     * Exports project analytics data to a CSV file.
     *
     * @param id       the ID of the project.
     * @param response the HTTP response to write the CSV data.
     * @throws IOException if an error occurs while writing the file.
     */
    @Operation(
            summary = "Export project analytics to CSV",
            description = "Export analytics data for a specific project to a CSV file.",
            tags = {"Analytics"}
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "CSV file generated successfully."
            )
    })
    @GetMapping("/projects/{id}/export")
    public void exportProjectAnalyticsToCSV(@PathVariable Long id, HttpServletResponse response) throws IOException {
        log.info("Exporting analytics for project {} to CSV", id);
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=project_analytics.csv");

        PrintWriter writer = response.getWriter();
        writer.println("Task Status,Count");

        analyticsService.getTaskStatusCountForProject(id).forEach((status, count) -> {
            writer.println(status + "," + count);
        });

        writer.flush();
    }
}
