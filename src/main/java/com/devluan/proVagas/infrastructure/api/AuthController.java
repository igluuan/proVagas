package com.devluan.proVagas.infrastructure.api;

import com.devluan.proVagas.application.service.user.TokenBlocklistService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.devluan.proVagas.application.dto.user.request.LoginUserRequest;
import com.devluan.proVagas.application.dto.user.request.UserRegisterRequest;
import com.devluan.proVagas.application.dto.user.response.LoginUserResponse;
import com.devluan.proVagas.application.dto.user.response.UserRegisterResponse;
import com.devluan.proVagas.application.service.password.PasswordResetTokenService;
import com.devluan.proVagas.application.service.user.AuthAccountService;
import com.devluan.proVagas.infrastructure.logging.LoggerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthAccountService authApplicantionService;
    private final PasswordResetTokenService passwordResetTokenService;
    private final LoggerService logger;
    private final TokenBlocklistService tokenBlocklistService;

    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> register(@RequestBody @Valid UserRegisterRequest request) {
        logger.info("Requisição de registro recebida para o email: {}", request.email());
        var response = authApplicantionService.createUser(request);
        logger.info("Registro bem-sucedido para o email: {}", request.email());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> login(@RequestBody @Valid LoginUserRequest request) {
        logger.info("Requisição de login recebida para o email: {}", request.email());
        var response = authApplicantionService.authenticateAccount(request);
        logger.info("Login bem-sucedido para o email: {}", request.email());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(HttpServletRequest request) {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            var token = authHeader.substring(7);
            tokenBlocklistService.add(token);
        }
        return ResponseEntity.ok().build();
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> requestPasswordReset(@RequestParam String email) {
        passwordResetTokenService.requestPasswordReset(email);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/reset-password")
    public ResponseEntity<Void> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        passwordResetTokenService.resetPassword(token, newPassword);
        return ResponseEntity.ok().build();
    }
}
