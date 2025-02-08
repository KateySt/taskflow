package org.taskflow.com.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record CreateTaskDTO(
        @Schema(description = "The title of the task", example = "Task 1")
        String title,

        @Schema(description = "A description of the task", example = "This is the description of the task.")
        String description,

        @Schema(description = "The priority level of the task", example = "High")
        String priority,

        @Schema(description = "The status of the task", example = "Pending")
        String status,

        @Schema(description = "The ID of the project the task belongs to", example = "1")
        Long projectId,

        @Schema(description = "The deadline for completing the task", example = "2025-02-10T14:00:00")
        LocalDateTime deadline
) {}
