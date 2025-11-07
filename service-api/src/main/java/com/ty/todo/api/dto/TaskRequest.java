package com.ty.todo.api.dto;

import java.time.LocalDateTime;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Request payload for creating or updating a task.
 */
public record TaskRequest(
        @NotBlank @Size(max = 255) String title,
        LocalDateTime remindAt,
        @Email @Size(max = 255) String email) {
}
