package com.devluan.proVagas.application.dto.user.response;

public record UserRegisterResponse(
    String id,
    String name,
    String email,
    String message
) {
}
