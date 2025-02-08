package org.taskflow.com.model;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record ProjectDTO(
        @Schema(description = "The ID of the project", example = "1")
        Long id,

        @Schema(description = "The name of the project", example = "Project Alpha")
        String name,

        @Schema(description = "A description of the project", example = "This is a description of Project Alpha.")
        String description,

        @Schema(description = "The date and time when the project was created", example = "2025-01-01T10:00:00")
        LocalDateTime createdAt
) {
}
