# 🤝 Guia de Contribuição

Obrigado por seu interesse em contribuir para o **Chatbot Platform Skeleton**! Este documento fornece diretrizes e instruções para contribuir ao projeto.

## 📋 Índice

- [Código de Conduta](#código-de-conduta)
- [Como Contribuir](#como-contribuir)
- [Processo de Desenvolvimento](#processo-de-desenvolvimento)
- [Padrões de Código](#padrões-de-código)
- [Testes](#testes)
- [Commit Messages](#commit-messages)
- [Pull Requests](#pull-requests)
- [Reportar Bugs](#reportar-bugs)
- [Sugerir Melhorias](#sugerir-melhorias)

## 📜 Código de Conduta

Este projeto adota o [Contributor Covenant](CODE_OF_CONDUCT.md). Ao participar, você concorda em manter um ambiente respeitoso e inclusivo.

## 🚀 Como Contribuir

### Tipos de Contribuição

1. **Bug Reports** 🐛
2. **Feature Requests** ✨
3. **Code Improvements** 📝
4. **Documentation** 📖
5. **Tests** ✅

### Primeiro Passo

1. Verifique se o problema/feature já não foi reportado em [Issues](https://github.com/seu-usuario/chatbot-platform-skeleton/issues)
2. Procure por PRs relacionadas em [Pull Requests](https://github.com/seu-usuario/chatbot-platform-skeleton/pulls)
3. Se não existe, crie uma issue primeiro (exceto para documentação trivial)

## 🔄 Processo de Desenvolvimento

### 1. Fork e Clone

```bash
# Fork via GitHub UI

# Clone seu fork
git clone https://github.com/seu-usuario/chatbot-platform-skeleton.git
cd chatbot-platform-skeleton

# Adicione upstream (repo original)
git remote add upstream https://github.com/original-usuario/chatbot-platform-skeleton.git
```

### 2. Criar Branch

```bash
# Atualize main
git fetch upstream
git checkout main
git merge upstream/main

# Crie sua branch
git checkout -b feat/sua-feature

# Ou para bug fixes
git checkout -b fix/seu-bug
```

### 3. Desenvolver

```bash
# Instale dependências
mvn clean install
cd frontend && npm ci

# Faça suas mudanças
# Execute testes (ver seção Testes)
# Commit com mensagens claras
```

### 4. Atualizar com Upstream

```bash
# Fique sincronizado com o projeto principal
git fetch upstream
git rebase upstream/main
```

### 5. Push e Pull Request

```bash
# Push para seu fork
git push origin feat/sua-feature

# Abra PR via GitHub UI
```

## 💻 Padrões de Código

### Java/Backend

**Estilo de Código**
```java
// ✅ BOAS PRÁTICAS

@Service
@Slf4j
@RequiredArgsConstructor
public class BotService {
    
    private final BotRepository botRepository;
    private final BotMapper botMapper;
    
    public BotDTO createBot(CreateBotRequest request) {
        log.info("Creating bot with name: {}", request.getName());
        
        Bot bot = Bot.builder()
            .name(request.getName())
            .description(request.getDescription())
            .active(true)
            .build();
            
        Bot savedBot = botRepository.save(bot);
        return botMapper.toDTO(savedBot);
    }
}

// ❌ EVITAR

public class BotService {
    public void createBot(String name, String desc, Boolean active) {
        // Sem logs
        // Magic values
        // Sem validation
    }
}
```

**Nomenclatura**
- Classes: `PascalCase` (ex: `BotService`)
- Métodos/Variáveis: `camelCase` (ex: `createBot`)
- Constantes: `UPPER_SNAKE_CASE` (ex: `DEFAULT_TIMEOUT_MS`)
- Packages: `com.br.chatbotplatformskeleton.feature`

**Anotações Spring**
- Use `@Slf4j` para logging
- Use `@RequiredArgsConstructor` para injeção de dependências
- Use `@Data`, `@Builder` do Lombok quando apropriado
- Adicione `@Valid` em DTOs de entrada

**Tratamento de Erros**
```java
// ✅ BOM
if (!bot.isActive()) {
    throw new BotInactiveException("Bot " + botId + " is inactive");
}

// ❌ RUIM
if (!bot.isActive()) {
    throw new RuntimeException("Error");
}
```

### TypeScript/Frontend

**Estilo de Código**
```typescript
// ✅ BOAS PRÁTICAS

@Component({
  selector: 'app-bot-list',
  templateUrl: './bot-list.component.html',
  styleUrls: ['./bot-list.component.scss']
})
export class BotListComponent implements OnInit, OnDestroy {
  
  bots$ = this.botService.getBots();
  private destroy$ = new Subject<void>();
  
  constructor(private botService: BotService) {}
  
  ngOnInit(): void {
    // Initialization
  }
  
  ngOnDestroy(): void {
    this.destroy$.next();
    this.destroy$.complete();
  }
}

// ❌ EVITAR

export class BotListComponent {
  bots: any[];
  
  constructor(botService: any) {
    botService.getBots().subscribe(x => this.bots = x);
    // Memory leak, sem tipagem
  }
}
```

**Nomenclatura**
- Classes: `PascalCase` (ex: `BotService`)
- Métodos/Variáveis: `camelCase` (ex: `getBots`)
- Interfaces: `IPascalCase` ou `PascalCase` (ex: `IBot`)
- Observables: sufixo `$` (ex: `bots$`)

**Tipagem Forte**
```typescript
// ✅ BOM - Tipado
interface Bot {
  id: string;
  name: string;
  active: boolean;
}

getBots(): Observable<Bot[]> {
  return this.http.get<Bot[]>('/api/bots');
}

// ❌ RUIM - Fraco
getBots(): Observable<any> {
  return this.http.get('/api/bots');
}
```

## ✅ Testes

### Backend - JUnit 5 + Mockito

```java
@ExtendWith(MockitoExtension.class)
class BotServiceTest {
    
    @Mock
    private BotRepository botRepository;
    
    @InjectMocks
    private BotService botService;
    
    @Test
    void shouldCreateBotSuccessfully() {
        // Arrange
        CreateBotRequest request = new CreateBotRequest("Test Bot");
        Bot expectedBot = new Bot("1", "Test Bot");
        when(botRepository.save(any())).thenReturn(expectedBot);
        
        // Act
        BotDTO result = botService.createBot(request);
        
        // Assert
        assertNotNull(result);
        assertEquals("Test Bot", result.getName());
        verify(botRepository, times(1)).save(any());
    }
}
```

**Cobertura de Testes**
- Mínimo: 80% de cobertura
- Execute: `mvn test jacoco:report`

### Frontend - Jasmine + Karma

```typescript
describe('BotService', () => {
  let service: BotService;
  let httpMock: HttpTestingController;
  
  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [BotService]
    });
    service = TestBed.inject(BotService);
    httpMock = TestBed.inject(HttpTestingController);
  });
  
  it('should fetch bots', () => {
    const mockBots: Bot[] = [{ id: '1', name: 'Bot 1' }];
    
    service.getBots().subscribe(bots => {
      expect(bots.length).toBe(1);
      expect(bots[0].name).toBe('Bot 1');
    });
    
    const req = httpMock.expectOne('/api/bots');
    expect(req.request.method).toBe('GET');
    req.flush(mockBots);
  });
});
```

**Executar Testes**
```bash
# Backend
mvn test

# Frontend
cd frontend
npm test
```

## 📝 Commit Messages

Siga o padrão [Conventional Commits](https://www.conventionalcommits.org/):

```bash
<tipo>(<escopo>): <descrição>

[corpo opcional]

[rodapé opcional]
```

### Tipos

- `feat`: Nova funcionalidade
- `fix`: Correção de bug
- `docs`: Mudanças na documentação
- `style`: Formatação, missing semicolons, etc
- `refactor`: Refatoração de código sem mudança de funcionalidade
- `perf`: Melhorias de performance
- `test`: Adição ou mudança de testes
- `chore`: Tarefas de build, dependências, etc
- `ci`: Mudanças em CI/CD

### Exemplos

```bash
# Feature
git commit -m "feat(bot): add bot status tracking"

# Bug Fix
git commit -m "fix(auth): resolve JWT expiration issue"

# Documentation
git commit -m "docs(readme): update installation instructions"

# Com corpo explicativo
git commit -m "refactor(api): simplify request validation

- Consolidate validation logic
- Remove duplicate checks
- Improve error messages"
```

## 📤 Pull Requests

### Template

Seu PR deve incluir:

1. **Descrição Clara**
   - O que foi mudado e por quê
   - Links para issues relacionadas

2. **Type de Mudança**
   - [ ] 🐛 Bug fix
   - [ ] ✨ Feature
   - [ ] 📖 Documentation
   - [ ] ♻️ Refactor

3. **Checklist**
   - [ ] Meus mudanças seguem o estilo do projeto
   - [ ] Executei testes locais com sucesso
   - [ ] Adicionei testes para novas funcionalidades
   - [ ] Atualizei documentação relevante
   - [ ] Minhas mudanças não geram novos warnings

### Exemplo de PR

```
## 📝 Descrição
Adiciona suporte para desativar bots sem deletá-los.

## 🔗 Relacionado
Fecha #123

## 🧪 Como foi testado
- Testes unitários adicionados
- Manual testing com Postman
- Verificado em MySQL 8.0

## ✅ Checklist
- [x] Código segue padrões
- [x] Testes passando (cobertura 85%)
- [x] Documentação atualizada
```

### Processo de Review

1. Mínimo 2 aprovações necessárias
2. CI/CD deve passar
3. Sem conflitos com main
4. Feedback será envolvido no PR

## 🐛 Reportar Bugs

### Antes de Reportar

- [ ] Procure em issues existentes
- [ ] Verifique se é problema de configuração (não bug)
- [ ] Tente reproduzir em versão mais recente

### Ao Reportar

**Template:**

```
**Descrição do Bug**
[Descrição clara e concisa]

**Passos para Reproduzir**
1. [Primeiro passo]
2. [Segundo passo]
3. ...

**Comportamento Esperado**
[O que deveria acontecer]

**Comportamento Atual**
[O que realmente acontece]

**Screenshots/Logs**
[Se aplicável]

**Ambiente**
- OS: [ex: Ubuntu 22.04]
- Java: [ex: 17.0.4]
- MySQL: [ex: 8.0.32]
- navegador: [ex: Chrome 120]

**Contexto Adicional**
[Qualquer outra informação relevante]
```

## 💡 Sugerir Melhorias

**Template:**

```
**Descrição da Melhoria**
[Descrição clara da funcionalidade/melhoria desejada]

**Problema Atual**
[O problema que essa melhoria soluciona]

**Solução Proposta**
[Como você imagina que deveria funcionar]

**Alternativas Consideradas**
[Outras abordagens possíveis]

**Contexto Adicional**
[Por que isso seria útil]
```

## 🔐 Relatando Vulnerabilidades de Segurança

**NÃO** crie uma issue pública para vulnerabilidades de segurança!

Envie um email privado para: `security@chatbotplatform.com`

Inclua:
- Descrição da vulnerabilidade
- Passos para reproduzir
- Possível impacto
- Sugestões de fix (opcional)

## 📚 Recursos Úteis

- [Arquitetura do Projeto](architecture.md)
- [Setup Local](../README.md#instalação--setup)
- [API Documentation](API_ENDPOINTS_COMPLETO.md)
- [Database Schema](er-diagram.txt)
- [Spring Boot Docs](https://spring.io/projects/spring-boot)
- [Angular Docs](https://angular.io/docs)

## 🎓 Aprendendo Mais

- Verifique [Issues Abertas](https://github.com/seu-usuario/chatbot-platform-skeleton/issues?q=is%3Aopen+is%3Aissue+label%3A%22good+first+issue%22) marcadas como `good first issue`
- Leia código existente
- Participe das [Discussions](https://github.com/seu-usuario/chatbot-platform-skeleton/discussions)

## ❓ Dúvidas?

- 📧 Email: dev@chatbotplatform.com
- 💬 Discord: [Link do servidor]
- 🌐 Website: [www.chatbotplatform.com](https://www.chatbotplatform.com)

---

<div align="center">

**Obrigado por contribuir! 🙏**

Sua ajuda torna este projeto melhor para todos!

</div>

