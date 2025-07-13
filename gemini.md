# Análise do Projeto: proVagas

## Visão Geral

`proVagas` é uma aplicação backend construída com Spring Boot que fornece uma API REST para gerenciamento de usuários, incluindo registro, autenticação e operações de perfil. A aplicação utiliza uma arquitetura em camadas bem definida, separando as responsabilidades em `application`, `domain` e `infrastructure`.

## Tecnologias Principais

- **Backend:** Java 21, Spring Boot 3.5.0
- **Segurança:** Spring Security com OAuth2 Resource Server para autenticação baseada em token JWT.
- **Banco de Dados:** PostgreSQL, com acesso via Spring Data JPA.
- **Mapeamento de Objetos:** MapStruct para conversão entre DTOs e Entidades.
- **Build Tool:** Maven.
- **Outras Ferramentas:** Lombok para redução de código boilerplate.

## Arquitetura

O projeto segue uma arquitetura em camadas:

- **`infrastructure`**: Contém os pontos de entrada da aplicação, como os `RestController`s (`AuthController`, `UserController`) e a configuração de segurança (ex: `JwtProvider`).
- **`application`**: Contém a lógica de orquestração e os casos de uso. Define as interfaces dos serviços (`AuthAccountService`, `UserAccountService`) e os DTOs (Data Transfer Objects) para requisição e resposta.
- **`domain`**: Representa o núcleo do negócio. Contém os modelos de domínio (`User`, `Role`), os repositórios (`UserRepository`) e as implementações da lógica de negócio (`AuthAccountImpl`, `UserAccountImpl`).

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
