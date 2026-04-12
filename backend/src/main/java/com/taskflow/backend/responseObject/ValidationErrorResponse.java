package com.taskflow.backend.responseObject;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;

@Data
@AllArgsConstructor
public class ValidationErrorResponse {

    private String error;
    private Map<String, String> fields;

}
