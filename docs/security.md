Segurança e Fluxo de Autenticação (JWT)

Objetivo

Detalhar o fluxo de autenticação usando JWT, gerenciamento de tokens e práticas de segurança para uma aplicação corporativa.

Fluxo básico

1. Usuário envia credenciais para POST /api/auth/login.
2. Backend valida credenciais (UserDetailsService + PasswordEncoder).
3. Se válidas, backend gera Access Token (curta duração, ex: 15min) e Refresh Token (maior duração, ex: 7 dias).
4. Access Token retornado ao cliente; Refresh Token é armazenado seguro (HTTP-only cookie recomendado) e/ou persistido (store de refresh tokens com jti e revogação).
5. Cliente envia Access Token no header Authorization: Bearer <token> para acessar APIs.
6. Quando Access Token expira, cliente chama /api/auth/refresh com o Refresh Token para obter novo Access Token.
7. Logout pode invalidar o Refresh Token no server-side blacklist.

Token Claims recomendados

- sub: identificador do usuário (user id ou username)
- roles: lista de roles (['ADMIN','GESTOR'])
- iss: emissor
- iat: issued at
- exp: expiration time
- jti: token id (para blacklist/revogação)

Refresh Token storage

- Preferir HTTP-only, Secure, SameSite=strict cookies para refresh token quando o frontend é um browser.
- Em aplicações SPAs com APIs separadas, avaliar CSRF; tokens em cookies mitigam CSRF com SameSite e proteção adicional.
- Alternativamente, armazenar apenas o access token em memória e o refresh token em cookie.

Segurança adicional

- Rate limiting no endpoint /api/auth/login para prevenir brute force.
- Captcha ou MFA para operações sensíveis.
- TLS em todas as comunicações.
- Auditoria de logins e falhas.

Spring Boot recomendação (implementação mínima)

- Filtrar requisições com JwtAuthenticationFilter que extrai o token e valida via JwtTokenProvider.
- Usar AuthenticationManager para autenticar com UsernamePasswordAuthenticationToken.
- Configurar HttpSecurity para permitir /api/auth/** sem autenticação e exigir autenticação para demais endpoints.
- Uso de Method Security com @EnableGlobalMethodSecurity(prePostEnabled = true) e @PreAuthorize("hasRole('ADMIN')") nos controllers/service methods.

Blacklist e Logout

- Para invalidação imediata de access tokens curtos, preferir não persistir; confiar no exp.
- Para refresh tokens, persistir com jti e data de expiração e checar se não foi revogado.

Recomendações de bibliotecas

- jjwt (io.jsonwebtoken) ou Nimbus JOSE JWT para criação/validação de tokens.
- Spring Security 6+ compatível com Spring Boot 3+.
- Use Spring Session + Redis se decidir armazenar sessões server-side.

Política de Roles e Acessos

- Mapear roles ADMIN, GESTOR, USUARIO nos endpoints via anotações ou via config.
- Roles mais finas (permissões) podem ser modeladas como authorities separadas (ex: BOT_CREATE, BOT_MANAGE).

Próximo passo

- Criar um exemplo de configuração de segurança, entidade User e Role, e snippets para frontend (Angular AuthService e AuthGuard).
