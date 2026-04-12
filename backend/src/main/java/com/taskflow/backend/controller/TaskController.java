package com.taskflow.backend.controller;

import com.taskflow.backend.responseObject.TasksListResponse;
import com.taskflow.backend.service.TaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
            @RequestParam(required = false) String assignee
    ) {
        return taskService.getTasks(projectId, status, assignee);
    }
}