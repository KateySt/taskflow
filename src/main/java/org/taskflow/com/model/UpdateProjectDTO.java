package org.taskflow.com.model;

import jakarta.validation.constraints.Size;

public record UpdateProjectDTO(
        @Size(max = 100, message = "Name cannot exceed 100 characters")
        String name,
        @Size(max = 255, message = "Description cannot exceed 255 characters")
        String description
) {
}