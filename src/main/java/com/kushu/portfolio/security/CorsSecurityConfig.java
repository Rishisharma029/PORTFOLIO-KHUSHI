package com.kushu.portfolio.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Locked-down Cross-Origin Resource Sharing (CORS) configuration.
 *
 * Restricts cross-origin requests exclusively to trusted domains:
 * - Local development origins (localhost, 127.0.0.1)
 * - Khushi's GitHub Pages host (*.github.io)
 * - Verified cloud hosts (*.onrender.com, *.railway.app)
 *
 * Disallows arbitrary third-party websites from making unauthorized API calls or scraping private state.
 */
@Configuration
public class CorsSecurityConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(
                        "http://localhost:[*]",
                        "http://127.0.0.1:[*]",
                        "https://*.github.io",
                        "https://*.onrender.com",
                        "https://*.railway.app"
                )
                .allowedMethods("GET", "POST", "OPTIONS", "HEAD")
                .allowedHeaders("X-CSRF-TOKEN", "X-XSRF-TOKEN", "Content-Type", "Accept", "Origin", "Authorization")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
