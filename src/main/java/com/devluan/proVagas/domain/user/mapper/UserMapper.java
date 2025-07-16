package com.devluan.proVagas.domain.user.mapper;

import com.devluan.proVagas.application.dto.user.request.UserRegisterRequest;
import com.devluan.proVagas.application.dto.user.response.MyProfileUserResponse;
import com.devluan.proVagas.application.dto.user.response.UserRegisterResponse;
import com.devluan.proVagas.domain.user.model.User;

import java.util.HashSet;

import org.springframework.stereotype.Component;

@Component
public class UserMapper {

    public User toEntity(UserRegisterRequest request) {
        if (request == null) {
            return null;
        }
        
        return new User(
            null, 
            request.name(),
            request.email(),
            request.password(),
            null, 
            true,
            new HashSet<>()
        );
    }

    public UserRegisterResponse toResponse(User user) {
        if (user == null) {
            return null;
        }
        
        return new UserRegisterResponse(
            user.getId().toString(),
            user.getName(),
            user.getEmail()
        );
    }

    public MyProfileUserResponse MyprofileResponse(User user){
        if(user == null){
            return null;
        }

        return new MyProfileUserResponse(
                user.getName(),
                user.getEmail(),
                user.getCreatedAt(),
                user.getRoles(),
                true
            );
    }
}
