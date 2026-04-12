package com.taskflow.backend.responseObject;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;



@Data
@AllArgsConstructor
public class TaskResponse {
    private UUID id;
    private String title;
    private String description;
    private String status;
    private String priority;
    private UUID project_id;
    private UUID assignee_id;
    private LocalDate due_date;
    private Instant created_at;
    private Instant updated_at;
}