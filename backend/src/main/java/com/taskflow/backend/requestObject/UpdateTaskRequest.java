package com.taskflow.backend.requestObject;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateTaskRequest {
    private String title;
    private String description;
    private String status;
    private String priority;
    private String assignee_id;
    private LocalDate due_date;
}
