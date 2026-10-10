package com.kushu.portfolio.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filter that applies enterprise-standard HTTP security headers, including
 * HTTP Strict Transport Security (HSTS), X-Content-Type-Options, X-Frame-Options,
 * and Referrer-Policy.
 *
 * Designed to be 100% safe for both local development (HTTP) and production (HTTPS).
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SecurityHeadersFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 1. HTTP Strict Transport Security (HSTS)
        // Enabled for HTTPS requests or when behind a reverse proxy/load balancer (e.g. Render, Railway, Cloudflare)
        boolean isSecure = request.isSecure() || "https".equalsIgnoreCase(request.getHeader("X-Forwarded-Proto"));
        if (isSecure) {
            response.setHeader("Strict-Transport-Security", "max-age=31536000; includeSubDomains; preload");
        }

        // 2. Prevent MIME-type sniffing
        response.setHeader("X-Content-Type-Options", "nosniff");

        // 3. Clickjacking Protection
        response.setHeader("X-Frame-Options", "SAMEORIGIN");

        // 4. Referrer Policy
        response.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");

        // 5. Cross-Origin-Opener-Policy
        response.setHeader("Cross-Origin-Opener-Policy", "same-origin");

        filterChain.doFilter(request, response);
    }
}
