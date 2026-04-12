package com.taskflow.backend.requestObject;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateProjectRequest {
    @NotBlank(message = "is required")
    private String name;
    private String description;
}
