package org.taskflow.com.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

public record UpdateProjectDTO(
        @Schema(description = "The updated name of the project", example = "New Project Name")
        @Size(max = 100, message = "Name cannot exceed 100 characters")
        String name,

        @Schema(description = "The updated description of the project", example = "This is a project to redesign the website.")
        @Size(max = 255, message = "Description cannot exceed 255 characters")
        String description
) {
}