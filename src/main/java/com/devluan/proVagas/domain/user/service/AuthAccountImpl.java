package com.devluan.proVagas.domain.user.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.devluan.proVagas.application.dto.user.request.LoginUserRequest;
import com.devluan.proVagas.application.dto.user.request.UserRegisterRequest;
import com.devluan.proVagas.application.dto.user.response.LoginUserResponse;
import com.devluan.proVagas.application.dto.user.response.UserRegisterResponse;
import com.devluan.proVagas.application.service.user.AuthAccountService;
import com.devluan.proVagas.domain.user.mapper.UserMapper;
import com.devluan.proVagas.domain.user.model.User;
import com.devluan.proVagas.domain.user.repository.UserRepository;
import com.devluan.proVagas.infrastructure.security.JwtProvider;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.devluan.proVagas.infrastructure.logging.LoggerService;
@Service
@Transactional
@RequiredArgsConstructor
public class AuthAccountImpl implements AuthAccountService{

    private final LoggerService logger;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserRegisterResponse createUser(UserRegisterRequest request) {
        validateUserRequest(request);
        logger.info("Criando novo usuário com email: {}", request.email());
        User newUser = userMapper.toEntity(request);
        encodeAndSetPassword(newUser, request.password());
        userRepository.save(newUser);
        logger.info("Usuário criado com sucesso: {}", newUser.getId());
        return userMapper.toResponse(newUser);
    }

    @Override
    public LoginUserResponse authenticateAccount(LoginUserRequest request) {
        if(request == null){
            throw new IllegalArgumentException("Os campos não devem ser nulos.");
        }
        User user = findUserByEmail(request.email());
        verifyPassword(request.password(), user.getPassword());
        try {
        
           String token = jwtProvider.generateToken(user);
           Long expiresIn = jwtProvider.getExpiresIn();
            
           logger.info("Login realizado com sucesso para o usuário: {}", request.email());
           return new LoginUserResponse(token, expiresIn);

        } catch (RuntimeException e) {
            logger.error("Erro inesperado ao processar o JWT token {}", e.getMessage());
            throw new IllegalArgumentException("Erro ao processar a autenticação.");
        }
    }
    
    private User findUserByEmail(String email){
        return userRepository.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("usuário não encontrado."));
    }
    private void verifyPassword(String requestPassword, String userPassword){
        if(!passwordEncoder.matches(requestPassword, userPassword)){
            logger.warn("Senha incorreta para o usuário: {}", "Senha inválida.");
            throw new IllegalArgumentException("Senha incorreta.");
        }
    }
    private void encodeAndSetPassword(User newUser, String password) {
        String encodedPassword = passwordEncoder.encode(password);
        newUser.changePassword(encodedPassword);
    }
    private void validateUserRequest(UserRegisterRequest request){
        if(request == null) {
            logger.error("Tentativa de criar usuário com request nulo.");
            throw new IllegalArgumentException("O usuário não pode ser nulo.");
        }
        try {
            request.validate();
        } catch (IllegalArgumentException e) {
            logger.warn("Validação falhou ao criar usuário: {}", e.getMessage());
            throw e;
        }
    }
}
