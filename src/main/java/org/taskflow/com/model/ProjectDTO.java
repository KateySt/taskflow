package org.taskflow.com.model;

import java.time.LocalDateTime;

public record ProjectDTO(Long id, String name, String description, LocalDateTime createdAt) {
}
