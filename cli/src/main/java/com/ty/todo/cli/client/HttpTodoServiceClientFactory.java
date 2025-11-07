package com.ty.todo.cli.client;

/**
 * Produces HTTP clients backed by the Java 11 HttpClient.
 */
public class HttpTodoServiceClientFactory implements TodoServiceClientFactory {

    @Override
    public TodoServiceClient create(String baseUrl) {
        return new HttpTodoServiceClient(baseUrl, JsonSupport.mapper());
    }
}
