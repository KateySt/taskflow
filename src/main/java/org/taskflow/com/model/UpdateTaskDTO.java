package org.taskflow.com.model;

import java.time.LocalDateTime;

public record UpdateTaskDTO(
        String title,
        String description,
        String priority,
        String status,
        LocalDateTime deadline
) {
}