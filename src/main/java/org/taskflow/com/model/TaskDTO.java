package org.taskflow.com.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record TaskDTO(
        @Schema(description = "The ID of the task", example = "1")
        Long id,

        @Schema(description = "The title of the task", example = "Finish the report")
        String title,

        @Schema(description = "A description of the task", example = "Complete the final report for the project by end of this week.")
        String description,

        @Schema(description = "The priority of the task", example = "High")
        String priority,

        @Schema(description = "The status of the task", example = "In Progress")
        String status,

        @Schema(description = "The email of the user assigned to the task", example = "user@example.com")
        String assignedToEmail,

        @Schema(description = "The deadline for the task", example = "2025-02-10T18:00:00")
        LocalDateTime deadline,

        @Schema(description = "The date and time when the task was created", example = "2025-01-01T10:00:00")
        LocalDateTime createdAt
) {}