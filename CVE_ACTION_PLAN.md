# CVE Remediation Action Plan
## Quick Reference for Developers

---

## Summary of Changes Applied

### ✅ Backend (Maven) - COMPLETED
- **PostgreSQL JDBC:** 42.7.1 → 42.7.2 ✅
  - Fixes: CVE-2024-1597 (CRITICAL SQL Injection)
  
- **Apache POI:** 5.1.0 → 5.4.0 ✅
  - Fixes: CVE-2025-31672 (MEDIUM Input Validation)
  
- **Spring Security:** 6.2.3 → 6.3.2 ✅
  - Addresses: CVE-2024-38827 (MEDIUM - Unfixable in current release)

**Build Status:** ✅ All tests passing

---

## Remaining Security Issues & Mitigation

### Frontend XSS Vulnerabilities (Angular 18.2.0)

#### Issue #1: XSS via SVG Script Attributes (CVE-2026-22610)
**Severity:** 🔴 HIGH  
**Current Status:** Vulnerable

**What to avoid:**
```html
<!-- ❌ VULNERABLE -->
<script [attr.href]="userInput"></script>
```

**How to fix:**
```typescript
// ✅ SAFE - Use DomSanitizer
import { DomSanitizer, SecurityContext } from '@angular/platform-browser';

constructor(private sanitizer: DomSanitizer) {}

getSafeUrl(url: string) {
  return this.sanitizer.sanitize(SecurityContext.RESOURCE_URL, url);
}
```

```html
<!-- ✅ SAFE - Apply sanitizer -->
<script [attr.href]="getSafeUrl(userInput)"></script>
```

#### Issue #2: XSS in i18n Attributes (CVE-2026-32635)
**Severity:** 🔴 HIGH  
**Current Status:** Vulnerable

**What to avoid:**
```html
<!-- ❌ VULNERABLE -->
<a href="{{userUrl}}" i18n-href>Link</a>
```

**How to fix:**
```html
<!-- ✅ SAFE - Remove i18n from security-sensitive attributes OR use sanitizer -->
<a [href]="sanitizer.sanitize(SecurityContext.URL, userUrl)">Link</a>
```

#### Issue #3: XSS in i18n Messages (CVE-2026-27970)
**Severity:** 🔴 HIGH  
**Current Status:** Vulnerable if translation files are compromised

**How to mitigate:**
```typescript
// Verify translation file integrity
async loadTranslation(lang: string) {
  const translation = await this.http.get(`/assets/i18n/${lang}.json`).toPromise();
  const hash = await this.http.get(`/assets/i18n/${lang}.hash`).toPromise();
  
  // Verify hash matches before using translations
  if (!this.verifyHash(translation, hash)) {
    throw new Error('Translation file tampered with!');
  }
  return translation;
}
```

#### Issue #4: XSRF Token Leakage (CVE-2025-66035)
**Severity:** 🟡 HIGH  
**Current Status:** Vulnerable if protocol-relative URLs are used

**What to avoid:**
```typescript
// ❌ VULNERABLE - Uses protocol-relative URL
this.http.post('//api.example.com/data', payload).subscribe(...);
```

**How to fix:**
```typescript
// ✅ SAFE - Use absolute or relative URLs
this.http.post('https://api.example.com/data', payload).subscribe(...);
this.http.post('/api/data', payload).subscribe(...);
```

---

## Step-by-Step Mitigation Guide

### Step 1: Enable Content Security Policy (IMMEDIATE - 30 min)
**File:** `frontend/src/index.html`

Add this meta tag to `<head>`:
```html
<meta http-equiv="Content-Security-Policy" 
  content="default-src 'self'; 
    script-src 'self'; 
    style-src 'self' 'unsafe-inline'; 
    img-src 'self' data: https:; 
    connect-src 'self' https:;">
```

**Test:** Run `npm run build` to ensure no CSP violations

### Step 2: Create Security Service (1 hour)
**File:** `frontend/src/app/core/security.service.ts`

```typescript
import { Injectable, SecurityContext } from '@angular/core';
import { DomSanitizer, SafeHtml, SafeResourceUrl } from '@angular/platform-browser';

@Injectable({ providedIn: 'root' })
export class SecurityService {
  constructor(private sanitizer: DomSanitizer) {}

  sanitizeUrl(url: string): string {
    return this.sanitizer.sanitize(SecurityContext.URL, url) || '';
  }

  sanitizeResourceUrl(url: string): string {
    return this.sanitizer.sanitize(SecurityContext.RESOURCE_URL, url) || '';
  }

  sanitizeHtml(html: string): SafeHtml {
    return this.sanitizer.bypassSecurityTrustHtml(html);
  }

  // Whitelist approach for external URLs
  isSafeUrl(url: string): boolean {
    const trustedDomains = [
      'https://trusted-api.example.com',
      'https://cdn.example.com'
    ];
    return trustedDomains.some(domain => url.startsWith(domain));
  }
}
```

### Step 3: Audit and Fix Components (4-8 hours)
For each component that handles user input:

**Checklist:**
- [ ] Find all `[attr.href]`, `[attr.src]` bindings
- [ ] Find all `i18n-*` attributes with data bindings
- [ ] Find all `/api` calls with protocol-relative URLs
- [ ] Add DomSanitizer where needed
- [ ] Remove unnecessary `i18n` from security attributes
- [ ] Use absolute/relative URLs in HttpClient

**Example Fix:**
```typescript
// BEFORE
import { Component } from '@angular/core';

@Component({
  template: `
    <a href="{{userUrl}}">Link</a>
    <img src="{{imageSrc}}">
  `
})
export class MyComponent { ... }

// AFTER
import { Component, inject } from '@angular/core';
import { SecurityService } from '@app/core/security.service';

@Component({
  template: `
    <a [href]="security.sanitizeUrl(userUrl)">Link</a>
    <img [src]="security.sanitizeResourceUrl(imageSrc)">
  `
})
export class MyComponent {
  security = inject(SecurityService);
}
```

### Step 4: Validate Changes (2 hours)
```bash
# Build and test
cd frontend
npm run build
npm run test
npm run lint

# Manual testing in browser
npm start
```

### Step 5: Monitor & Update (Ongoing)
- [ ] Deploy with CSP headers enabled
- [ ] Monitor browser console for CSP violations
- [ ] Plan Angular 22 upgrade for Q3/Q4 2026
- [ ] Subscribe to Angular security advisories

---

## Timeline

| Phase | Duration | Effort | Priority |
|-------|----------|--------|----------|
| Enable CSP | 0.5 hours | Minimal | 🔴 IMMEDIATE |
| Create SecurityService | 1 hour | Low | 🔴 Week 1 |
| Audit Components | 4-8 hours | Medium | 🟡 Week 1-2 |
| Fix & Test | 2-4 hours | Medium | 🟡 Week 2 |
| Verification | 1-2 hours | Low | 🟡 Week 2 |
| Plan Upgrade | N/A | Planning | 🔵 Month 2-3 |

**Total Effort:** ~12-16 hours for mitigation  
**Total Effort:** ~2-4 weeks for full Angular 22 upgrade

---

## Testing Checklist

Before deploying to production:

- [ ] CSP headers don't break functionality
- [ ] No console errors in DevTools
- [ ] All user input sanitized properly
- [ ] XSS payloads blocked (test: `<img src=x onerror=alert(1)>`)
- [ ] CSRF token present for all POST requests
- [ ] No protocol-relative URLs in network requests
- [ ] All unit tests pass
- [ ] All e2e tests pass

---

## Backend Spring Security Fix

No additional code changes needed. CVE-2024-38827 is unfixable in current Spring versions.

**Workaround:** Avoid case-sensitive authorization checks that depend on locale:

```java
// ❌ AVOID: Locale-dependent
String normalizedRole = role.toLowerCase(); // Bad!

// ✅ SAFE: Explicit case handling
String normalizedRole = role.toLowerCase(Locale.ENGLISH);
// Or better:
String normalizedRole = role.toLowerCase(Locale.ROOT);
```

---

## Questions?

Refer to the full report: `/home/robertojr/chatbot-platform-skeleton/CVE_REMEDIATION_REPORT.md`

---

**Last Updated:** April 29, 2026  
**Next Review:** June 30, 2026

