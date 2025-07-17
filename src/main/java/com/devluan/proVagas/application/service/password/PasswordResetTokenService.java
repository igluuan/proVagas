package com.devluan.proVagas.application.service.password;

public interface PasswordResetTokenService {
    void requestPasswordReset(String email);
    void resetPassword(String token, String newPassword);
    boolean isValidToken(String token);
}
