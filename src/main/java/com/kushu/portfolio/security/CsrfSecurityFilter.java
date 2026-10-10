package com.kushu.portfolio.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.Set;

/**
 * Filter that provides Cross-Site Request Forgery (CSRF) protection without
 * complex configuration or breaking changes.
 *
 * - Exposes "_csrf" token to Thymeleaf templates and request attributes.
 * - Sets an "XSRF-TOKEN" cookie for frontend JavaScript clients.
 * - Validates POST/PUT/DELETE requests against the active session token.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 10)
public class CsrfSecurityFilter extends OncePerRequestFilter {

    public static final String CSRF_SESSION_KEY = "PORTFOLIO_CSRF_TOKEN";
    public static final String CSRF_REQUEST_ATTR = "_csrf";
    public static final String CSRF_HEADER_NAME = "X-CSRF-TOKEN";
    public static final String CSRF_PARAM_NAME = "_csrf";
    public static final String XSRF_COOKIE_NAME = "XSRF-TOKEN";

    private static final Set<String> SAFE_METHODS = Set.of("GET", "HEAD", "OPTIONS", "TRACE");
    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        HttpSession session = request.getSession(true);
        CsrfToken csrfToken = (CsrfToken) session.getAttribute(CSRF_SESSION_KEY);

        if (csrfToken == null) {
            byte[] randomBytes = new byte[32];
            secureRandom.nextBytes(randomBytes);
            String tokenValue = Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes);
            csrfToken = new CsrfToken(tokenValue, CSRF_PARAM_NAME, CSRF_HEADER_NAME);
            session.setAttribute(CSRF_SESSION_KEY, csrfToken);
        }

        // Expose token to request attributes so Thymeleaf can access via ${_csrf.token}
        request.setAttribute(CSRF_REQUEST_ATTR, csrfToken);

        // Update cookie for frontend AJAX/Fetch calls
        Cookie xsrfCookie = new Cookie(XSRF_COOKIE_NAME, csrfToken.getToken());
        xsrfCookie.setPath("/");
        xsrfCookie.setHttpOnly(false); // Accessible to JavaScript for API calls
        xsrfCookie.setSecure(request.isSecure() || "https".equalsIgnoreCase(request.getHeader("X-Forwarded-Proto")));
        response.addCookie(xsrfCookie);

        String method = request.getMethod().toUpperCase();

        // Safe methods (GET, HEAD, OPTIONS) do not require CSRF validation
        if (SAFE_METHODS.contains(method)) {
            filterChain.doFilter(request, response);
            return;
        }

        // State-changing methods (POST, PUT, DELETE, PATCH): Validate token
        String receivedToken = request.getHeader(CSRF_HEADER_NAME);
        if (receivedToken == null || receivedToken.isBlank()) {
            receivedToken = request.getHeader("X-XSRF-TOKEN");
        }
        if (receivedToken == null || receivedToken.isBlank()) {
            receivedToken = request.getParameter(CSRF_PARAM_NAME);
        }

        if (receivedToken == null || !receivedToken.equals(csrfToken.getToken())) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid or missing CSRF token");
            return;
        }

        filterChain.doFilter(request, response);
    }
}
