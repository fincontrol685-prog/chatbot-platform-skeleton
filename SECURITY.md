# 🔐 Security Policy

## Reporting Security Vulnerabilities

**IMPORTANTE: NÃO crie issues públicas para relatar vulnerabilidades de segurança.**

Se você descobrir uma vulnerabilidade de segurança, por favor envie um email para:

📧 **security@chatbotplatform.com**

Inclua as seguintes informações:

1. **Descrição da Vulnerabilidade**
   - O que é o problema de segurança?
   - Como pode ser explorado?

2. **Passos para Reproduzir**
   - Instruções detalhadas para reproduzir
   - Provas de conceito se aplicável

3. **Possível Impacto**
   - Qual é o impacto potencial?
   - Quantos usuários podem ser afetados?

4. **Sugestões de Fix** (opcional)
   - Você tem ideias sobre como corrigir?

5. **Informações Adicionais**
   - Versão afetada
   - Ambiente (backend, frontend, etc)

### SLA de Resposta

- **Recebimento**: Reconhecimento em 24 horas
- **Investigação**: Avaliação inicial em 72 horas
- **Disclosure**: Público após patch ser lançado

## Supported Versions

As seguintes versões recebem atualizações de segurança:

| Versão | Status | Fim do Suporte |
|--------|--------|---|
| 1.x | ✅ Active | 2026-12-31 |
| 0.x | ⚠️ LTS | 2025-12-31 |

## Security Best Practices

### Para Usuários

1. **Manter Atualizado**
   - Use sempre a versão mais recente
   - Aplique patches de segurança imediatamente

2. **Configuração de Produção**
   - Altere todas as senhas padrão
   - Use JWT_SECRET único e forte
   - Configure CORS apenas para domínios permitidos
   - Use HTTPS em produção

3. **Credenciais**
   - Nunca commite `.env` ou secrets
   - Use variáveis de ambiente em produção
   - Rotacione credenciais periodicamente

4. **Acesso**
   - Implemente autenticação forte (MFA se possível)
   - Use RBAC para limitar permissões
   - Monitore logs de acesso

### Para Desenvolvedores

1. **Revisão de Código**
   - Todas as mudanças requerem code review
   - Foco especial em mudanças de segurança

2. **Testes**
   - Incluir testes de segurança
   - Verificar vulnerabilidades com dependabot

3. **Dependências**
   - Manter dependências atualizadas
   - Revisar changelogs de segurança
   - Usar `npm audit` e `mvn dependency-check`

4. **Não Faça**
   - SQL Injection: Use prepared statements
   - XSS: Escape output, use Content Security Policy
   - CSRF: Use tokens CSRF
   - Hardcoding secrets

## Conhecidas Vulnerabilidades

Nenhuma vulnerabilidade conhecida no momento. Verifique [Security Advisories](https://github.com/seu-usuario/chatbot-platform-skeleton/security/advisories) para histórico.

## Dependências com CVE

Monitoramos vulnerabilidades em todas as dependências. Execute:

```bash
# Backend
mvn dependency-check:check

# Frontend
npm audit
```

## Processos de Segurança

### Code Review
- Mínimo 2 revisões para commits críticos
- Foco em padrões de segurança
- Validação de entrada/output

### Testing
- Testes de segurança em CI/CD
- SAST (Static Application Security Testing)
- Dependency scanning

### Monitoramento
- Logging de ações de segurança
- Alertas de comportamento suspeito
- Auditoria de acesso

## Conformidade

Este projeto segue:

- ✅ **OWASP Top 10** - Proteção contra vulnerabilidades comuns
- ✅ **CWE/SANS Top 25** - Melhores práticas em segurança
- ✅ **JWT Best Practices** - RFC 7519
- ✅ **Spring Security Guidelines** - Framework recomendações

## Contato para Segurança

| Canal | Contato | Uso |
|-------|---------|-----|
| Email | security@chatbotplatform.com | Vulnerabilidades críticas |
| GitHub Security | [Advisory](https://github.com/seu-usuario/chatbot-platform-skeleton/security) | Tracking público |
| Discussions | [Security Label](https://github.com/seu-usuario/chatbot-platform-skeleton/discussions) | Questões geral |

## Changelog de Segurança

### [1.0.0] - 2024-05-05

#### Corrigido
- CVE-XXXX: XSS vulnerability em user input validation
- CVE-YYYY: JWT expiration handling

#### Adicionado
- Content Security Policy headers
- CORS validation melhorada

#### Mudanças
- Atualizado Spring Security para 6.3.2
- Atualizado dependencies para versões sem CVE

## Recursos Adicionais

- [OWASP](https://owasp.org/) - Open Web Application Security Project
- [Spring Security Documentation](https://spring.io/projects/spring-security)
- [Angular Security Guide](https://angular.io/guide/security)
- [NIST Cybersecurity Framework](https://www.nist.gov/cyberframework)

---

**Agradecemos sua ajuda em manter este projeto seguro! 🔒**

