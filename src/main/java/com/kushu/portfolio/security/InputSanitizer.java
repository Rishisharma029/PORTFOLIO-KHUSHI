package com.kushu.portfolio.security;

import java.util.regex.Pattern;

/**
 * Utility class for sanitizing and validating all incoming user inputs
 * prior to processing, memory caching, or persistent storage.
 *
 * Protects against:
 * - Cross-Site Scripting (XSS) via HTML entity encoding
 * - SQL / Script Injection via character filtering
 * - Header Injection (CRLF) in emails and subjects
 * - Unbounded memory / payload exhaustion via strict length bounds
 */
public final class InputSanitizer {

    private static final int DEFAULT_MAX_LENGTH = 1000;
    private static final Pattern EMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,63}$"
    );
    private static final Pattern SAFE_TEXT_PATTERN = Pattern.compile("[\\p{Cntrl}&&[^\r\n\t]]");

    private InputSanitizer() {
        // Utility class
    }

    /**
     * Sanitizes general text input by trimming, stripping control characters,
     * enforcing maximum length, and escaping HTML special characters.
     *
     * @param input raw user input
     * @param maxLength maximum allowed string length
     * @return clean, safe, HTML-escaped string
     */
    public static String sanitizeText(String input, int maxLength) {
        if (input == null) {
            return "";
        }

        // 1. Remove non-printable control characters (except tab/newline)
        String cleaned = SAFE_TEXT_PATTERN.matcher(input).replaceAll("");

        // 2. Trim surrounding whitespace
        cleaned = cleaned.trim();

        // 3. Enforce maximum length bound
        int max = maxLength > 0 ? maxLength : DEFAULT_MAX_LENGTH;
        if (cleaned.length() > max) {
            cleaned = cleaned.substring(0, max);
        }

        // 4. HTML entity escape to prevent XSS execution
        return escapeHtml(cleaned);
    }

    /**
     * Overload using default max length (1000 chars).
     */
    public static String sanitizeText(String input) {
        return sanitizeText(input, DEFAULT_MAX_LENGTH);
    }

    /**
     * Sanitizes and validates an email address.
     * Strips CRLF characters to prevent HTTP/Email Header Injection.
     *
     * @param email raw email string
     * @return safe lower-case email or empty string if invalid
     */
    public static String sanitizeEmail(String email) {
        if (email == null) {
            return "";
        }

        // Strip carriage returns and line feeds to prevent header injection
        String cleaned = email.replaceAll("[\\r\\n]", "").trim().toLowerCase();

        if (cleaned.length() > 254 || !EMAIL_PATTERN.matcher(cleaned).matches()) {
            return "";
        }

        return cleaned;
    }

    /**
     * Validates and sanitizes a URL, allowing only safe HTTP/HTTPS or mailto schemes.
     * Rejects javascript:, data:, and vbscript: URIs.
     */
    public static String sanitizeUrl(String url) {
        if (url == null) {
            return "";
        }
        String cleaned = url.replaceAll("[\\p{Cntrl}]", "").trim();
        String lower = cleaned.toLowerCase();

        if (lower.startsWith("https://") || lower.startsWith("http://") || lower.startsWith("mailto:")) {
            return cleaned;
        }
        return "";
    }

    /**
     * Escapes standard HTML special characters (&, <, >, ", ', /).
     */
    public static String escapeHtml(String text) {
        if (text == null || text.isEmpty()) {
            return "";
        }

        StringBuilder out = new StringBuilder(Math.max(16, text.length()));
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            switch (c) {
                case '&' -> out.append("&amp;");
                case '<' -> out.append("&lt;");
                case '>' -> out.append("&gt;");
                case '"' -> out.append("&quot;");
                case '\'' -> out.append("&#x27;");
                case '/' -> out.append("&#x2F;");
                default -> out.append(c);
            }
        }
        return out.toString();
    }
}
