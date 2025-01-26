package org.taskflow.com.model;

import java.time.LocalDateTime;

public record CreateTaskDTO(
        String title,
        String description,
        String priority,
        String status,
        Long projectId,
        LocalDateTime deadline
) {}
