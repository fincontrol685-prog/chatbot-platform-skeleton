# Relatório Detalhado de Vulnerabilidades de Segurança (CVEs)
**Chatbot Platform Skeleton - Análise de CVEs**  
**Data da Análise:** 12 de Março de 2026

---

## Sumário Executivo

A análise de segurança das dependências do projeto identificou **4 CVEs de severidade HIGH** nas dependências npm do frontend, enquanto as dependências Maven do backend foram avaliadas como seguras.

| Métrica | Resultado |
|---------|-----------|
| **Dependências Maven Analisadas** | 5 (SEGURAS ✅) |
| **Dependências npm Analisadas** | 24 |
| **CVEs Encontrados** | 4 HIGH |
| **Dependências Afetadas** | 2 (@angular/common, @angular/compiler, @angular/core) |
| **CVEs Fixáveis** | 4 (100%) |

---

## 1. DEPENDÊNCIAS MAVEN (Backend)

### ✅ Status: SEGURO - Nenhuma Vulnerabilidade Detectada

**Dependências Analisadas:**
- ✅ `org.springframework.boot:spring-boot-starter-parent@3.2.4` - Seguro
- ✅ `io.jsonwebtoken:jjwt-api@0.11.5` - Seguro
- ✅ `io.jsonwebtoken:jjwt-impl@0.11.5` - Seguro
- ✅ `io.jsonwebtoken:jjwt-jackson@0.11.5` - Seguro
- ✅ `org.mapstruct:mapstruct@1.5.5.Final` - Seguro

**Conclusão:** O backend não apresenta vulnerabilidades conhecidas nas dependências diretas analisadas.

---

## 2. DEPENDÊNCIAS NPM (Frontend)

### ⚠️ Status: VULNERÁVEL - 4 CVEs de Severidade HIGH Detectados

As vulnerabilidades estão concentradas em 3 pacotes Angular do frontend. Todas as vulnerabilidades são do tipo **XSS (Cross-Site Scripting)** ou **Vazamento de Token XSRF**.

---

### 🔴 CVE-2025-66035 - XSRF Token Leakage via Protocol-Relative URLs

**Dependência Afetada:** `@angular/common@16.2.0`

**Severidade:** 🔴 **HIGH**

**Descrição:**
Uma vulnerabilidade de vazamento de token XSRF foi identificada no HttpClient do Angular. Quando URLs relativas ao protocolo (`//`) são usadas, o token XSRF é incorretamente tratado como uma requisição de mesma origem e automaticamente adicionado ao header `X-XSRF-TOKEN`, mesmo que a requisição seja para um domínio controlado pelo atacante.

**Impacto:**
- Vazamento do token XSRF válido do usuário
- Bypass completo da proteção CSRF do Angular
- Possibilita ataques CSRF autenticados contra a sessão do usuário

**Condições de Ataque:**
1. A aplicação Angular deve ter proteção XSRF ativa
2. O atacante deve conseguir fazer a aplicação enviar requisições para URLs relativas ao protocolo (ex: `//attacker.com`)
3. Requisições devem ser state-changing (POST, PUT, DELETE)

**Versões com Patch:**
- `@angular/common@19.2.16+`
- `@angular/common@20.3.14+`
- `@angular/common@21.0.1+`

**Recomendação:** ⚠️ **Atualizar para `@angular/common@21.0.1` ou superior**

---

### 🔴 CVE-2025-66412 - Stored XSS via SVG Animation, URL e Atributos MathML

**Dependência Afetada:** `@angular/compiler@16.2.0`

**Severidade:** 🔴 **HIGH**

**Descrição:**
Uma vulnerabilidade de Stored XSS foi identificada no compilador de templates do Angular. O esquema de segurança interno do compilador está incompleto, permitindo que atacantes contornem a sanitização de segurança do Angular. Especificamente:

1. **Atributos de URL SVG/MathML:** Atributos como `xlink:href`, `href` em elementos SVG não são corretamente classificados como contextos de URL rigorosos, permitindo injeção de URLs `javascript:`.

2. **Elementos de Animação SVG:** Os atributos `attributeName` em elementos SVG (`<animate>`, `<set>`, `<animateMotion>`, `<animateTransform>`) não são validados corretamente, permitindo que atacantes direcionem dinamicamente atributos sensíveis como `href` ou `xlink:href` com payloads `javascript:`.

**Atributos Vulneráveis Confirmados:**
- `xlink:href`
- `math|href`
- `annotation|href`
- SVG animation `attributeName`

**Exemplo de Ataque:**
```html
<!-- Vulnerável -->
<svg>
  <animate [attributeName]="'href'" [values]="maliciousURL"></animate>
</svg>

<!-- OU -->
<image [attr.xlink:href]="userInputWithJavaScriptURL"></image>
```

**Impacto:**
- Execução de JavaScript arbitrário no contexto da aplicação
- Roubo de sessão e tokens de autenticação
- Exfiltração de dados sensíveis
- Realização de ações não autorizadas em nome do usuário

**Condições de Ataque:**
1. A aplicação deve renderizar dados de entrada não confiável (database, API, parâmetros URL)
2. Dados devem ser vinculados aos atributos vulneráveis
3. Usuário deve interagir com o elemento (clique) ou animação deve ser acionada

**Versões com Patch:**
- `@angular/compiler@19.2.17+`
- `@angular/compiler@20.3.15+`
- `@angular/compiler@21.0.2+`

**Recomendação:** ⚠️ **Atualizar para `@angular/compiler@21.0.2` ou superior**

---

### 🔴 CVE-2026-22610 - XSS via Unsanitized SVG Script Attributes

**Dependências Afetadas:** 
- `@angular/compiler@16.2.0`
- `@angular/core@16.2.0`

**Severidade:** 🔴 **HIGH**

**Descrição:**
Uma vulnerabilidade de XSS foi identificada no compilador de templates do Angular relacionada aos atributos `href` e `xlink:href` de elementos SVG `<script>`. O compilador falha em classificar esses atributos como um contexto de "Resource URL" que requer validação estrita.

Quando template binding é usado para atribuir dados controlados pelo usuário a esses atributos, o compilador os trata como strings simples em vez de links de recursos, permitindo injeção de payloads maliciosos como:
- `data:text/javascript` URIs
- Links para scripts maliciosos externos

**Exemplo de Ataque:**
```html
<!-- Vulnerável -->
<svg>
  <script [attr.href]="userInput"></script>
</svg>
```

**Impacto:**
- Execução arbitrária de JavaScript
- Roubo de cookies de sessão e tokens
- Acesso e transmissão de informações sensíveis
- Realização de ações não autorizadas (cliques em botões, submissão de formulários)

**Condições de Ataque:**
1. Aplicação deve usar elementos SVG `<script>` em templates
2. Deve usar property/attribute binding para `href` ou `xlink:href`
3. Dados vinculados devem vir de fonte não confiável

**Versões com Patch:**
- `@angular/compiler@19.2.18+`
- `@angular/compiler@20.3.16+`
- `@angular/compiler@21.0.7+`
- `@angular/core@19.2.18+`
- `@angular/core@20.3.16+`
- `@angular/core@21.1.0+`

**Recomendação:** ⚠️ **Atualizar para `@angular/compiler@21.0.7` ou `@angular/core@21.1.0` ou superior**

---

### 🔴 CVE-2026-27970 - XSS via i18n ICU Messages

**Dependência Afetada:** `@angular/core@16.2.0`

**Severidade:** 🔴 **HIGH**

**Descrição:**
Uma vulnerabilidade de Cross-Site Scripting (XSS) foi identificada no pipeline de internacionalização (i18n) do Angular. Em mensagens ICU (International Components for Unicode), HTML de conteúdo traduzido não é adequadamente sanitizado, permitindo execução de JavaScript arbitrário.

Diferentemente da maioria das vulnerabilidades XSS, esta requer que o atacante comprometa os arquivos de tradução (xliff, xtb, etc.) antes de poder explorar a aplicação.

**Impacto:**
- Execução de JavaScript controlado pelo atacante na origem da aplicação
- Roubo de dados sensíveis armazenados em memória, LocalStorage, IndexedDB ou cookies
- Vandalism da página
- Potencial para escalação de privilégios se combinada com outras vulnerabilidades

**Condições de Ataque:**
1. Atacante deve comprometer o arquivo de tradução (xliff, xtb, etc.)
2. Aplicação deve usar Angular i18n
3. Aplicação deve usar uma ou mais mensagens ICU
4. Aplicação deve renderizar uma mensagem ICU

**Versões com Patch:**
- `@angular/core@19.2.19+`
- `@angular/core@20.3.17+`
- `@angular/core@21.1.6+`
- `@angular/core@21.2.0+`

**Recomendação:** ⚠️ **Atualizar para `@angular/core@21.2.0` ou superior**

---

## 3. PLANO DE REMEDIAÇÃO

### 3.1 Dependências com CVEs Fixáveis: 100% (4/4)

Todas as vulnerabilidades detectadas possuem versões corrigidas disponíveis. Recomenda-se atualizar as seguintes dependências:

| Pacote | Versão Atual | Versão Recomendada | CVEs Corrigidos |
|--------|-------------|-------------------|-----------------|
| `@angular/common` | 16.2.0 | **21.0.1+** | CVE-2025-66035 |
| `@angular/compiler` | 16.2.0 | **21.0.7+** | CVE-2025-66412, CVE-2026-22610 |
| `@angular/core` | 16.2.0 | **21.2.0+** | CVE-2026-22610, CVE-2026-27970 |

**Pacotes Dependentes que Também Devem Ser Atualizados:**

Para manter consistência e compatibilidade, recomenda-se atualizar também os seguintes pacotes Angular associados:

- `@angular/animations` (16.2.0 → 21.2.0)
- `@angular/cdk` (16.2.0 → 21.2.0 ou verificar compatibilidade)
- `@angular/forms` (16.2.0 → 21.2.0)
- `@angular/material` (16.2.0 → 21.2.0 ou verificar compatibilidade)
- `@angular/platform-browser` (16.2.0 → 21.2.0)
- `@angular/platform-browser-dynamic` (16.2.0 → 21.2.0)
- `@angular/router` (16.2.0 → 21.2.0)
- `@angular-devkit/build-angular` (16.2.0 → 21.2.0)
- `@angular/cli` (16.2.0 → 21.2.0)
- `@angular/compiler-cli` (16.2.0 → 21.2.0)
- `angular-eslint` (21.0.1 → versão compatível com Angular 21)

### 3.2 Passos Recomendados para Remediação

#### Fase 1: Backup e Planejamento
1. ✅ Criar backup do `frontend/package.json` e `frontend/package-lock.json`
2. ✅ Revisar release notes das versões alvo para breaking changes
3. ✅ Atualizar documentação de dependências

#### Fase 2: Atualização de Dependências
1. Atualizar `package.json` no diretório `frontend/`
2. Executar `npm install` para resolver dependências
3. Verificar se há conflitos de dependência

#### Fase 3: Validação
1. Executar `npm run build` para verificar compilação
2. Executar `npm test` para verificar testes
3. Executar linter (`npm run lint`) para verificar qualidade de código
4. Re-validar CVEs após atualização

#### Fase 4: Testes Funcionais
1. Testar login e autenticação
2. Testar dashboard e funcionalidades principais
3. Testar no navegador (compatibilidade)

---

## 4. MITIGAÇÕES TEMPORÁRIAS (Se Não Puder Atualizar Imediatamente)

### Para CVE-2025-66035 (XSRF Token Leakage)
- ✅ Evitar URLs relativas ao protocolo (`//`) nas requisições HttpClient
- ✅ Usar caminhos relativos (começando com `/`) ou URLs absolutas totalmente qualificadas
- ✅ Validar todas as URLs de backend antes de uso

### Para CVE-2025-66412 (SVG Animation/URL XSS)
- ✅ Evitar bindings dinâmicos para atributos vulneráveis (`xlink:href`, `href` em SVGs, `attributeName`)
- ✅ Se dados dinâmicos forem necessários, validar entrada contra whitelist rigoroso
- ✅ Usar `DomSanitizer` do Angular explicitamente para SVG bindings

### Para CVE-2026-22610 (SVG Script XSS)
- ✅ Não usar template binding para atributos `href` e `xlink:href` em elementos SVG `<script>`
- ✅ Se necessário usar dados dinâmicos, validar entrada rigorosamente no servidor antes de enviar para template

### Para CVE-2026-27970 (i18n XSS)
- ✅ Revisar e verificar conteúdo traduzido recebido de terceiros
- ✅ Habilitar Content Security Policy (CSP) rigoroso
- ✅ Implementar Trusted Types para enforçar sanitização adequada

### Content Security Policy (CSP)
Recomenda-se implementar CSP com as seguintes directives:
```
Content-Security-Policy: 
  default-src 'self';
  script-src 'self';
  style-src 'self' 'unsafe-inline';
  img-src 'self' data: https:;
  font-src 'self' data:;
  connect-src 'self' https://api.seu-dominio.com
```

---

## 5. RISCOS E CONSIDERAÇÕES

### 5.1 Riscos da Atualização (Angular 16 → 21)

**Mudanças Significativas Esperadas:**
- ✅ Angular 21 é uma versão com LTS estendida (maior estabilidade)
- ⚠️ Possíveis breaking changes em APIs internas
- ⚠️ Possível recompilação de componentes/módulos
- ⚠️ Possível atualização de TypeScript (5.1.6 → versão mais recente)

**Recomendação:** Executar testes completos após atualização.

### 5.2 Dependências Secundárias
Alguns pacotes podem ter dependências transitivas que também precisam ser atualizadas:
- `rxjs` (pode estar vinculado à versão do Angular)
- `zone.js` (pode estar vinculado à versão do Angular)
- `typescript` (pode ter requerimentos de versão do Angular 21)

---

## 6. CRONOGRAMA RECOMENDADO

| Fase | Ação | Prazo | Prioridade |
|------|------|-------|-----------|
| **IMEDIATO** | Aplicar mitigações temporárias | Hoje | 🔴 CRÍTICA |
| **CURTO PRAZO** | Planejar atualização de dependências | Esta semana | 🔴 CRÍTICA |
| **MÉDIO PRAZO** | Executar atualização em ambiente de staging | Próximas 2 semanas | 🟠 ALTA |
| **LONGO PRAZO** | Testar e fazer deploy em produção | Próximas 4 semanas | 🟠 ALTA |

---

## 7. CONCLUSÕES E RECOMENDAÇÕES FINAIS

### ✅ Backend (Maven)
- **Status:** SEGURO ✅
- **Ação:** Nenhuma urgente. Continuar monitorando.

### ⚠️ Frontend (npm)
- **Status:** VULNERÁVEL - 4 CVEs HIGH
- **Risco:** Alto - Vulnerabilidades XSS podem ser exploradas por atacantes para roubar dados e tokens
- **Recomendação:** 🔴 **ATUALIZAR COM URGÊNCIA**

### Plano de Ação Recomendado:
1. **Imediato (Hoje):** Implementar mitigações temporárias listadas na seção 4
2. **Curto Prazo (Esta Semana):** Começar planejamento de atualização para Angular 21+
3. **Médio Prazo (2 semanas):** Executar atualização em staging e validar
4. **Longo Prazo (4 semanas):** Deploy em produção após testes completos

---

## Apêndice: Informações de Contato e Suporte

Para mais informações sobre as vulnerabilidades:
- **CVE-2025-66035:** https://github.com/advisories/GHSA-58c5-g7wp-6w37
- **CVE-2025-66412:** https://github.com/advisories/GHSA-v4hv-rgfq-gp49
- **CVE-2026-22610:** https://github.com/advisories/GHSA-jrmj-c5cx-3cw6
- **CVE-2026-27970:** https://github.com/advisories/GHSA-prjf-86w9-mfqv

**Documentação Angular:**
- [Angular Security Guide](https://angular.dev/best-practices/security)
- [Angular i18n Security](https://angular.dev/guide/i18n)
- [Angular HttpClient XSRF Protection](https://angular.dev/guide/http#xsrf-protection)

---

**Relatório Compilado em:** 12 de Março de 2026  
**Versão do Relatório:** 1.0  
**Status:** ✅ Completo

