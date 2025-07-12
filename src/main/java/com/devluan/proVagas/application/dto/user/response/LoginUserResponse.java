package com.devluan.proVagas.application.dto.user.response;

public record LoginUserResponse(
    String token,
    Long expiresIn
) {

}
