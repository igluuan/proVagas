package com.devluan.proVagas.infrastructure.api;

import com.devluan.proVagas.application.dto.user.request.UserRegisterRequest;
import com.devluan.proVagas.application.service.user.UserAccountService;
import com.devluan.proVagas.domain.user.model.User;

import com.devluan.proVagas.infrastructure.logging.LoggerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserAccountService userService;
    private final LoggerService logger;


    @PutMapping("/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> updateUser(@PathVariable UUID userId, @RequestBody UserRegisterRequest request) {
        logger.info("Requisição de atualização recebida para o usuário com ID: {}", userId);
        userService.updateMyProfile(userId, request);
        logger.info("Usuário com ID: {} atualizado com sucesso", userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID userId) {
        logger.info("Requisição de exclusão recebida para o usuário com ID: {}", userId);
        userService.deleteMyAccount(userId);
        logger.info("Usuário com ID: {} excluído com sucesso", userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<User> getMyProfile(@AuthenticationPrincipal Jwt jwt){
        logger.info("Requisição de perfil recebida para o usuário com email: {}", jwt.getSubject());
        User user = userService.getMyProfile(jwt);
        logger.info("Perfil retornado com sucesso para o usuário com email: {}", jwt.getSubject());
        return ResponseEntity.ok(user);
    }
}