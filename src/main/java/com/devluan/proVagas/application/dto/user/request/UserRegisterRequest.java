package com.devluan.proVagas.application.dto.user.request;

public record UserRegisterRequest(
    String name,
    String email,
    String password
) {
    
    public void validate() {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("O nome é obrigatório.");
        }
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("O e-mail é obrigatório.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("A senha é obrigatória.");
        }
    }
}
