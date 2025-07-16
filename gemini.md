# Análise do Projeto: proVagas

## Visão Geral

`proVagas` é uma aplicação backend construída com Spring Boot que fornece uma API REST para gerenciamento de usuários, incluindo registro, autenticação e operações de perfil. A aplicação utiliza uma arquitetura em camadas bem definida, separando as responsabilidades em `application`, `domain` e `infrastructure`.

## Tecnologias Principais

- **Backend:** Java 21, Spring Boot 3.5.0
- **Segurança:** Spring Security com OAuth2 Resource Server para autenticação baseada em token JWT.
- **Banco de Dados:** PostgreSQL, com acesso via Spring Data JPA.
- **Mapeamento de Objetos:** Use mapeamento manual.
- **Build Tool:** Maven.
- **Outras Ferramentas:** Lombok para redução de código boilerplate.

## Arquitetura

O projeto segue uma arquitetura em camadas:

- **`infrastructure`**: Contém os pontos de entrada da aplicação, como os `RestController`s (`AuthController`, `UserController`) e a configuração de segurança (ex: `JwtProvider`).
- **`application`**: Contém a lógica de orquestração e os casos de uso. Define as interfaces dos serviços (`AuthAccountService`, `UserAccountService`) e os DTOs (Data Transfer Objects) para requisição e resposta.
- **`domain`**: Representa o núcleo do negócio. Contém os modelos de domínio (`User`, `Role`), os repositórios (`UserRepository`) e as implementações da lógica de negócio (`AuthAccountImpl`, `UserAccountImpl`).

## Fluxo de Trabalho de Desenvolvimento

- **Branches:** Sempre crie uma nova branch para cada nova funcionalidade ou correção de bug.

## Endpoints da API

### Autenticação (`/auth`)

- **`POST /auth/register`**: Registra um novo usuário.
  - **Request Body:** `UserRegisterRequest`
  - **Response Body:** `UserRegisterResponse`
- **`POST /auth/login`**: Autentica um usuário e retorna um token.
  - **Request Body:** `LoginUserRequest`
  - **Response Body:** `LoginUserResponse`

### Usuários (`/users`)

- **`GET /users/profile`**: Retorna o perfil do usuário autenticado.
  - **Autorização:** Requer `hasRole('USER')`.
- **`PUT /users/{userId}`**: Atualiza o perfil de um usuário.
  - **Autorização:** Requer `hasRole('USER')`.
- **`DELETE /users/{userId}`**: Deleta a conta de um usuário.
  - **Autorização:** Requer `hasRole('USER')`.

## Logout Implementation

To provide a secure logout mechanism, we will implement a server-side token blocklist using the existing Caffeine cache. This will prevent the use of compromised tokens after a user has logged out.

### Implementation Steps

1.  **`TokenBlocklistService`**:
    *   Create a new service to manage the token blocklist.
    *   This service will use the Caffeine cache to store invalidated JWTs until they expire.
    *   It will expose two methods:
        *   `add(token)`: Adds a token to the blocklist.
        *   `isBlocklisted(token)`: Checks if a token is in the blocklist.

2.  **`/logout` Endpoint**:
    *   Add a new `POST /auth/logout` endpoint to the `AuthController`.
    *   This endpoint will:
        *   Extract the JWT from the `Authorization` header.
        *   Call the `TokenBlocklistService` to add the token to the blocklist.
        *   Return a `200 OK` response.

3.  **Authentication Filter**:
    *   Modify the authentication process to check the token blocklist.
    *   Before validating a JWT, the application will first check if the token is in the blocklist.
    *   If the token is blocklisted, the request will be rejected with a `401 Unauthorized` error.
