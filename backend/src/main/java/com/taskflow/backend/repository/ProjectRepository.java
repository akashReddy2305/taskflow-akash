package com.taskflow.backend.repository;

import com.taskflow.backend.entity.Project;
import com.taskflow.backend.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProjectRepository extends JpaRepository<Project, UUID> {

    List<Project> findByOwner(User owner);

    Optional<Project> findById(UUID id);

    @Query("SELECT DISTINCT p FROM Project p LEFT JOIN Task t ON t.project = p " +
           "WHERE p.owner = :user OR t.assignee = :user")
    List<Project> findAccessibleByUser(@Param("user") User user);
}