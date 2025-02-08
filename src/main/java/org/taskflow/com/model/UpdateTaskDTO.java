package org.taskflow.com.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record UpdateTaskDTO(
        @Schema(description = "The updated title of the task", example = "Update website layout")
        String title,

        @Schema(description = "The updated description of the task", example = "This task involves updating the homepage layout and making it more user-friendly.")
        String description,

        @Schema(description = "The updated priority of the task", example = "HIGH")
        String priority,

        @Schema(description = "The updated status of the task", example = "IN_PROGRESS")
        String status,

        @Schema(description = "The updated deadline for the task", example = "2025-03-10T15:30:00")
        LocalDateTime deadline
) {
}