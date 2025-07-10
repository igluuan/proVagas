package com.devluan.proVagas.domain.user.mapper;

import com.devluan.proVagas.application.dto.user.request.UserRegisterRequest;
import com.devluan.proVagas.application.dto.user.response.UserRegisterResponse;
import com.devluan.proVagas.domain.user.model.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toEntity(UserRegisterRequest request);
    @Mapping(target = "message", ignore = true)
    UserRegisterResponse toResponse(User user);
}
