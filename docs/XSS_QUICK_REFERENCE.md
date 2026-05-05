# XSS Security Quick Reference Guide

## 🎯 Status Summary

| Component | Status | Details |
|-----------|--------|---------|
| **Backend Sanitization** | ✅ Implemented | InputSanitizer utility with HTML entity encoding |
| **Security Headers** | ✅ Configured | CSP, X-Frame-Options, X-XSS-Protection set |
| **CVE-2024-1597** | ✅ Fixed | PostgreSQL JDBC 42.7.2 (SQL Injection) |
| **CVE-2025-31672** | ✅ Fixed | Apache POI 5.4.0 (OOXML validation) |
| **CVE-2024-38827** | ⚠️ Monitoring | Spring Security (persists in 6.x) |
| **Angular XSS CVEs** | ⚠️ Code Fix Needed | 4 vulnerabilities (CVE-2026-22610, etc.) |

---

## 🚀 Quick Wins (Do These First)

### 1. Improve CSP Header (5 min)
**File:** `src/main/java/com/br/chatbotplatformskeleton/config/SecurityHeadersConfig.java` (line 98)

**Change:**
```java
// FROM:
"script-src 'self' 'unsafe-inline' 'unsafe-eval'; " +

// TO:
"script-src 'self'; " +
```

### 2. Audit Dangerous Angular Patterns (1 hour)

Search for these patterns in `frontend/src/**/*.component.html`:

```bash
# Pattern 1: SVG with dynamic attributes
grep -r '\[attr\.xlink:href\]\|xlink:href.*{{' frontend/src

# Pattern 2: innerHTML with user data
grep -r '\[innerHTML\]=.*userInput\|\[innerHTML\]=.*data' frontend/src

# Pattern 3: Protocol-relative URLs
grep -r "'//" frontend/src

# Pattern 4: i18n with HTML
grep -r '\[i18n.*href\]\|\[attr\..*href\].*i18n' frontend/src
```

### 3. Create Security Service (30 min)

Create `frontend/src/app/services/security.service.ts`:

```typescript
import { Injectable, SecurityContext } from '@angular/core';
import { DomSanitizer } from '@angular/platform-browser';

@Injectable({
  providedIn: 'root'
})
export class SecurityService {
  private readonly ALLOWED_URLS = [
    'https://trusted-cdn.com',
    'https://our-domain.com'
  ];

  constructor(private sanitizer: DomSanitizer) {}

  isValidUrl(url: string): boolean {
    try {
      const urlObj = new URL(url);
      return this.ALLOWED_URLS.some(
        allowed => urlObj.origin === new URL(allowed).origin
      );
    } catch {
      return false;
    }
  }

  sanitizeHtml(html: string): string {
    return this.sanitizer.sanitize(SecurityContext.HTML, html) || '';
  }

  sanitizeUrl(url: string): string {
    return this.sanitizer.sanitize(SecurityContext.RESOURCE_URL, url) || '';
  }
}
```

---

## 🔍 Where XSS Can Happen

### Backend (Protected ✅)
- ❌ Form validation error messages → ✅ Encoded in ApiExceptionHandler
- ❌ User input in database → ✅ Checked by InputSanitizer
- ❌ URLs in responses → ✅ Validated by isSafeUrl()

### Frontend (At Risk ⚠️)
- SVG elements with `[attr.href]` → Must validate & sanitize
- `[innerHTML]` with user data → Must use DomSanitizer
- i18n bindings with attributes → Must avoid mixing
- `//` protocol-relative URLs → Must use absolute paths

---

## ✅ Minimal Security Checklist

- [x] InputSanitizer implemented
- [x] CSP headers configured
- [x] Security vulnerabilities in dependencies fixed
- [ ] Audit Angular templates for XSS patterns
- [ ] Create SecurityService for centralized sanitization
- [ ] Fix 4 XSS patterns in components
- [ ] Enable Trusted Types API
- [ ] Add security tests for critical paths

---

## 📖 Code Examples

### Safe: Static Content
```html
<!-- ✅ Safe: No user input -->
<svg>
  <image xlink:href="assets/logo.svg"></image>
</svg>
```

### Unsafe: Dynamic Content Without Sanitization
```html
<!-- ❌ UNSAFE: Data binding to SVG -->
<svg>
  <image [attr.xlink:href]="userUrl"></image>
</svg>
```

### Safe: Sanitized Dynamic Content
```html
<!-- ✅ Safe: Sanitized URL -->
<svg>
  <image [attr.xlink:href]="sanitizedUrl"></image>
</svg>

<!-- Component -->
<code>
this.sanitizedUrl = this.sanitizer.bypassSecurityTrustResourceUrl(
  this.validateUrl(userUrl)
);

private validateUrl(url: string): string {
  // Whitelist validation
  if (!this.isValidImageUrl(url)) {
    return 'assets/placeholder.svg';
  }
  return url;
}
</code>
```

---

## 🧪 Test Your XSS Protection

```bash
# Test 1: HTML Entity Encoding
curl -X POST http://localhost:8080/api/test \
  -d 'input=<script>alert("XSS")</script>'

# Should return encoded HTML entities, not executable script

# Test 2: CSP Header Check
curl -I http://localhost:8080/api/auth/login \
  | grep -i "content-security-policy"

# Should see CSP header with 'self' restrictions
```

---

## 📞 Questions & Escalations

**Q: Do I need to update Angular?**  
A: Not immediately. Code-level fixes work now. Full upgrade to Angular 22 LTS recommended for Q3 2026.

**Q: What about the Spring Security CVE?**  
A: It persists in 6.x versions. Monitored for Spring Security 7.x patch. Not blocking.

**Q: How critical are the Angular XSS vulnerabilities?**  
A: HIGH severity but only exploitable if you use vulnerable patterns. Fix by next sprint.

**Q: Do I need Trusted Types?**  
A: Recommended but not required. Adds extra protection layer.

---

## 📚 Documentation Files

- **XSS_SECURITY_SUMMARY.md** - This comprehensive guide
- **CVE_REMEDIATION_REPORT.md** - Full technical CVE analysis
- **CVE_ACTION_PLAN.md** - Step-by-step remediation steps

All files are in project root.

---

**Last Updated:** April 29, 2026

