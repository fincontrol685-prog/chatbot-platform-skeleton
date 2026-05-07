# 🐳 Docker Compose - Solução para Erro de Schema Validation

## 📋 Problema Identificado

O erro `Schema-validation: missing table [analytics_metric]` ocorria porque:

1. **Hibernate estava em modo `validate`** - que apenas valida o schema sem criar tabelas
2. **Flyway não estava ativado** - o que poderia executar as migrations SQL automaticamente
3. **As migrations SQL** estavam no diretório `db/migrations` mas não eram executadas pelo Flyway

## ✅ Soluções Implementadas

### 1. Adicionado Flyway ao `pom.xml`
```xml
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-core</artifactId>
</dependency>
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-mysql</artifactId>
</dependency>
```

### 2. Configurado Flyway em `application-prod.properties`
```properties
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration
spring.flyway.baseline-on-migrate=true
spring.flyway.baseline-version=1
spring.flyway.table=flyway_schema_history
spring.flyway.validate-on-migrate=true
```

### 3. Copiadas Migrations para Local Esperado pelo Flyway
- **De:** `db/migrations/V*.sql`
- **Para:** `src/main/resources/db/migration/V*.sql`

Flyway espera encontrar as migrations em `src/main/resources/db/migration` no classpath.

### 4. Atualizado `docker-compose.yml`
- Removida a volume `./db/migrations:/docker-entrypoint-initdb.d`
- Agora o Flyway execute as migrations automaticamente quando a aplicação inicia

## 🚀 Como Usar

### Base de Dados Limpa (Primeira Vez)

```bash
# 1. Limpe volumes antigos se necessário
docker-compose down -v

# 2. Suba os containers
docker-compose up --build

# 3. Flyway executará automaticamente as migrations
```

### Se Já Tiver Database com Dados

Se você já tem um banco com dados e quer continuar usando-o:

```bash
docker-compose up --build
```

O Flyway detectará quais migrations já foram executadas (via tabela `flyway_schema_history`) e executará apenas as novas.

### Resetar o Banco (Desenvolvimento)

```bash
# Remove o volume do MySQL
docker-compose down -v

# Suba novamente - o banco será recriado do zero
docker-compose up --build
```

## 📂 Estrutura de Migrations

```
src/main/resources/db/migration/
├── V1__create_core_tables.sql
├── V2__create_conversation_tables.sql
├── V3__create_audit_log_table.sql
├── V4__create_bot_templates_and_notifications.sql
├── V5__insert_test_data.sql
├── V6__create_department_and_team_tables.sql
├── V7__create_security_and_compliance_tables.sql
└── V8__create_analytics_and_reporting_tables.sql
```

**Importante:** Mantenha as migrations ordenadas com prefixo `V<numero>__` para que o Flyway execute na sequência correta.

## 🔍 Verificar Status das Migrations

Acesse o MySQL e verifique a tabela de histórico:

```bash
docker exec chatbot-mysql mysql -u chatbot_user -p chatbot_prod -e "SELECT * FROM flyway_schema_history;"
```

## ⚙️ Configuração de Ambiente

Atualize o `.env` com os valores corretos:

```bash
# Database
DB_HOST=mysql
DB_PORT=3306
DB_NAME=chatbot_prod
DB_USERNAME=chatbot_user
DB_PASSWORD=db_password_change_me
MYSQL_ROOT_PASSWORD=root_password_change_me

# JWT
JWT_SECRET=seu_super_secreto_jwt_key_aqui_com_32_caracteres
JWT_EXPIRATION_MS=3600000

# CORS
CORS_ORIGINS=http://localhost:4200

# Server
SERVER_PORT=8080
BACKEND_PORT=8080
NGINX_PORT=80

# Monitoring
PROMETHEUS_PORT=9090
GRAFANA_PORT=3000
GRAFANA_PASSWORD=admin_change_me
```

## 📊 Arquivos Modificados

| Arquivo | Mudança |
|---------|---------|
| `pom.xml` | Adicionado Flyway Core e MySQL |
| `docker-compose.yml` | Removida volume de migrations do MySQL |
| `src/main/resources/application-prod.properties` | Ativado Flyway |
| `src/main/resources/db/migration/` | Criado e preenchido com migrations |

## 🐛 Troubleshooting

### Erro: "Flyway validation error"
Se o Flyway reclamar de migrations duplicadas ou conflitos:

```bash
# Verifique a tabela de histórico
docker exec chatbot-mysql mysql -u chatbot_user -p chatbot_prod -e "SELECT * FROM flyway_schema_history;"

# Se precisar limpar (desenvolvimento apenas)
docker-compose down -v
docker-compose up --build
```

### Erro: "Unable to connect to database"
Certifique-se que o MySQL iniciou com sucesso:

```bash
docker-compose logs mysql
docker-compose logs backend
```

### Erro: "CREATE_ANALYTICS_AND_REPORTING_TABLES.sql: Table already exists"
Isso pode ocorrer se você tiver mudado de `ddl-auto=create-drop` para `validate`. A solução é resetar o banco:

```bash
docker-compose down -v
docker-compose up --build
```

## ✨ Próximos Passos

1. **Teste o setup:**
   ```bash
   docker-compose up --build
   ```

2. **Verifique os logs:**
   ```bash
   docker-compose logs backend | grep -i flyway
   ```

3. **Acesse a aplicação:**
   - Frontend: http://localhost (via Nginx)
   - Backend API: http://localhost/api
   - Swagger UI: http://localhost/api/swagger-ui.html

## 📝 Notas Importantes

- ✅ Flyway é idempotente - pode ser executado múltiplas vezes sem problemas
- ✅ Cada migration tem um checksum - alterações invalidam a migration
- ✅ Novas migrations devem usar prefixo `V<numero>__` incrementando o número
- ⚠️ **Nunca** altere uma migration já executada em produção
- 🔒 Para alterações em schema já executado, crie uma nova migration

---

**Status:** ✅ Docker Compose está pronto para uso!

