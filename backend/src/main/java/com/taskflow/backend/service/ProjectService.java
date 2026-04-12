package com.taskflow.backend.service;

import com.taskflow.backend.entity.Project;
import com.taskflow.backend.entity.User;
import com.taskflow.backend.repository.ProjectRepository;
import com.taskflow.backend.requestObject.CreateProjectRequest;
import com.taskflow.backend.responseObject.CreateProjectResponse;
import com.taskflow.backend.responseObject.ProjectResponse;
import com.taskflow.backend.responseObject.ProjectsListResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectsListResponse getProjectsForCurrentUser(User user) {

        List<Project> projects = projectRepository.findByOwner(user);

        List<ProjectResponse> responses = projects.stream()
                .map(project -> new ProjectResponse(
                        project.getId(),
                        project.getName(),
                        project.getDescription(),
                        project.getOwner().getId(),
                        project.getCreatedAt()
                ))
                .toList();

        return new ProjectsListResponse(responses);
    }

    public CreateProjectResponse createProject(CreateProjectRequest request, User currentUser) {
        Project project = new Project();
        project.setId(UUID.randomUUID());
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setOwner(currentUser);
        project.setCreatedAt(LocalDateTime.now());

        Project savedProject = projectRepository.save(project);

        return new CreateProjectResponse(
                savedProject.getId(),
                savedProject.getName(),
                savedProject.getDescription(),
                savedProject.getOwner().getId(),
                savedProject.getCreatedAt()
        );
    }
}