package com.devluan.proVagas.application.service.user;

import com.devluan.proVagas.application.dto.user.request.UserRegisterRequest;
import com.devluan.proVagas.domain.user.model.User;

import java.util.UUID;

import org.springframework.security.oauth2.jwt.Jwt;

public interface UserAccountService {
    User getMyProfile(Jwt jwt);
    void updateMyProfile(UUID userId, UserRegisterRequest request);
    void deleteMyAccount(UUID userId);
}
