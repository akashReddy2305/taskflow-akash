package com.taskflow.backend.responseObject;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class TasksListResponse {
    private List<TaskResponse> tasks;
}
