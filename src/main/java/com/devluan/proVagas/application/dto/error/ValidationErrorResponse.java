package com.devluan.proVagas.application.dto.error;

import lombok.Getter;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Getter
public class ValidationErrorResponse extends ErrorResponse {

    private final List<FieldMessage> errors = new ArrayList<>();

    public ValidationErrorResponse(Instant timestamp, Integer status, String error, String message, String path) {
        super(timestamp, status, error, message, path);
    }

    public void addError(String fieldName, String message) {
        this.errors.add(new FieldMessage(fieldName, message));
    }
}
