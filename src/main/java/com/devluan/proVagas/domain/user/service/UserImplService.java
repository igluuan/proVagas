package com.devluan.proVagas.domain.user.service;

import com.devluan.proVagas.application.dto.user.request.UserRegisterRequest;
import com.devluan.proVagas.application.dto.user.response.UserRegisterResponse;
import com.devluan.proVagas.application.service.user.UserApplicationService;
import com.devluan.proVagas.domain.user.model.User;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class UserImplService implements UserApplicationService {

    @Override
    public UserRegisterResponse createUser(UserRegisterRequest request) {
        return null;
    }

    @Override
    public void updateUser(UUID userId, UserRegisterRequest request) {

    }

    @Override
    public Optional<User> findUserById(UUID userId) {
        return Optional.empty();
    }

    @Override
    public void deleteUser(UUID userId) {

    }
}
