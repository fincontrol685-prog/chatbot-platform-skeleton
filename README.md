# 🤖 Chatbot Platform Skeleton

Uma plataforma de chatbot corporativa escalável e modular, construída com **Spring Boot** no backend e **Angular** no frontend. Projetada para criar, gerenciar e orquestrar conversas inteligentes em ambientes corporativos.

[![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.4-green?logo=springboot)](https://spring.io/projects/spring-boot)
[![Angular](https://img.shields.io/badge/Angular-21-red?logo=angular)](https://angular.io/)
[![MySQL](https://img.shields.io/badge/MySQL-8.0-blue?logo=mysql)](https://www.mysql.com/)
[![Docker](https://img.shields.io/badge/Docker-Ready-2496ED?logo=docker)](https://www.docker.com/)
[![License](https://img.shields.io/badge/License-MIT-green)](LICENSE)

## 📋 Índice

- [Características Principais](#características-principais)
- [Arquitetura](#arquitetura)
- [Stack Tecnológico](#stack-tecnológico)
- [Pré-requisitos](#pré-requisitos)
- [Instalação & Setup](#instalação--setup)
- [Execução](#execução)
- [API Documentation](#api-documentation)
- [Estrutura do Projeto](#estrutura-do-projeto)
- [Configuração](#configuração)
- [Docker](#docker)
- [CI/CD](#cicd)
- [Contribuindo](#contribuindo)
- [Suporte](#suporte)

## ✨ Características Principais

### 🔐 Segurança & Autenticação
- ✅ Autenticação baseada em **JWT** (JSON Web Tokens)
- ✅ Controle de Acesso por Função (RBAC)
- ✅ Proteção contra vulnerabilidades comuns (CORS, XSS, CSRF)
- ✅ Criptografia segura de senhas

### 💬 Gerenciamento de Chatbots
- ✅ Criar, editar e deletar bots
- ✅ Ativar/desativar bots em tempo real
- ✅ Histórico de conversas persistente
- ✅ Suporte para múltiplos canais

### 🎯 Motor de Fluxos Inteligentes
- ✅ Editor visual de fluxos de conversa
- ✅ Suporte para intents e utterances
- ✅ Roteamento inteligente de mensagens
- ✅ Análise de intenção da conversa

### 📊 Analytics & Observabilidade
- ✅ Dashboard de métricas em tempo real
- ✅ Logging estruturado e correlação de requisições
- ✅ Monitoramento de performance (Prometheus + Grafana)
- ✅ Health checks e liveness probes

### 🚀 Infraestrutura Moderna
- ✅ Containerização com Docker & Docker Compose
- ✅ CI/CD com GitHub Actions
- ✅ Cache com Redis (optional)
- ✅ Rate limiting e proteção contra abuso
- ✅ Migrations automáticas com Flyway

## 🏗️ Arquitetura

```
┌─────────────────────────────────────────────────────────────┐
│                      Frontend (Angular 21)                   │
│              Material Design + ngx-charts                     │
└────────────────────────┬────────────────────────────────────┘
                         │ REST API
                         ↓
┌─────────────────────────────────────────────────────────────┐
│              Backend (Spring Boot 3.2.4)                      │
├─────────────────────────────────────────────────────────────┤
│  API Layer  │ Controllers REST, Validação, Auth              │
├─────────────────────────────────────────────────────────────┤
│  Service    │ Regras de Negócio, Orquestração                │
├─────────────────────────────────────────────────────────────┤
│  Flow       │ Motor de Fluxos, Intent Analysis               │
├─────────────────────────────────────────────────────────────┤
│  Persistence│ JPA/Hibernate, Repositórios                    │
├─────────────────────────────────────────────────────────────┤
│  Integration│ Adaptadores, Gateways, Provedores IA          │
└────────────┬────────────────────────────────────────────────┘
             │
    ┌────────┼────────┐
    ↓        ↓        ↓
 MySQL    Redis   External
          APIs
```

### Camadas da Aplicação

| Camada | Responsabilidade | Componentes |
|--------|-----------------|------------|
| **API Layer** | Expor endpoints REST, validação, autenticação | Controllers, Exception Handlers |
| **Security Layer** | JWT, autorização, RBAC | SecurityConfig, JwtProvider, AuthService |
| **Service Layer** | Lógica de negócio, orquestração | BotService, UserService, FlowService |
| **Flow Engine** | Execução e roteamento de fluxos | FlowEngine, IntentAnalyzer |
| **Persistence** | Acesso a dados, ORM | Entities, Repositories, Migrations |
| **Integration** | Adaptadores externos | Gateway, IntegrationService |

## 🛠️ Stack Tecnológico

### Backend
```
Java 17 LTS
├── Spring Boot 3.2.4
│   ├── Spring Security 6.3.2 (JWT)
│   ├── Spring Data JPA
│   ├── Spring Web
│   └── Spring Validation
├── Hibernate 6.x
├── MySQL 8.0 (JDBC Driver)
├── Flyway (Database Migrations)
├── MapStruct (Entity Mapping)
├── JJWT 0.11.5 (JWT)
├── Redis (Caching)
├── Prometheus (Metrics)
└── Logback (Structured Logging)
```

### Frontend
```
Angular 21
├── @angular/material 21.2.9
├── @swimlane/ngx-charts 20.3.0
├── RxJS 7.8.1
├── TypeScript 5.9.3
└── Angular CLI 21.2.9
```

### Infraestrutura
```
Docker & Docker Compose
├── MySQL 8.0
├── Redis (optional)
└── Nginx (reverse proxy)

CI/CD: GitHub Actions
Development Tools:
├── Maven (Build)
├── npm (Frontend)
└── Git
```

## 📦 Pré-requisitos

- **Java 17+** ([OpenJDK](https://openjdk.org/) ou [AdoptOpenJDK](https://adoptopenjdk.net/))
- **Maven 3.8+**
- **Node.js 18+** e **npm 9+**
- **Docker & Docker Compose** (para desenvolvimento containerizado)
- **MySQL 8.0+** (ou use o container Docker)

### Validar Instalação

```bash
# Verificar Java
java -version

# Verificar Maven
mvn -version

# Verificar Node.js
node --version && npm --version

# Verificar Docker (opcional)
docker --version && docker-compose --version
```

## 🚀 Instalação & Setup

### 1. Clone o Repositório

```bash
git clone https://github.com/seu-usuario/chatbot-platform-skeleton.git
cd chatbot-platform-skeleton
```

### 2. Instalar Dependências do Frontend

```bash
cd frontend
npm ci
cd ..
```

### 3. Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto:

```bash
# Database
DB_URL=jdbc:mysql://localhost:3306/chatBox?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
DB_USERNAME=chatbot
DB_PASSWORD=ChatBot@2024Password

# JWT
JWT_SECRET=your-secret-key-change-in-production

# CORS
CORS_ALLOWED_ORIGINS=http://localhost:4200

# Mail (Gmail SMTP)
MAIL_HOST=smtp.gmail.com
MAIL_PORT=587
MAIL_USERNAME=your-email@gmail.com
MAIL_PASSWORD=your-app-password

# System
BOT_SYSTEM_PASSWORD=your-system-password
```

### 4. Banco de Dados (MySQL)

**Opção A: Usando Docker (Recomendado)**

```bash
docker-compose up -d mysql
```

**Opção B: MySQL Local**

```bash
# Criar banco de dados
mysql -u root -p << EOF
CREATE DATABASE chatBox;
CREATE USER 'chatbot'@'localhost' IDENTIFIED BY 'ChatBot@2024Password';
GRANT ALL PRIVILEGES ON chatBox.* TO 'chatbot'@'localhost';
FLUSH PRIVILEGES;
EOF
```

## ▶️ Execução

### Desenvolvimento
<localHost>
```bash
# Backend (Terminal 1)
mvn spring-boot:run

# Frontend (Terminal 2)
cd frontend
npm start
```

Acesse:
- 🌐 **Frontend**: http://localhost:4200
- 📚 **Swagger API**: http://localhost:8080/swagger-ui.html
- ⚙️ **Actuator**: http://localhost:8080/actuator

### Produção com Docker

```bash
# Construir imagens
docker-compose build

# Iniciar todos os serviços
docker-compose up -d

# Ver logs
docker-compose logs -f backend

# Parar serviços
docker-compose down
```

## 📚 API Documentation

### Swagger/OpenAPI

A documentação interativa está disponível em: `http://localhost:8080/swagger-ui.html`

### Endpoints Principais

```
Authentication:
POST   /api/auth/login
POST   /api/auth/refresh
POST   /api/auth/logout

Users:
GET    /api/users
POST   /api/users
PUT    /api/users/{id}
DELETE /api/users/{id}

Bots:
GET    /api/bots
POST   /api/bots
PUT    /api/bots/{id}
DELETE /api/bots/{id}

Messages:
POST   /api/bots/{botId}/messages

Analytics:
GET    /api/analytics/dashboard
GET    /api/analytics/metrics
```

Para documentação completa: [API_ENDPOINTS_COMPLETO.md](docs/API_ENDPOINTS_COMPLETO.md)

## 📁 Estrutura do Projeto

```
chatbot-platform-skeleton/
├── src/
│   ├── main/
│   │   ├── java/com/br/chatbotplatformskeleton/
│   │   │   ├── auth/              # Autenticação JWT
│   │   │   ├── bot/               # Gerenciamento de bots
│   │   │   ├── flowendine/        # Motor de fluxos
│   │   │   ├── user/              # Gerenciamento de usuários
│   │   │   ├── analytics/         # Métricas e dashboards
│   │   │   ├── integration/       # Integrações externas
│   │   │   ├── config/            # Configurações Spring
│   │   │   ├── exception/         # Tratamento de erros
│   │   │   └── util/              # Utilitários
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-prod.properties
│   │       └── db/migrations/     # Scripts Flyway
│   └── test/
│       └── java/                  # Testes unitários
├── frontend/
│   ├── src/
│   │   ├── app/
│   │   │   ├── components/        # Componentes Angular
│   │   │   ├── services/          # Serviços HTTP
│   │   │   ├── models/            # Interfaces TypeScript
│   │   │   └── guards/            # Route guards
│   │   ├── assets/                # Imagens, ícones
│   │   └── styles/                # SCSS global
│   ├── angular.json
│   └── tsconfig.json
├── docker-compose.yml
├── Dockerfile.backend
├── pom.xml                        # Maven config
├── package.json                   # NPM config (root)
└── docs/                          # Documentação detalhada
```

## ⚙️ Configuração

### application.properties

Principais propriedades de configuração:

```properties
# Database
spring.datasource.url=jdbc:mysql://localhost:3306/chatBox
spring.datasource.username=chatbot
spring.jpa.hibernate.ddl-auto=update

# JWT
security.jwt.secret=your-secret-key
security.jwt.expiration-ms=900000

# CORS
cors.allowed-origins=http://localhost:4200

# Cache (Redis)
spring.cache.type=redis
spring.data.redis.host=localhost
spring.data.redis.port=6379

# Rate Limiting
ratelimit.enabled=true
ratelimit.user.requests=10
ratelimit.user.interval-minutes=1

# Logging
logging.level.com.br.chatbotplatformskeleton=DEBUG
logging.file.name=logs/chatbot-platform.log
```

Para todas as propriedades: [application.properties](src/main/resources/application.properties)

## 🐳 Docker

### Docker Compose

```bash
# Iniciar todos os serviços
docker-compose up -d

# Parar
docker-compose down

# Logs
docker-compose logs -f
```

### Serviços inclusos

- **backend**: Spring Boot (porta 8080)
- **mysql**: MySQL 8.0 (porta 3306)
- **redis**: Redis (porta 6379 - opcional)
- **nginx**: Reverse proxy (porta 80/443 - opcional)

### Build Manual

```bash
# Build backend
docker build -t chatbot-platform:latest -f Dockerfile.backend .

# Run
docker run -p 8080:8080 --env-file .env chatbot-platform:latest
```

## 🔄 CI/CD

### GitHub Actions

Workflows automáticos para:
- ✅ Build & Test (a cada push)
- ✅ Validação de segurança (CVE scanning)
- ✅ Análise de código (Sonar)
- ✅ Deploy automático (em produção)

```bash
# Ver workflows
cat .github/workflows/

# Local testing
./docs/test-build.sh
```

## 🧪 Testes

### Backend

```bash
# Testes unitários
mvn test

# Testes com cobertura
mvn test jacoco:report

# Build completo
mvn clean package
```

### Frontend

```bash
cd frontend

# Testes unitários
npm test

# Build produção
npm run build

# Lint
npm run lint
```

## 📊 Monitoramento

### Métricas (Prometheus)

```
http://localhost:8080/actuator/prometheus
```

### Health Checks

```
http://localhost:8080/actuator/health
```

### Exemplos de Métricas

- `http_requests_total`: Total de requisições
- `http_request_duration_seconds`: Latência das requisições
- `jvm_memory_usage_bytes`: Uso de memória JVM

Configure Prometheus & Grafana para visualização avançada!

## 🚨 Tratamento de Erros

A aplicação usa um sistema padronizado de erros:

```json
{
  "timestamp": "2024-05-05T10:00:00Z",
  "status": 400,
  "error": "VALIDATION_ERROR",
  "message": "Campo obrigatório ausente",
  "path": "/api/bots"
}
```

Veja: [Tratamento de Exceções](docs/architecture.md#segurança)

## 🔐 Segurança

### Melhores Práticas Implementadas

1. **Autenticação JWT**
   - Tokens com expiração configurável
   - Refresh token automático

2. **CORS Configurável**
   - Apenas origens permitidas

3. **Protecção CSRF**
   - SameSite cookies

4. **SQL Injection Prevention**
   - Prepared statements (JPA)
   - Validação de entrada

5. **XSS Protection**
   - Output encoding
   - Content Security Policy

6. **Rate Limiting**
   - Proteção contra DoS/brute force

7. **Auditoria**
   - Log de ações críticas
   - Correlação de requisições

### Variáveis Sensíveis

**NUNCA** commita secrets no Git:

```bash
# Usar .env ou variáveis de ambiente
export JWT_SECRET="seu-secret"
export DB_PASSWORD="sua-senha"
```

## 🤝 Contribuindo

1. **Fork** o repositório
2. **Crie** uma branch para sua feature (`git checkout -b feature/amazing-feature`)
3. **Commit** suas mudanças (`git commit -m 'Add amazing feature'`)
4. **Push** para a branch (`git push origin feature/amazing-feature`)
5. **Abra** um Pull Request

### Diretrizes

- Siga o padrão de código existente
- Escreva testes para novas funcionalidades
- Atualize documentação
- Mensagens de commit claras e concisas

## 📝 Licença

Este projeto está sob a licença MIT. Veja [LICENSE](LICENSE) para detalhes.

## 💬 Suporte

### Contato & Comunidade

- 📧 Email: suporte@chatbotplatform.com
- 🪲 Issues: [GitHub Issues](https://github.com/seu-usuario/chatbot-platform-skeleton/issues)
- 📖 Wiki: [GitHub Wiki](https://github.com/seu-usuario/chatbot-platform-skeleton/wiki)
- 💬 Discussions: [GitHub Discussions](https://github.com/seu-usuario/chatbot-platform-skeleton/discussions)

### Documentação Adicional

- [Quick Start Guide](docs/COMECE_AQUI_RAPIDO.md)
- [Production Deployment](docs/PRODUCTION_DEPLOYMENT_GUIDE.md)
- [API Documentation](docs/API_ENDPOINTS_COMPLETO.md)
- [Architecture Details](docs/architecture.md)
- [Database Schema](docs/er-diagram.txt)

## 🎯 Roadmap

- [ ] Integração com provedores de IA (OpenAI, Google Bard)
- [ ] Suporte para múltiplos idiomas
- [ ] Machine learning para classificação de intents
- [ ] Webhooks e integrações custom
- [ ] Mobile app (React Native)
- [ ] GraphQL API

## 🙏 Agradecimentos

- Spring Boot & Spring Security
- Angular team
- MySQL & Redis communities
- Todas as bibliotecas open-source usadas

---

<div align="center">

**[⬆ voltar ao topo](#-chatbot-platform-skeleton)**

Feito com ❤️ para a comunidade dev

</div>

