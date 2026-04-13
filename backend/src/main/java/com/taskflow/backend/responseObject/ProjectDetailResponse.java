package com.taskflow.backend.responseObject;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ProjectDetailResponse {
    private UUID id;
    private String name;
    private String description;
    private UUID owner_id;
    private LocalDateTime created_at;
    private List<TaskResponse> tasks;
}
