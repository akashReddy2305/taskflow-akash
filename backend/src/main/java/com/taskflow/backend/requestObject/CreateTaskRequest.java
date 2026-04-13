package com.taskflow.backend.requestObject;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateTaskRequest {

    @NotBlank(message = "is required")
    private String title;

    private String description;

    @NotBlank(message = "is required")
    private String priority;

    private String assignee_id;

    private LocalDate due_date;
}
