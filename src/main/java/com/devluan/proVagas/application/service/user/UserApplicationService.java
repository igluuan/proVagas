package com.devluan.proVagas.application.service.user;

import com.devluan.proVagas.application.dto.user.request.LoginUserRequest;
import com.devluan.proVagas.application.dto.user.request.UserRegisterRequest;
import com.devluan.proVagas.application.dto.user.response.LoginUserResponse;
import com.devluan.proVagas.application.dto.user.response.UserRegisterResponse;
import com.devluan.proVagas.domain.user.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserApplicationService {
    UserRegisterResponse createUser(UserRegisterRequest request);
    void updateUser(UUID userId, UserRegisterRequest request);
    Optional<User> findUserById(UUID userId);
    List<User> findAllUsers();
    void deleteUser(UUID userId);
    LoginUserResponse authenticateAccount(LoginUserRequest request);
}
