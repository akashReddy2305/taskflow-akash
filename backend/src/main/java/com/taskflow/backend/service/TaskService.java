package com.taskflow.backend.service;

import com.taskflow.backend.entity.*;
import com.taskflow.backend.exception.BadRequestException;
import com.taskflow.backend.exception.ForbiddenException;
import com.taskflow.backend.exception.NotFoundException;
import com.taskflow.backend.repository.ProjectRepository;
import com.taskflow.backend.repository.TaskRepository;
import com.taskflow.backend.repository.UserRepository;
import com.taskflow.backend.requestObject.CreateTaskRequest;
import com.taskflow.backend.requestObject.UpdateTaskRequest;
import com.taskflow.backend.responseObject.TaskResponse;
import com.taskflow.backend.responseObject.TasksListResponse;
import org.springframework.stereotype.Service;

import java.time.Instant;
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
                .map(this::toTaskResponse)
                .toList();

        return new TasksListResponse(responseList);
    }

    public TaskResponse createTask(UUID projectId, CreateTaskRequest request, User currentUser) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new NotFoundException("Project not found"));

        Priority priority = parsePriority(request.getPriority());

        Task task = new Task();
        task.setId(UUID.randomUUID());
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(Status.TODO);
        task.setPriority(priority);
        task.setProject(project);
        task.setCreatedBy(currentUser);
        task.setCreatedAt(Instant.now());
        task.setUpdatedAt(Instant.now());

        if (request.getAssignee_id() != null) {
            User assignee = userRepository.findById(UUID.fromString(request.getAssignee_id()))
                    .orElseThrow(() -> new NotFoundException("Assignee not found"));
            task.setAssignee(assignee);
        }

        if (request.getDue_date() != null) {
            task.setDueDate(request.getDue_date());
        }

        Task savedTask = taskRepository.save(task);
        return toTaskResponse(savedTask);
    }

    public TaskResponse updateTask(UUID taskId, UpdateTaskRequest request, User currentUser) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        if (request.getTitle() != null) {
            task.setTitle(request.getTitle());
        }
        if (request.getDescription() != null) {
            task.setDescription(request.getDescription());
        }
        if (request.getStatus() != null) {
            task.setStatus(parseStatus(request.getStatus()));
        }
        if (request.getPriority() != null) {
            task.setPriority(parsePriority(request.getPriority()));
        }
        if (request.getAssignee_id() != null) {
            User assignee = userRepository.findById(UUID.fromString(request.getAssignee_id()))
                    .orElseThrow(() -> new NotFoundException("Assignee not found"));
            task.setAssignee(assignee);
        }
        if (request.getDue_date() != null) {
            task.setDueDate(request.getDue_date());
        }

        task.setUpdatedAt(Instant.now());

        Task updatedTask = taskRepository.save(task);
        return toTaskResponse(updatedTask);
    }

    public void deleteTask(UUID taskId, User currentUser) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new NotFoundException("Task not found"));

        boolean isProjectOwner = task.getProject().getOwner().getId().equals(currentUser.getId());
        boolean isTaskCreator = task.getCreatedBy().getId().equals(currentUser.getId());

        if (!isProjectOwner && !isTaskCreator) {
            throw new ForbiddenException("Only the project owner or task creator can delete this task");
        }

        taskRepository.delete(task);
    }

    private TaskResponse toTaskResponse(Task task) {
        return new TaskResponse(
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
        );
    }

    private Status parseStatus(String status) {
        try {
            return Status.valueOf(status.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid status: " + status + ". Must be one of: todo, in_progress, done");
        }
    }

    private Priority parsePriority(String priority) {
        try {
            return Priority.valueOf(priority.trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BadRequestException("Invalid priority: " + priority + ". Must be one of: low, medium, high");
        }
    }
}