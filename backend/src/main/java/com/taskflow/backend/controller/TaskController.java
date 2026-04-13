package com.taskflow.backend.controller;

import com.taskflow.backend.entity.User;
import com.taskflow.backend.requestObject.CreateTaskRequest;
import com.taskflow.backend.requestObject.UpdateTaskRequest;
import com.taskflow.backend.responseObject.TaskResponse;
import com.taskflow.backend.responseObject.TasksListResponse;
import com.taskflow.backend.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/projects/{id}/tasks")
    public TasksListResponse getTasks(
            @PathVariable("id") UUID projectId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String assignee) {
        return taskService.getTasks(projectId, status, assignee);
    }

    @PostMapping("/projects/{id}/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(
            @PathVariable("id") UUID projectId,
            @Valid @RequestBody CreateTaskRequest request,
            @AuthenticationPrincipal User user) {
        return taskService.createTask(projectId, request, user);
    }

    @PatchMapping("/tasks/{id}")
    public TaskResponse updateTask(
            @PathVariable("id") UUID taskId,
            @RequestBody UpdateTaskRequest request,
            @AuthenticationPrincipal User user) {
        return taskService.updateTask(taskId, request, user);
    }

    @DeleteMapping("/tasks/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(
            @PathVariable("id") UUID taskId,
            @AuthenticationPrincipal User user) {
        taskService.deleteTask(taskId, user);
    }
}