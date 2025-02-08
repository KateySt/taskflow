package org.taskflow.com.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateProjectDTO(
        @NotBlank(message = "Name is required")
        @Size(max = 100, message = "Name cannot exceed 100 characters")
        @Schema(description = "The name of the project", example = "New Project")
        String name,

        @Size(max = 255, message = "Description cannot exceed 255 characters")
        @Schema(description = "A brief description of the project", example = "This is a detailed description of the project")
        String description
        ) {
}