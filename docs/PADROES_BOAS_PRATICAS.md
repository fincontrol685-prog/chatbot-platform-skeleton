# 📚 PADRÕES E BOAS PRÁTICAS IMPLEMENTADAS

## 1. ARQUITETURA EM CAMADAS

```
┌─────────────────────────────────────────────┐
│           PRESENTATION LAYER                 │
│    (Controllers, REST Endpoints)             │
├─────────────────────────────────────────────┤
│           BUSINESS LOGIC LAYER               │
│    (Services, Validations, Rules)            │
├─────────────────────────────────────────────┤
│           DATA ACCESS LAYER                  │
│    (Repositories, JPA Queries)               │
├─────────────────────────────────────────────┤
│           DOMAIN LAYER                       │
│    (Entities, Domain Models)                 │
├─────────────────────────────────────────────┤
│           DATABASE LAYER                     │
│    (MySQL, Flyway Migrations)                │
└─────────────────────────────────────────────┘
```

**Benefícios:**
- ✅ Separação de responsabilidades clara
- ✅ Código testável e manutenível
- ✅ Fácil de escalar horizontalmente
- ✅ Reutilização de código

---

## 2. PADRÕES DE DESIGN UTILIZADOS

### **Service Layer Pattern**
```java
@Service
public class ConversationService {
    private final ConversationRepository repo;
    
    // Lógica de negócio centralizada
    public ConversationDto create(ConversationDto dto, Long userId) {
        // Validações
        // Transformações
        // Persist
        // Auditoria
    }
}
```

**Quando usar:** Operações que envolvem múltiplos repositórios ou lógica complexa

### **Repository Pattern**
```java
@Repository
public interface ConversationRepository extends JpaRepository<Conversation, Long> {
    Page<Conversation> findByBotId(Long botId, Pageable pageable);
    
    @Query("SELECT COUNT(c) FROM Conversation c WHERE c.bot.id = :botId")
    long countByBotId(@Param("botId") Long botId);
}
```

**Benefício:** Abstração do acesso a dados, facilita testes e mudança de BD

### **DTO Pattern**
```java
public class ConversationDto {
    private Long id;
    private Long botId;  // ID, não entidade completa
    private String title;
    // Sem relacionamentos
}
```

**Por que não expor entidades diretamente:**
- Segurança (não expõe colunas sensíveis)
- Flexibilidade (pode mudar schema sem quebrar API)
- Controle de serialização

### **Dependency Injection**
```java
@Service
public class ConversationService {
    private final ConversationRepository repo;
    private final AuditService auditService;
    
    // Constructor Injection (preferível a @Autowired)
    public ConversationService(ConversationRepository repo, 
                              AuditService auditService) {
        this.repo = repo;
        this.auditService = auditService;
    }
}
```

**Vantagens:**
- Fácil testar (injetar mocks)
- Imutabilidade (final fields)
- Ordem de criação clara

### **Transactional Pattern**
```java
@Service
@Transactional
public class ConversationService {
    
    public ConversationDto create(ConversationDto dto) {
        // Toda a operação é uma transação
        // Se falhar, tudo reverte (ROLLBACK)
        conversation.save();
        auditService.log(); // Atômico com save()
    }
}
```

**Propriedades:**
- `readOnly = true` para queries (otimiza performance)
- Rollback automático em exceção não-checada
- Commit em caso de sucesso

---

## 3. PADRÕES REST API

### **Convenções HTTP**
```
GET     /api/conversations           → Listar (com paginação)
POST    /api/conversations           → Criar
GET     /api/conversations/{id}      → Recuperar
PUT     /api/conversations/{id}      → Atualizar (replace)
PATCH   /api/conversations/{id}      → Atualizar (parcial)
DELETE  /api/conversations/{id}      → Deletar

GET     /api/conversations/bot/{id}  → Recurso aninhado
GET     /api/analytics/stats         → Recurso diferente
```

### **Status HTTP Corretos**
```
200 OK              → GET, PUT, PATCH com sucesso
201 Created         → POST com sucesso
204 No Content      → DELETE com sucesso
400 Bad Request     → Dados inválidos
401 Unauthorized    → Sem token/token inválido
403 Forbidden       → Sem permissão (ex: USER tenta ADMIN)
404 Not Found       → Recurso não existe
409 Conflict        → Duplicado
500 Server Error    → Erro interno
```

### **Paginação Padrão**
```java
// Frontend
GET /api/conversations/bot/1?page=0&size=10

// Backend responde com
{
  "content": [...],
  "totalElements": 150,
  "totalPages": 15,
  "currentPage": 0,
  "pageSize": 10,
  "hasNext": true
}
```

### **Response Wrapper (Opcional)**
```java
// Padrão consistente para todas as respostas
{
  "status": "SUCCESS",
  "statusCode": 200,
  "data": { /* ConversationDto */ },
  "message": "Conversa criada com sucesso",
  "timestamp": "2024-02-17T10:30:00Z"
}
```

---

## 4. SEGURANÇA

### **Authentication (JWT)**
```
1. Login → POST /api/auth/login (username + password)
2. Backend retorna JWT token
3. Client salva token no localStorage
4. Cada request inclui: Authorization: Bearer {token}
5. Servidor valida token via JwtAuthenticationFilter
6. Request continua com Authorization context
```

### **Authorization (@PreAuthorize)**
```java
@PostMapping
@PreAuthorize("hasAnyRole('ADMIN','GESTOR')")
public ResponseEntity<ConversationDto> create(@RequestBody ConversationDto dto) {
    // Apenas ADMIN ou GESTOR podem criar
}

@GetMapping
@PreAuthorize("hasAnyRole('ADMIN','GESTOR','USUARIO')")
public ResponseEntity<List<ConversationDto>> list() {
    // Todos podem listar
}
```

### **Auditoria Automática**
```java
// Cada ação é registrada
public ConversationDto create(ConversationDto dto, Long userId) {
    ConversationDto saved = /* save */;
    
    // Auditoria automática
    auditService.log(
        userId,                    // Quem fez?
        "CREATE",                  // O quê?
        "CONVERSATION",            // Qual tipo?
        saved.getId(),             // Qual ID?
        null,                       // Valor antigo
        toJson(saved)              // Valor novo
    );
    return saved;
}
```

### **IP Tracking**
```java
private String getClientIpAddress() {
    ServletRequestAttributes attributes = 
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    HttpServletRequest request = attributes.getRequest();
    
    // Captura IP mesmo atrás de proxy
    String xForwardedFor = request.getHeader("X-Forwarded-For");
    if (xForwardedFor != null) {
        return xForwardedFor.split(",")[0].trim();
    }
    return request.getRemoteAddr();
}
```

---

## 5. PERFORMANCE

### **Índices de Banco de Dados**
```java
@Entity
@Table(name = "CONVERSATION", indexes = {
    @Index(name = "idx_conversation_bot_id", columnList = "bot_id"),
    @Index(name = "idx_conversation_user_id", columnList = "user_id"),
    @Index(name = "idx_conversation_created_at", columnList = "created_at")
})
```

**Onde adicionar índices:**
- ✅ Foreign keys (bot_id, user_id)
- ✅ Campos frequentemente filtrados (status, created_at)
- ✅ Campos em WHERE e ORDER BY
- ✅ NÃO adicionar em campos com poucos valores únicos

### **Lazy Loading**
```java
@ManyToOne(fetch = FetchType.LAZY)  // Só carrega quando acessado
@JoinColumn(name = "bot_id")
private Bot bot;

// VS

@ManyToOne(fetch = FetchType.EAGER)  // Carrega sempre (evitar)
@JoinColumn(name = "bot_id")
private Bot bot;
```

### **Query Otimizações**
```java
// Ruim - N+1 query problem
for (Conversation c : conversations) {
    Bot bot = c.getBot();  // Query adicional para cada conversa!
}

// Bom - Eager fetch
@Query("SELECT c FROM Conversation c JOIN FETCH c.bot")
List<Conversation> findAllWithBot();

// Bom - Apenas campos necessários
@Query("SELECT new ConversationDto(c.id, c.title) FROM Conversation c")
List<ConversationDto> findAllBasic();
```

### **Paginação**
```java
// Evita carregar tudo na memória
Page<Conversation> findByBotId(Long botId, Pageable pageable);

// Frontend
GET /api/conversations/bot/1?page=0&size=10&sort=createdAt,desc
```

---

## 6. TRATAMENTO DE ERROS

### **Exceções Customizadas**
```java
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}

public class UnauthorizedException extends RuntimeException {
    public UnauthorizedException(String message) {
        super(message);
    }
}
```

### **Global Exception Handler**
```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleNotFound(
            ResourceNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse("NOT_FOUND", ex.getMessage()));
    }
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse("ERROR", "Erro interno do servidor"));
    }
}
```

### **Validação de Input**
```java
public class ConversationDto {
    
    @NotNull(message = "Bot ID obrigatório")
    private Long botId;
    
    @NotBlank(message = "Título obrigatório")
    @Size(min = 3, max = 255)
    private String title;
}

// Controller
@PostMapping
public ResponseEntity<ConversationDto> create(
        @Valid @RequestBody ConversationDto dto) {
    // Validação automática
}
```

---

## 7. VERSIONAMENTO DE BANCO DE DADOS

### **Flyway Migrations**
```
db/migrations/
├── V1__create_core_tables.sql       (existente)
├── V2__create_conversation_tables.sql (novo)
└── V3__create_audit_log_table.sql   (novo)
```

**Regras Importantes:**
1. ✅ Nunca modifique migrations antigas
2. ✅ Sempre adicione novo arquivo V{N}
3. ✅ Naming: V{numero}__{descricao}.sql
4. ✅ Idempotente: `CREATE TABLE IF NOT EXISTS`
5. ✅ Rollback: crie V{numero}__undo_{descricao}.sql

### **Evolução de Schema**
```sql
-- V4__add_bot_templates.sql (próximo)
CREATE TABLE BOT_TEMPLATE (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    description LONGTEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE BOT ADD COLUMN template_id BIGINT;
ALTER TABLE BOT ADD FOREIGN KEY (template_id) REFERENCES BOT_TEMPLATE(id);
```

---

## 8. TESTES (TDD)

### **Estrutura de Testes**
```
src/test/java/
├── service/
│   ├── ConversationServiceTest
│   ├── ConversationMessageServiceTest
│   └── AnalyticsServiceTest
├── controller/
│   ├── ConversationControllerTest
│   └── AnalyticsControllerTest
└── repository/
    └── ConversationRepositoryTest
```

### **Exemplo Unit Test**
```java
@SpringBootTest
@ExtendWith(MockitoExtension.class)
public class ConversationServiceTest {
    
    @Mock
    private ConversationRepository repo;
    
    @InjectMocks
    private ConversationService service;
    
    @Test
    public void testCreate() {
        // Arrange
        ConversationDto dto = new ConversationDto();
        dto.setTitle("Teste");
        
        // Act
        ConversationDto result = service.create(dto, 1L);
        
        // Assert
        assertThat(result).isNotNull();
        verify(repo, times(1)).save(any());
    }
}
```

### **Exemplo Integration Test**
```java
@SpringBootTest
@Transactional
public class ConversationControllerIntegrationTest {
    
    @Autowired
    private MockMvc mvc;
    
    @Autowired
    private ConversationRepository repo;
    
    @Test
    @WithMockUser(roles = "GESTOR")
    public void testCreateConversation() throws Exception {
        mvc.perform(post("/api/conversations")
            .contentType(MediaType.APPLICATION_JSON)
            .content(/* JSON */))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists());
    }
}
```

---

## 9. LOGGING

### **Boas Práticas**
```java
private static final Logger logger = LoggerFactory.getLogger(ConversationService.class);

public ConversationDto create(ConversationDto dto) {
    logger.info("Criando conversa: {}", dto.getTitle());
    
    try {
        Conversation saved = repository.save(entity);
        logger.info("Conversa criada com ID: {}", saved.getId());
        return toDto(saved);
    } catch (Exception e) {
        logger.error("Erro ao criar conversa", e);
        throw new RuntimeException("Falha ao criar conversa", e);
    }
}
```

### **Níveis de Log**
```
ERROR   → Exceções, situações críticas
WARN    → Situações suspeitas mas não críticas
INFO    → Informações relevantes do fluxo
DEBUG   → Detalhes para debugging
TRACE   → Altamente detalhado
```

---

## 10. DOCUMENTAÇÃO DE CÓDIGO

### **JavaDoc Comments**
```java
/**
 * Cria uma nova conversa entre usuário e bot.
 *
 * @param dto Dados da conversa (botId, userId, title)
 * @param userId ID do usuário que está criando
 * @return ConversationDto com ID gerado
 * @throws IllegalArgumentException se bot ou usuário não encontrado
 * @see ConversationDto
 * @since 0.1.0
 */
public ConversationDto create(ConversationDto dto, Long userId) {
    // ...
}
```

### **README per Módulo**
```
features/conversations/
├── README.md          (documentação específica)
├── conversation.service.ts
└── ...
```

---

## 📊 RESUMO DE PADRÕES

| Padrão | Onde Usar | Exemplo |
|--------|-----------|---------|
| Service Layer | Lógica de negócio complexa | ConversationService |
| Repository | Acesso a dados | ConversationRepository |
| DTO | Transferência de dados | ConversationDto |
| Dependency Injection | Injetar dependências | Constructor Injection |
| Transactional | Múltiplas operações atômicas | @Transactional em Service |
| @PreAuthorize | Controle de acesso | @PreAuthorize("hasRole('ADMIN')") |
| Global Exception Handler | Tratamento centralizado | @RestControllerAdvice |
| Lazy Loading | Relacionamentos opcionais | @ManyToOne(fetch = LAZY) |
| Flyway | Versionamento de schema | V1, V2, V3 migrations |
| Paginação | Listas grandes | Pageable (Spring Data) |

---

## ✅ CHECKLIST DE QUALIDADE

- [ ] Todas as classes têm responsabilidade única
- [ ] DTOs usados em API, nunca entidades
- [ ] Dependências injetadas via constructor
- [ ] Todos os endpoints têm @PreAuthorize
- [ ] Exceções customizadas para casos específicos
- [ ] Logging em pontos críticos
- [ ] Índices de BD para queries frequentes
- [ ] Testes unitários com >70% cobertura
- [ ] Migrations Flyway seguem convenção
- [ ] Documentação JavaDoc em métodos públicos

---

**Status:** Padrões profissionais aplicados ✅
**Conformidade:** Spring Boot best practices, Clean Code, SOLID principles

