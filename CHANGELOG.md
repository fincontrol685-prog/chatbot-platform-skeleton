# 📖 Changelog

Todas as mudanças notáveis neste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
e este projeto segue [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [Unreleased]

### Roadmap
- [ ] Integração com OpenAI/Google Bard
- [ ] Suporte multilíngue
- [ ] Machine Learning para intent classification
- [ ] Mobile app (React Native)
- [ ] GraphQL API

## [1.0.0] - 2024-05-05

### Adicionado
- ✨ Autenticação com JWT e refresh tokens
- ✨ Gerenciamento de usuários com RBAC
- ✨ Sistema de bots escalável
- ✨ Motor de fluxos com editor visual (planejado)
- ✨ Dashboard de analytics em tempo real
- ✨ Logging estruturado e correlação de requisições
- ✨ Docker & Docker Compose support
- ✨ CI/CD com GitHub Actions
- ✨ Documentação completa com Swagger/OpenAPI
- ✨ Cache com Redis (opcional)
- ✨ Rate limiting para proteção contra abuso
- ✨ Health checks e liveness probes
- ✨ Monitoramento com Prometheus
- ✨ 🔐 Proteção contra XSS, CSRF, SQL Injection

### Fixado
- 🐛 Problemas de sincronização de package-lock.json
- 🐛 Compatibilidade de @types/node com Vite 7.3.2
- 🐛 Erro em application.properties (npm audit comando inserido)

### Segurança
- Atualizado Spring Security para 6.3.2
- Atualizado Spring Boot para 3.2.4
- Validação de entrada robusta
- JWT com claims mínimos
- Proteção CORS configurável

## [0.1.0] - 2024-04-01

### Adicionado
- 🎉 Setup inicial do projeto
- 📁 Estrutura de pastas backend e frontend
- 🛠️ Configuração Maven e npm
- 🐘 Suporte MySQL 8.0
- 🌐 Frontend Angular 21 base
- 📚 Swagger documentation setup

---

## Como Atualizar

### Patch Version (0.0.X → 0.0.Y)
- Apenas correções de bugs
- Sem breaking changes
- Atualização recomendada

### Minor Version (0.X → 0.Y)
- Novas funcionalidades
- Sem breaking changes
- Atualização recomendada

### Major Version (X → Y)
- Possíveis breaking changes
- Revise migration guide antes de atualizar
- Teste em staging antes de produção

## Suporte de Versões

| Versão | Status | Lançado | Suporte até |
|--------|--------|---------|------------|
| 1.0.0  | ✅ LTS | 2024-05-05 | 2026-12-31 |
| 0.1.0  | ⚠️ EOL | 2024-04-01 | 2024-10-04 |

## Ciclo de Vida

- **Active**: Recebe features e bugfixes
- **LTS**: Apenas bugfixes críticos
- **EOL**: Sem suporte

## Relatando Problemas

Encontrou um bug em uma versão específica?
- Verifique se o problema ainda existe em `main`
- Abra uma [issue](https://github.com/seu-usuario/chatbot-platform-skeleton/issues)
- Inclua o número da versão

## Contributors

Veja [CONTRIBUTING.md](CONTRIBUTING.md) para instruções e [lista de contributors](https://github.com/seu-usuario/chatbot-platform-skeleton/graphs/contributors).

---

**Últimas mudanças**: Veja todos os [commits](https://github.com/seu-usuario/chatbot-platform-skeleton/commits)

