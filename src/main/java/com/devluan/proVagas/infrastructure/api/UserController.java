package com.devluan.proVagas.infrastructure.api;

import com.devluan.proVagas.application.dto.user.request.UserRegisterRequest;
import com.devluan.proVagas.application.dto.user.response.UserRegisterResponse;
import com.devluan.proVagas.application.service.user.UserApplicationService;
import com.devluan.proVagas.domain.user.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserApplicationService userService;

    public UserRegisterResponse register(UserRegisterRequest request) {
        return userService.createUser(request);
    }
    public Optional<User> getUserById(UUID userId) {
        return userService.findUserById(userId);
    }
    public void updateUser(UUID userId, UserRegisterRequest request) {
        userService.updateUser(userId, request);
    }
    public void deleteUser(UUID userId) {
        userService.deleteUser(userId);
    }
}
