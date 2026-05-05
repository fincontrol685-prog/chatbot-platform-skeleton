# Checklist de Producao do Bot

## Criticos

- [x] Remover credenciais sensiveis versionadas das configuracoes principais.
- [x] Impedir bootstrap automatico de usuarios padrao em producao.
- [x] Exigir bootstrap controlado do usuario interno `bot.system`.
- [x] Restringir acesso de usuario comum as proprias conversas e mensagens.
- [x] Restringir endpoints analiticos e visoes por bot para `ADMIN` e `GESTOR`.
- [x] Corrigir rate limit real do endpoint de troca de mensagens autenticadas.
- [x] Alinhar rota base de producao para nao quebrar o frontend.
- [x] Trocar `ddl-auto=update` por validacao de schema em producao.
- [x] Impedir spoofing de `messageType`, `intent`, `confidence` e `metadata` no endpoint manual de mensagem.

## Importantes

- [ ] Implementar refresh token real em `/api/auth/refresh`.
- [ ] Revisar se `POST /api/auth/register` deve ficar publico em producao.
- [ ] Configurar TLS real para banco e canais externos; hoje a URL de MySQL continua com `useSSL=false`.
- [ ] Revisar politicas de senha, expiracao de token e rotacao de segredo JWT.
- [ ] Definir estrategia de migrations de banco e pipeline de rollout.
- [ ] Restaurar a suite de testes do frontend com target `test` funcional no Angular.

## Operacionais

- [ ] Provisionar `DB_URL`/`DB_USERNAME`/`DB_PASSWORD`/`JWT_SECRET`/`BOT_SYSTEM_PASSWORD`/`MAIL_*` no ambiente.
- [ ] Definir `CORS_ORIGINS` do dominio real.
- [ ] Confirmar que o schema produtivo esta compativel com `spring.jpa.hibernate.ddl-auto=validate`.
- [ ] Adicionar smoke tests de login, criacao de conversa, envio de mensagem e leitura de historico no deploy.
