# 🔧 Troubleshooting

Solução para problemas comuns ao trabalhar com a Chatbot Platform.

## 📋 Índice

- [Problemas de Build](#problemas-de-build)
- [Problemas de Banco de Dados](#problemas-de-banco-de-dados)
- [Problemas de Rede](#problemas-de-rede)
- [Problemas de Frontend](#problemas-de-frontend)
- [Problemas de Autenticação](#problemas-de-autenticação)
- [Problemas de Docker](#problemas-de-docker)
- [Problemas de Performance](#problemas-de-performance)

---

## Problemas de Build

### ❌ `mvn clean package` falha com `[ERROR] COMPILATION ERROR`

**Causa**: Versão do Java incorreta ou dependências não resolvidas

**Solução**:
```bash
# Verificar versão Java
java -version  # Deve ser Java 17+

# Limpar cache Maven
mvn clean -U install

# Se persistir, verificar internet
mvn dependency:resolve

# Build com mais verbosidade
mvn clean package -X
```

### ❌ `npm ci` retorna `EUSAGE` - package.json/lock desincronizado

**Causa**: `package-lock.json` desatualizado

**Solução**:
```bash
# Remover lock file
rm package-lock.json node_modules/

# Regenerar
npm install

# Testar
npm ci
```

### ❌ Erro: `Could not resolve dependency`

**Exemplo**: 
```
Could not resolve dependency: @types/node@"^20.19.0" from vite@7.3.2
```

**Solução**:
```bash
# Atualizar dependência conflitante
npm install @types/node@^20.19.0

# Ou usar novo package-lock.json
npm ci --force
```

### ❌ `Maven wrapper` não encontrado

**Causa**: Permissões ou arquivo corrompido

**Solução**:
```bash
# Dar permissão executável
chmod +x mvnw

# Executar com bash explicitamente
bash mvnw clean package

# Ou usar Maven local
mvn clean package
```

---

## Problemas de Banco de Dados

### ❌ `Connection refused: [FATAL] password authentication failed`

**Causa**: Senha incorreta ou MySQL não iniciado

**Solução**:
```bash
# Verificar MySQL
mysql -u chatbot -p -h localhost

# Se não conectar, iniciar com Docker
docker-compose up -d mysql

# Esperar inicialização (5-10 segundos)
docker-compose logs mysql

# Verificar credenciais em .env
cat .env | grep DB_
```

### ❌ `Access denied for user 'chatbot'@'localhost'`

**Solução**:
```bash
# Resetar usuário no MySQL
mysql -u root -p

# No MySQL terminal:
DROP USER IF EXISTS 'chatbot'@'localhost';
CREATE USER 'chatbot'@'localhost' IDENTIFIED BY 'ChatBot@2024Password';
GRANT ALL PRIVILEGES ON chatBox.* TO 'chatbot'@'localhost';
FLUSH PRIVILEGES;
```

### ❌ Database `chatBox` não existe

**Solução**:
```bash
# Criar banco
mysql -u root -p -e "CREATE DATABASE chatBox;"

# Ou deixar Hibernata criar (se `ddl-auto=create`)
# Mas risco de perda de dados em produção!
```

### ❌ Flyway migration falha

**Erro**: 
```
Migration failed with error: Incompatible migration version
```

**Solução**:
```bash
# Verificar migrations
ls -la db/migrations/

# Validate migrations
mvn flyway:validate

# Limpar e reiniciar (CUIDADO: apaga dados!)
mvn flyway:clean
mvn flyway:migrate

# Ver histórico
mysql -u chatbot -p chatBox -e "SELECT * FROM flyway_schema_history;"
```

### ❌ `wait-for-mysql.sh` timeout

**Solução**:
```bash
# Docker compose approach
docker-compose down
docker-compose up -d

# Ou manual
mysql -u chatbot -p << EOF
-- esperar resposta antes de continuar
SELECT 1;
EOF

mvn spring-boot:run
```

---

## Problemas de Rede

### ❌ `Connection refused` na API

**Causa**: Backend não iniciado ou escutando porta errada

**Solução**:
```bash
# Verificar porta 8080 está livre
lsof -i :8080

# Se ocupada, matar processo
kill -9 <PID>

# Iniciar backend
mvn spring-boot:run

# Ou com porta customizada
mvn spring-boot:run -Dserver.port=9090
```

### ❌ CORS error: `Access-Control-Allow-Origin`

**Erro no console**:
```
Access to XMLHttpRequest blocked by CORS policy
```

**Solução**:
```properties
# Verificar application.properties
cors.allowed-origins=http://localhost:4200

# Frontend em desenvolvimentodocker
# Se em https, adicionar
cors.allowed-origins=https://seu-dominio.com

# Swagger/API testar
curl -H "Origin: http://localhost:4200" -i http://localhost:8080/api/bots
```

### ❌ Timeout em requisições HTTP

**Solução**:
```bash
# Aumentar timeout
mvn spring-boot:run -Dserver.servlet.session.timeout=30m

# Verificar network
ping localhost
netstat -tulpn | grep 8080

# Verificar logs
tail -f logs/chatbot-platform.log | grep -i timeout
```

---

## Problemas de Frontend

### ❌ `ng serve` crashes com erro de módulo

**Erro**: 
```
ERROR in ./src/app/app.module.ts
Module not found: Error: Can't resolve '@angular/core'
```

**Solução**:
```bash
cd frontend

# Limpar e reinstalar
rm -rf node_modules package-lock.json
npm cache clean --force
npm install

# Verificar versão Angular
ng version

# Build de debug
ng serve --configuration development --verbose
```

### ❌ `npm start` não abre browser

**Solução**:
```bash
cd frontend

# Sem auto-open
ng serve --open=false

# Abrir manualmente em http://localhost:4200

# Ou com proxy específico
ng serve --proxy-config proxy.conf.json --open
```

### ❌ CSS/SCSS não compila

**Erro**: 
```
ERROR in ./src/styles.scss
```

**Solução**:
```bash
# Reinstalar dependências
npm rebuild node-sass

# Ou usar dart-sass
npm install -D sass

# Limpar cache Angular
rm -rf .angular/cache

# Rebuild
ng build
```

### ❌ TypeScript errors após update

**Solução**:
```bash
cd frontend

# Verificar versão TS
npm list typescript

# Atualizar conforme Angular
npm install --save-dev typescript@5.9.3

# Compilar e verificar erros
ng build --strict
```

---

## Problemas de Autenticação

### ❌ `401 Unauthorized` em todas as requisições

**Causa**: Token JWT inválido ou expirado

**Solução**:
```bash
# Fazer login novamente
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"admin","password":"password"}'

# Verificar token
echo "eyJhbGc..." | jq . # JWT decode

# Verificar expiração
mvn spring-boot:run # Logs darão detalhes
```

### ❌ `403 Forbidden` - Acesso negado

**Causa**: Permissões insuficientes (RBAC)

**Solução**:
```bash
# Verificar role do usuário
mysql -u chatbot -p chatBox \
  -e "SELECT * FROM users WHERE username='seu_user';"

# Adicionar role ADMIN se necessário
mysql -u chatbot -p chatBox -e \
  "UPDATE users SET role='ADMIN' WHERE username='seu_user';"

# Fazer logout e login novamente
# Browser: Limpar localStorage
localStorage.clear()
```

### ❌ Token não é persistido entre requests

**Solução** (Frontend):
```typescript
// Verificar se token está sendo salvo
localStorage.getItem('aluthToken') // Deve existir

// Se não:
// AuthService.ts
localStorage.setItem('authToken', response.token);

// Interceptor deve adicionar headers
export class AuthInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler) {
    const token = localStorage.getItem('authToken');
    if (token) {
      req = req.clone({
        setHeaders: {
          Authorization: `Bearer ${token}`
        }
      });
    }
    return next.handle(req);
  }
}
```

---

## Problemas de Docker

### ❌ `docker-compose up` falha com `Cannot connect to`

**Solução**:
```bash
# Iniciar Docker daemon
sudo service docker start  # Linux
# Ou abrir Docker Desktop (Mac/Windows)

# Verificar status
docker ps

# Se ainda falhar, reiniciar Docker
sudo systemctl restart docker
```

### ❌ Container MySQL não inicia

**Erro**: 
```
docker-compose_mysql_1 exited with code 127
```

**Solução**:
```bash
# Ver logs
docker-compose logs mysql

# Remover container e volume antigos
docker-compose down -v

# Rebuild e start
docker-compose up -d --build mysql

# Esperar inicialização
docker-compose exec mysql mysqladmin ping
```

### ❌ Porta já em uso

**Erro**: 
```
Bind for 0.0.0.0:8080 failed: port is already allocated
```

**Solução**:
```bash
# Encontrar processo na porta
lsof -i :8080

# Matar processo
kill -9 <PID>

# Ou usar porta diferente
docker-compose.yml:
  ports:
    - "9090:8080"  # Mudar host port
```

### ❌ Container sem acesso a internet dentro do Docker

**Solução**:
```bash
# Verificar conectividade
docker-compose exec backend ping google.com

# Se falhar, verificar DNS
docker-compose exec backend cat /etc/resolv.conf

# Ou usar --dns
docker run --dns 8.8.8.8 ...
```

---

## Problemas de Performance

### ❌ A aplicação está lenta

**Solução**:
```bash
# Verificar resources
docker stats

# Se memória baixa:
mvn spring-boot:run -Xmx2G -Xms1G

# Verificar queries SQL (active Hibernate logs)
# application.properties
logging.level.org.hibernate.SQL=DEBUG
logging.level.org.hibernate.type.descriptor.sql.BasicBinder=TRACE

# Verificar cache Redis
redis-cli INFO stats
```

### ❌ Database queries lentas

**Solução**:
```bash
# Habilitar query logging
mysql -u chatbot -p chatBox
SET GLOBAL log_queries_not_using_indexes=ON;
SET GLOBAL long_query_time=2;
SET GLOBAL slow_query_log=ON;

# Verificar índices
SHOW INDEXES FROM bots;

# Adicionar índice se necessário
ALTER TABLE bots ADD INDEX idx_active (active);
```

### ❌ Memory leak ou crescimento contínuo

**Solução**:
```bash
# Gerar heap dump
jmap -dump:live,format=b,file=heap.bin <PID>

# Analisar com JProfiler ou YourKit

# Ou verificar logs
docker-compose logs backend | grep -i "OutOfMemory"

# Aumentar heap
JAVA_OPTS="-Xmx4G -Xms2G" mvn spring-boot:run
```

---

## 🆘 Ainda com problemas?

1. **Verificar logs**
   ```bash
   mvn spring-boot:run 2>&1 | tee debug.log
   ```

2. **Buscar em Issues**
   - [GitHub Issues](https://github.com/seu-usuario/chatbot-platform-skeleton/issues)
   - Use filtro de labels

3. **Criar Issue nova**
   - Inclua logs completos
   - Versão do software (`java -version`, `node -v`)
   - Passos para reproduzir

4. **Contato**
   - 💬 [Discussions](https://github.com/seu-usuario/chatbot-platform-skeleton/discussions)
   - 📧 support@chatbotplatform.com

---

**Dica**: Muitos problemas são resolvidos com
```bash
mvn clean install
npm ci
docker-compose down -v
docker-compose up -d
```

📖 [Voltar ao README](README.md)

