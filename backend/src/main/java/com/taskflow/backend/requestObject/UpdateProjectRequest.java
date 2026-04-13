package com.taskflow.backend.requestObject;

import lombok.Data;

@Data
public class UpdateProjectRequest {
    private String name;
    private String description;
}
