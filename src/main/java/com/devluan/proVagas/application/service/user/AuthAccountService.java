package com.devluan.proVagas.application.service.user;

import com.devluan.proVagas.application.dto.user.request.LoginUserRequest;
import com.devluan.proVagas.application.dto.user.request.UserRegisterRequest;
import com.devluan.proVagas.application.dto.user.response.LoginUserResponse;
import com.devluan.proVagas.application.dto.user.response.UserRegisterResponse;

public interface AuthAccountService {
    UserRegisterResponse createUser(UserRegisterRequest request);
    LoginUserResponse authenticateAccount(LoginUserRequest request);
}
