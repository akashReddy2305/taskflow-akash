package com.taskflow.backend.controller;

import com.taskflow.backend.entity.User;
import com.taskflow.backend.requestObject.CreateProjectRequest;
import com.taskflow.backend.requestObject.UpdateProjectRequest;
import com.taskflow.backend.responseObject.CreateProjectResponse;
import com.taskflow.backend.responseObject.ProjectDetailResponse;
import com.taskflow.backend.responseObject.ProjectResponse;
import com.taskflow.backend.responseObject.ProjectsListResponse;
import com.taskflow.backend.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

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
    public CreateProjectResponse createProject(
            @Valid @RequestBody CreateProjectRequest request,
            @AuthenticationPrincipal User user) {
        return projectService.createProject(request, user);
    }

    @GetMapping("/projects/{id}")
    public ProjectDetailResponse getProject(@PathVariable("id") UUID projectId) {
        return projectService.getProjectById(projectId);
    }

    @PatchMapping("/projects/{id}")
    public ProjectResponse updateProject(
            @PathVariable("id") UUID projectId,
            @RequestBody UpdateProjectRequest request,
            @AuthenticationPrincipal User user) {
        return projectService.updateProject(projectId, request, user);
    }

    @DeleteMapping("/projects/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(
            @PathVariable("id") UUID projectId,
            @AuthenticationPrincipal User user) {
        projectService.deleteProject(projectId, user);
    }
}