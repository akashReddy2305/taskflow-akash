package com.taskflow.backend.service;

import com.taskflow.backend.entity.Project;
import com.taskflow.backend.entity.Task;
import com.taskflow.backend.entity.User;
import com.taskflow.backend.exception.ForbiddenException;
import com.taskflow.backend.exception.NotFoundException;
import com.taskflow.backend.repository.ProjectRepository;
import com.taskflow.backend.repository.TaskRepository;
import com.taskflow.backend.requestObject.CreateProjectRequest;
import com.taskflow.backend.requestObject.UpdateProjectRequest;
import com.taskflow.backend.responseObject.CreateProjectResponse;
import com.taskflow.backend.responseObject.ProjectDetailResponse;
import com.taskflow.backend.responseObject.ProjectResponse;
import com.taskflow.backend.responseObject.ProjectsListResponse;
import com.taskflow.backend.responseObject.TaskResponse;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public ProjectService(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    public ProjectsListResponse getProjectsForCurrentUser(User user) {
        List<Project> projects = projectRepository.findAccessibleByUser(user);

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

    public ProjectDetailResponse getProjectById(UUID projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        List<Task> tasks = taskRepository.findByProject(project);

        List<TaskResponse> taskResponses = tasks.stream()
                .map(task -> new TaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus().name().toLowerCase(),
                        task.getPriority().name().toLowerCase(),
                        task.getProject().getId(),
                        task.getAssignee() != null ? task.getAssignee().getId() : null,
                        task.getDueDate(),
                        task.getCreatedAt(),
                        task.getUpdatedAt()
                ))
                .toList();

        return new ProjectDetailResponse(
                project.getId(),
                project.getName(),
                project.getDescription(),
                project.getOwner().getId(),
                project.getCreatedAt(),
                taskResponses
        );
    }

    public ProjectResponse updateProject(UUID projectId, UpdateProjectRequest request, User currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        if (!project.getOwner().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("Only the project owner can update this project");
        }

        if (request.getName() != null) {
            project.setName(request.getName());
        }
        if (request.getDescription() != null) {
            project.setDescription(request.getDescription());
        }

        Project updatedProject = projectRepository.save(project);

        return new ProjectResponse(
                updatedProject.getId(),
                updatedProject.getName(),
                updatedProject.getDescription(),
                updatedProject.getOwner().getId(),
                updatedProject.getCreatedAt()
        );
    }

    @Transactional
    public void deleteProject(UUID projectId, User currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        if (!project.getOwner().getId().equals(currentUser.getId())) {
            throw new ForbiddenException("Only the project owner can delete this project");
        }

        taskRepository.deleteAllByProject(project);
        projectRepository.delete(project);
    }
}