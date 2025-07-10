package com.devluan.proVagas.application.service.user;

import com.devluan.proVagas.application.dto.user.request.UserRegisterRequest;
import com.devluan.proVagas.application.dto.user.response.UserRegisterResponse;
import com.devluan.proVagas.domain.user.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserApplicationService {
    UserRegisterResponse createUser(UserRegisterRequest request);
    void updateUser(UUID userId, UserRegisterRequest request);
    Optional<User> findUserById(UUID userId);
    void deleteUser(UUID userId);
}
