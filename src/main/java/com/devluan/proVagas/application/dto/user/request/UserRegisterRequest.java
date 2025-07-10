package com.devluan.proVagas.application.dto.user.request;

public record UserRegisterRequest(
    String name,
    String email,
    String password,
    String confirmPassword
) {
}
