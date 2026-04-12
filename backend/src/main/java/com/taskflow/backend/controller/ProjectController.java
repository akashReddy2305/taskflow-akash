package com.taskflow.backend.controller;

import com.taskflow.backend.entity.User;
import com.taskflow.backend.requestObject.CreateProjectRequest;
import com.taskflow.backend.responseObject.CreateProjectResponse;
import com.taskflow.backend.responseObject.ProjectsListResponse;
import com.taskflow.backend.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @GetMapping("/projects")
    public ProjectsListResponse getProjects(@AuthenticationPrincipal User user) {
        return projectService.getProjectsForCurrentUser(user);
    }

    @PostMapping("/projects")
    @ResponseStatus(HttpStatus.CREATED)
    public CreateProjectResponse createProject(@Valid @RequestBody CreateProjectRequest createProjectRequest, @AuthenticationPrincipal User user){
        return projectService.createProject(createProjectRequest,user);
    }
}