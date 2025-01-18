package org.taskflow.com.model;

import lombok.Builder;

@Builder
public record SessionInfo(
        String token
) {
}