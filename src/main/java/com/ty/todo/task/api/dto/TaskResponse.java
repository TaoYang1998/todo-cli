package com.ty.todo.task.api.dto;

import java.time.LocalDateTime;

/**
 * Response payload exposed to API clients.
 */
public record TaskResponse(
        Long id,
        String title,
        LocalDateTime remindAt,
        String email) {
}
