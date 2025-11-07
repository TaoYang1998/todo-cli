package com.ty.todo.task.api;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.ty.todo.api.dto.TaskRequest;
import com.ty.todo.api.dto.TaskResponse;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        properties = {
                "todo.reminder.mail.enabled=false",
                "todo.reminder.poll-interval=PT1H"
        })
class TaskControllerIT {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void createAndListTask() {
        TaskRequest request = new TaskRequest("integration", LocalDateTime.now().plusHours(1), "integration@example.com");

        ResponseEntity<TaskResponse> createResponse = restTemplate.postForEntity("/api/tasks", request, TaskResponse.class);
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody()).isNotNull();
        Long createdId = createResponse.getBody().id();
        assertThat(createdId).isNotNull();

        ResponseEntity<TaskResponse[]> listResponse = restTemplate.getForEntity("/api/tasks", TaskResponse[].class);
        assertThat(listResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(listResponse.getBody()).isNotNull();
        assertThat(listResponse.getBody()).anySatisfy(task -> assertThat(task.id()).isEqualTo(createdId));
    }
}
