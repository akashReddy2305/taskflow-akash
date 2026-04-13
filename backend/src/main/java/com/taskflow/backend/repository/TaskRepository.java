package com.taskflow.backend.repository;

import com.taskflow.backend.entity.Project;
import com.taskflow.backend.entity.Status;
import com.taskflow.backend.entity.Task;
import com.taskflow.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface TaskRepository extends JpaRepository<Task, UUID> {

    List<Task> findByProject(Project project);

    List<Task> findByProjectAndStatus(Project project, Status status);

    List<Task> findByProjectAndAssignee(Project project, User assignee);

    List<Task> findByProjectAndStatusAndAssignee(Project project, Status status, User assignee);

    void deleteAllByProject(Project project);
}