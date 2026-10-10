# 🛡️ Security Architecture & Policy

Welcome to the official security documentation for **Khushi Singh's Portfolio** application. This document details the multi-layered security controls, defenses, header hardening, sanitization protocols, and vulnerability reporting procedures implemented across the application.

---

## 📑 Table of Contents
1. [Security Architecture Overview](#1-security-architecture-overview)
2. [Implemented Security Controls](#2-implemented-security-controls)
   - [HTTP Strict Transport Security (HSTS)](#http-strict-transport-security-hsts)
   - [Cross-Site Request Forgery (CSRF) Protection](#cross-site-request-forgery-csrf-protection)
   - [Pre-Storage Input Sanitization](#pre-storage-input-sanitization)
   - [Locked-Down Cross-Origin Resource Sharing (CORS)](#locked-down-cross-origin-resource-sharing-cors)
   - [Hardened HTTP Response Headers](#hardened-http-response-headers)
   - [Client-Side Content Protection & Anti-Scraping](#client-side-content-protection--anti-scraping)
   - [Safe Error Handling & Information Leakage Prevention](#safe-error-handling--information-leakage-prevention)
3. [Automated Security Testing Suite](#3-automated-security-testing-suite)
4. [Deployment & Secrets Security Checklist](#4-deployment--secrets-security-checklist)
5. [Reporting a Vulnerability](#5-reporting-a-vulnerability)

---

## 1. Security Architecture Overview

The application follows a **Defense-in-Depth** design philosophy where security is enforced at multiple layers:
- **HTTP / Network Transport Layer**: HSTS enforcement, HTTPS redirect, TLS 1.3/1.2 requirement.
- **Servlet Filter Layer**: `SecurityHeadersFilter` attaches strict security headers on every response.
- **Framework & Controller Layer**: Spring Security CSRF token validation, input validation, and secure dispatching.
- **Service & Domain Layer**: `InputSanitizer` cleans and strips malicious payloads before memory/storage processing.
- **Presentation / Browser Layer**: Content Security Policy (CSP), anti-clickjacking frame restrictions, and content selection/drag restrictions.

```
       [ Client / Web Browser ]
                  │
     (HTTPS / TLS / HSTS Preloaded)
                  │
                  ▼
      ┌─────────────────────────┐
      │  SecurityHeadersFilter  │ ──► [HSTS, CSP, X-Frame-Options, X-Content-Type, Permissions]
      └───────────┬─────────────┘
                  │
                  ▼
      ┌─────────────────────────┐
      │  Spring Security & CORS │ ──► [CSRF Token Validation & Origin Lockdown]
      └───────────┬─────────────┘
                  │
                  ▼
      ┌─────────────────────────┐
      │     InputSanitizer      │ ──► [XSS stripping, HTML escaping, Control char cleanup]
      └───────────┬─────────────┘
                  │
                  ▼
      ┌─────────────────────────┐
      │   Portfolio Controllers │ ──► [Thymeleaf Rendering & JSON Response]
      └─────────────────────────┘
```

---

## 2. Implemented Security Controls

### HTTP Strict Transport Security (HSTS)
- **Header**: `Strict-Transport-Security: max-age=31536000; includeSubDomains; preload`
- **Purpose**: Enforces 100% encrypted HTTPS connections for 1 full year (31,536,000 seconds), mandates HTTPS across all subdomains, and qualifies the domain for the global browser HSTS Preload list to block SSL-stripping and man-in-the-middle (MITM) attacks.

### Cross-Site Request Forgery (CSRF) Protection
- **Token Mechanism**: Standard Spring Security `CsrfTokenRepository` generating cryptographically random session-bound tokens.
- **Client Integration**:
  - Embedded via `<meta name="_csrf" th:content="${_csrf.token}">` and `<meta name="_csrf_header" th:content="${_csrf.headerName}">` meta tags.
  - Automatically attached to AJAX and state-modifying requests (`POST`, `PUT`, `DELETE`).
  - Read-only (`GET`, `HEAD`, `OPTIONS`) endpoints remain safe and idempotent.

### Pre-Storage Input Sanitization
- **Component**: `com.kushu.portfolio.security.InputSanitizer`
- **Capabilities**:
  - Strips dangerous HTML tags (`<script>`, `<iframe>`, `<object>`, `<embed>`, `<style>`, `<form>`, `<svg onload=...>`).
  - Strips inline event handlers (`onclick=`, `onload=`, `onerror=`, `onmouseover=`).
  - Removes null bytes (`\0`) and invisible non-printable ASCII control characters.
  - Truncates oversized input fields to prevent buffer exhaustion and DoS.
  - Validates and formats email addresses, names, and contact messages before any persistence or dispatching.

### Locked-Down Cross-Origin Resource Sharing (CORS)
- **Component**: `com.kushu.portfolio.config.SecurityConfig` / `CorsConfigurationSource`
- **Configuration**:
  - `allowedOrigins`: Restricted strictly to verified production and local domains:
    - `https://khushisingh.dev` (Production)
    - `https://kushusingh.github.io` (GitHub Pages)
    - `http://localhost:8080` (Local Development)
    - `http://127.0.0.1:8080`
  - `allowedMethods`: `GET`, `POST`, `OPTIONS`, `HEAD`.
  - `allowCredentials`: `true` for authorized origins only.
  - Wildcard origin (`*`) with credentials is explicitly forbidden.

### Hardened HTTP Response Headers
Attached globally to every HTTP response:

| Header | Value | Protection |
| :--- | :--- | :--- |
| **`Content-Security-Policy`** | `default-src 'self'; script-src 'self' 'unsafe-inline' https://fonts.googleapis.com; style-src 'self' 'unsafe-inline' https://fonts.googleapis.com; font-src 'self' https://fonts.gstatic.com; img-src 'self' data: https:; connect-src 'self'; frame-ancestors 'none';` | Mitigates Cross-Site Scripting (XSS) and unauthorized code execution. |
| **`X-Frame-Options`** | `DENY` | Completely prevents UI Redressing & Clickjacking in any iframe. |
| **`X-Content-Type-Options`** | `nosniff` | Blocks MIME-type sniffing by browsers. |
| **`Referrer-Policy`** | `strict-origin-when-cross-origin` | Protects visitor URLs and sensitive query parameters from leaking. |
| **`Permissions-Policy`** | `camera=(), microphone=(), geolocation=(), payment=(), usb=()` | Disables access to sensitive browser hardware APIs. |
| **`X-XSS-Protection`** | `1; mode=block` | Legacy XSS filter activation for older browsers. |

### Client-Side Content Protection & Anti-Scraping
To protect Khushi's intellectual property, resume, certificates, and curated portfolio materials:
1. **Right-Click Context Menu**: Disabled globally (`contextmenu` event interception).
2. **Text & Card Selection**: Disabled via CSS (`user-select: none`) and JS (`selectstart` blocking).
3. **Element Dragging**: Disabled via `-webkit-user-drag: none` and `dragstart` blocking.
4. **Copy & Inspect Deterrents**: Intercepted browser shortcuts (`Ctrl+A`, `Ctrl+C`, `Ctrl+U`, `Ctrl+S`, `F12`, `Ctrl+Shift+I`).
5. **Interactive Controls**: Dedicated interactive buttons (e.g. Share link to clipboard, View certificate verify links) use explicit JavaScript Clipboard APIs safely without requiring raw text selection.

### Safe Error Handling & Information Leakage Prevention
- **Custom Error Page**: [`src/main/resources/templates/error/404.html`](src/main/resources/templates/error/404.html) handles missing routes cleanly with Khushi's plum theme.
- **Server Stack Traces**: `server.error.include-stacktrace=never` and `server.error.include-message=never` are configured in `application.properties` to ensure zero internal server architecture or framework details leak to end-users.

---

## 3. Automated Security Testing Suite

All security configurations are backed by comprehensive, automated JUnit 5 and Spring Boot integration tests:

| Test Suite | Test Class | Coverage |
| :--- | :--- | :--- |
| **Security Headers & HSTS** | `SecurityFilterTest.java` | Verifies presence of HSTS, CSP, X-Frame-Options, X-Content-Type, and Referrer-Policy on all routes. |
| **Input Sanitization** | `InputSanitizerTest.java` | Tests against XSS vectors, script tags, event handlers, control characters, and length limits. |
| **CORS Policy** | `SecurityFilterTest.java` | Tests valid origins acceptance and unauthorized origin rejection. |
| **Custom Error Handling** | `CustomErrorControllerTest.java` | Verifies 404 responses render the themed error template without stack trace leakage. |
| **Static Export Integrity** | `StaticSiteExportTest.java` | Verifies the standalone static build maintains structure, robots.txt, sitemap.xml, and secure headers. |

To run the full security test suite locally:
```powershell
.\mvnw.cmd test
```

---

## 4. Deployment & Secrets Security Checklist

When deploying Khushi's portfolio to production or GitHub:
- [x] **Zero Hardcoded Secrets**: No passwords, database credentials, or secret API keys exist in version control.
- [x] **Dependencies Kept Updated**: Built on latest Spring Boot 3.3.4 and Java 21 LTS with regular Dependabot checks.
- [x] **Static & Dynamic Modes**: GitHub Pages uses static compilation (`dist/`), eliminating server-side attack surface entirely.
- [x] **HTTPS Only**: Ensure DNS and host (GitHub Pages / Render / Railway / Custom Domain) have SSL/TLS certificates enabled.

---

## 5. Reporting a Vulnerability

Security and privacy are taken seriously. If you discover a potential security vulnerability in this project, please report it responsibly:

1. **Email**: Reach out directly via email to `khushisingh.work@gmail.com` or connect via [LinkedIn](https://www.linkedin.com/in/khushi-singh-0a9a9738a/).
2. **Details**: Please include:
   - Description of the issue
   - Steps to reproduce the vulnerability
   - Potential impact
3. **Response Window**: You will receive an acknowledgment within 24–48 hours.

Thank you for helping keep Khushi Singh's portfolio secure! 💜
