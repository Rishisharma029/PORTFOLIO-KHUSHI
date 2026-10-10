package com.kushu.portfolio.security;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class InputSanitizerTest {

    @Test
    void shouldEscapeHtmlAndScriptTags() {
        String dangerous = "<script>alert('xss');</script><img src=\"x\" onerror=\"alert(1)\"/>";
        String sanitized = InputSanitizer.sanitizeText(dangerous);

        assertFalse(sanitized.contains("<script>"));
        assertFalse(sanitized.contains("</script>"));
        assertFalse(sanitized.contains("<img"));
        assertTrue(sanitized.contains("&lt;script&gt;"));
        assertTrue(sanitized.contains("&lt;img"));
    }

    @Test
    void shouldEnforceMaxCharacterLength() {
        String longText = "A".repeat(500);
        String sanitized = InputSanitizer.sanitizeText(longText, 50);

        assertEquals(50, sanitized.length());
    }

    @Test
    void shouldStripControlCharactersAndTrim() {
        String inputWithNull = "  Hello \u0000 World \t \n ";
        String sanitized = InputSanitizer.sanitizeText(inputWithNull);

        assertFalse(sanitized.contains("\u0000"));
        assertTrue(sanitized.startsWith("Hello"));
    }

    @Test
    void shouldSanitizeAndValidateEmail() {
        assertEquals("khushi@example.com", InputSanitizer.sanitizeEmail("  Khushi@Example.COM  "));
        assertEquals("user.name+tag@sub.domain.org", InputSanitizer.sanitizeEmail("user.name+tag@sub.domain.org"));

        // Reject invalid emails
        assertEquals("", InputSanitizer.sanitizeEmail("not-an-email"));
        assertEquals("", InputSanitizer.sanitizeEmail("user@domain\r\nBcc: victim@example.com"));
        assertEquals("", InputSanitizer.sanitizeEmail("<script>@domain.com"));
    }

    @Test
    void shouldSanitizeUrlAndRejectJavascript() {
        assertEquals("https://github.com/khushi", InputSanitizer.sanitizeUrl("https://github.com/khushi"));
        assertEquals("mailto:khushi@example.com", InputSanitizer.sanitizeUrl("mailto:khushi@example.com"));

        // Reject dangerous schemes
        assertEquals("", InputSanitizer.sanitizeUrl("javascript:alert(1)"));
        assertEquals("", InputSanitizer.sanitizeUrl("data:text/html,<script>alert(1)</script>"));
        assertEquals("", InputSanitizer.sanitizeUrl("vbscript:msgbox(1)"));
    }
}
