package com.taskflow.backend.service;

import com.taskflow.backend.responseObject.TaskResponse;
import com.taskflow.backend.responseObject.TasksListResponse;
import com.taskflow.backend.entity.Project;
import com.taskflow.backend.entity.Status;
import com.taskflow.backend.entity.Task;
import com.taskflow.backend.entity.User;
import com.taskflow.backend.exception.NotFoundException;
import com.taskflow.backend.repository.ProjectRepository;
import com.taskflow.backend.repository.TaskRepository;
import com.taskflow.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    public TaskService(TaskRepository taskRepository,
                       ProjectRepository projectRepository,
                       UserRepository userRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
        this.userRepository = userRepository;
    }

    public TasksListResponse getTasks(UUID projectId, String status, String assignee) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        List<Task> tasks;

        if (status != null && assignee != null) {
            Status parsedStatus = parseStatus(status);
            User assigneeUser = userRepository.findById(UUID.fromString(assignee))
                    .orElseThrow(() -> new NotFoundException("Assignee not found"));

            tasks = taskRepository.findByProjectAndStatusAndAssignee(project, parsedStatus, assigneeUser);

        } else if (status != null) {
            Status parsedStatus = parseStatus(status);
            tasks = taskRepository.findByProjectAndStatus(project, parsedStatus);

        } else if (assignee != null) {
            User assigneeUser = userRepository.findById(UUID.fromString(assignee))
                    .orElseThrow(() -> new NotFoundException("Assignee not found"));

            tasks = taskRepository.findByProjectAndAssignee(project, assigneeUser);

        } else {
            tasks = taskRepository.findByProject(project);
        }

        List<TaskResponse> responseList = tasks.stream()
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

        return new TasksListResponse(responseList);
    }

    private Status parseStatus(String status) {
        return Status.valueOf(status.trim().toUpperCase());
    }
}