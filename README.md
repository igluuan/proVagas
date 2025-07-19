# ProVagas

Sistema de gestão de vagas e candidaturas desenvolvido com Spring Boot e PostgreSQL.

## 🚀 Configuração Inicial

### Pré-requisitos

- Java 21+
- Maven 3.6+
- Docker e Docker Compose
- PostgreSQL (se executar sem Docker)

### 1. Configuração de Variáveis de Ambiente

```bash
# Copie o arquivo de exemplo
cp .env.example .env

# Edite o arquivo .env com suas configurações
nano .env
```

**Variáveis obrigatórias:**
- `DB_PASSWORD`: Senha do banco de dados
- `MAIL_USERNAME`: Usuário para envio de emails
- `MAIL_PASSWORD`: Senha para envio de emails

### 2. Chaves JWT

As chaves JWT já foram geradas durante a configuração. Se precisar regenerá-las:

```bash
# Gerar nova chave privada
openssl genrsa -out src/main/resources/certs/app.key 2048

# Gerar chave pública correspondente
openssl rsa -in src/main/resources/certs/app.key -pubout -out src/main/resources/certs/app.pub
```

## 🐳 Executando com Docker

### Desenvolvimento
```bash
cd docker
docker-compose up -d
```

### Produção
```bash
cd docker
docker-compose -f docker-compose.yml up -d
```

## 💻 Executando Localmente

### 1. Configurar banco de dados PostgreSQL

```bash
# Criar banco de dados
createdb vagas_db

# Ou usar Docker apenas para o banco
cd docker
docker-compose up postgres -d
```

### 2. Executar a aplicação

```bash
# Definir profile ativo
export SPRING_PROFILES_ACTIVE=dev

# Executar com Maven
mvn spring-boot:run

# Ou compilar e executar
mvn clean package
java -jar target/proVagas-0.0.1-SNAPSHOT.jar
```

## 📱 Acessos

- **Aplicação**: http://localhost:8080
- **PgAdmin**: http://localhost:5050 (quando usando Docker)
- **API Documentation**: http://localhost:8080/api (planejado)

### Usuários padrão (desenvolvimento)

- **Admin**: admin@provagas.com / admin123

## 🛠️ Desenvolvimento

### Estrutura do projeto

```
src/
├── main/
│   ├── java/com/devluan/proVagas/
│   │   ├── application/     # DTOs e Services (Use Cases)
│   │   ├── domain/          # Entidades e lógica de negócio
│   │   └── infrastructure/  # Configurações e APIs
│   └── resources/
│       ├── certs/          # Chaves JWT (não versionadas)
│       ├── static/         # CSS, JS, imagens
│       ├── templates/      # Templates Thymeleaf
│       └── *.properties    # Configurações por ambiente
```

### Profiles disponíveis

- `dev`: Desenvolvimento local com logs detalhados
- `docker`: Execução em container Docker
- `prod`: Produção (planejado)

### Comandos úteis

```bash
# Executar testes
mvn test

# Verificar dependências desatualizadas
mvn versions:display-dependency-updates

# Gerar relatório de dependências
mvn dependency:tree
```

## 🔒 Segurança

- ✅ Senhas hasheadas com BCrypt
- ✅ JWT com chaves RSA
- ✅ Configurações sensíveis em variáveis de ambiente
- ✅ Rate limiting (planejado)
- ✅ CORS configurado adequadamente

## 📋 Funcionalidades

### Implementadas
- [x] Registro e login de usuários
- [x] Autenticação JWT
- [x] Sistema de roles (USER, ADMIN, COMPANY)
- [x] Reset de senha por email
- [x] Dashboard básico
- [x] Gerenciamento de perfil

### Planejadas
- [ ] Cadastro e busca de vagas
- [ ] Sistema de candidaturas
- [ ] Notificações por email
- [ ] API REST completa
- [ ] Testes automatizados
- [ ] Documentação Swagger

## 🤝 Contribuição

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

### 🔀 Merge para Develop

Para fazer merge seguro das alterações para a branch develop:

**Modo automático (recomendado):**
```bash
./scripts/safe-merge.sh
```

**Documentação detalhada:**
- 📖 [Guia Completo de Merge](MERGE_GUIDE.md) - Processo detalhado e seguro
- ⚡ [Guia Rápido](MERGE_QUICK_GUIDE.md) - Comandos essenciais
- 🛡️ [Variáveis de Ambiente](VARIAVEIS_AMBIENTE.md) - Configuração de credenciais

## 📄 Licença

Este projeto está sob a licença MIT. Veja o arquivo `LICENSE` para detalhes.

## 📞 Suporte

Para questões e suporte, abra uma issue no GitHub ou entre em contato:

- Email: [devluan@exemplo.com](mailto:devluan@exemplo.com)
- GitHub: [@devluan](https://github.com/devluan)