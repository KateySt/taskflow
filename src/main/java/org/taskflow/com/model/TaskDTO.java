package org.taskflow.com.model;

import java.time.LocalDateTime;

public record TaskDTO(
        Long id,
        String title,
        String description,
        String priority,
        String status,
        String assignedToEmail,
        LocalDateTime deadline,
        LocalDateTime createdAt
) {}