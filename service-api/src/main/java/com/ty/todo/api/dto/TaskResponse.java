package com.ty.todo.api.dto;

import java.time.LocalDateTime;

/**
 * Representation of a task as returned by the service.
 */
public record TaskResponse(
        Long id,
        String title,
        LocalDateTime remindAt,
        String email,
        LocalDateTime remindedAt) {
}
