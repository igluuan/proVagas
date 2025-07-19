# Variáveis de Ambiente

Esta aplicação utiliza variáveis de ambiente para configurar informações sensíveis e específicas do ambiente.

## Configuração do Banco de Dados

- `DB_HOST`: Host do banco de dados PostgreSQL (padrão: localhost)
- `DB_PORT`: Porta do banco de dados (padrão: 5432)
- `DB_NAME`: Nome do banco de dados (padrão: vagas_db)
- `DB_USER`: Usuário do banco de dados (padrão: postgres)
- `DB_PASSWORD`: **OBRIGATÓRIO** - Senha do banco de dados PostgreSQL

## Configuração de Email

- `MAIL_HOST`: Host do servidor SMTP (padrão: smtp.mailtrap.io)
- `MAIL_PORT`: Porta do servidor SMTP (padrão: 587)
- `MAIL_USERNAME`: **OBRIGATÓRIO** - Usuário para autenticação SMTP
- `MAIL_PASSWORD`: **OBRIGATÓRIO** - Senha para autenticação SMTP

## Configuração JWT

- `JWT_PUBLIC_KEY_PATH`: Caminho para chave pública RSA (padrão: classpath:certs/app.pub)
- `JWT_PRIVATE_KEY_PATH`: Caminho para chave privada RSA (padrão: classpath:certs/app.key)

## Configuração da Aplicação

- `SPRING_PROFILES_ACTIVE`: Profile ativo do Spring (dev, docker, prod)
- `APP_BASE_URL`: URL base da aplicação para links externos (padrão: http://localhost:8080)

## Docker Specific (para docker-compose)

- `PGADMIN_EMAIL`: Email para acessar PgAdmin
- `PGADMIN_PASSWORD`: Senha para acessar PgAdmin
- `PGADMIN_PORT`: Porta para PgAdmin (padrão: 5050)
- `APP_PORT`: Porta da aplicação (padrão: 8080)

## Como usar

1. Copie o arquivo `.env.example` para `.env`
2. Configure os valores apropriados para seu ambiente
3. Execute a aplicação

```bash
cp .env.example .env
# Edite o arquivo .env com seus valores
# Execute a aplicação
```

## Segurança

⚠️ **IMPORTANTE**: Nunca commite o arquivo `.env` ou qualquer arquivo contendo credenciais reais no controle de versão.
