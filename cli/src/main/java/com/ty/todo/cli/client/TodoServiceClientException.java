package com.ty.todo.cli.client;

/**
 * Indicates failures while communicating with the service.
 */
public class TodoServiceClientException extends RuntimeException {

    public TodoServiceClientException(String message) {
        super(message);
    }

    public TodoServiceClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
