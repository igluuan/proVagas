package com.devluan.proVagas.infrastructure.api;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.devluan.proVagas.application.dto.user.request.LoginUserRequest;
import com.devluan.proVagas.application.dto.user.request.UserRegisterRequest;
import com.devluan.proVagas.application.dto.user.response.LoginUserResponse;
import com.devluan.proVagas.application.dto.user.response.UserRegisterResponse;
import com.devluan.proVagas.application.service.user.UserApplicationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final UserApplicationService userService;


    @PostMapping("/register")
    public ResponseEntity<UserRegisterResponse> register(@RequestBody @Valid UserRegisterRequest request) {
        var response = userService.createUser(request);
        return ResponseEntity.ok(response);
    }

    
    @PostMapping("/login")
    public ResponseEntity<LoginUserResponse> login(@RequestBody @Valid LoginUserRequest request) {
        var response = userService.authenticateAccount(request);
        return ResponseEntity.ok(response);
    }
}
