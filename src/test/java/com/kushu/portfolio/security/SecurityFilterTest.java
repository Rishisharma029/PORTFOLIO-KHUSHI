package com.kushu.portfolio.security;

import com.kushu.portfolio.controller.HomeController;
import com.kushu.portfolio.service.PortfolioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(HomeController.class)
@Import({PortfolioService.class, SecurityHeadersFilter.class, CsrfSecurityFilter.class})
public class SecurityFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldIncludeStandardSecurityHeadersOnGet() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(header().string("X-Content-Type-Options", "nosniff"))
                .andExpect(header().string("X-Frame-Options", "SAMEORIGIN"))
                .andExpect(header().string("Referrer-Policy", "strict-origin-when-cross-origin"));
    }

    @Test
    void shouldIncludeHstsHeaderWhenHttps() throws Exception {
        mockMvc.perform(get("/").header("X-Forwarded-Proto", "https"))
                .andExpect(status().isOk())
                .andExpect(header().string("Strict-Transport-Security", "max-age=31536000; includeSubDomains; preload"));
    }

    @Test
    void shouldGenerateCsrfTokenAndExposeInSession() throws Exception {
        MvcResult result = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn();

        MockHttpSession session = (MockHttpSession) result.getRequest().getSession(false);
        assertNotNull(session, "Session should be created for CSRF storage");
        CsrfToken csrfToken = (CsrfToken) session.getAttribute(CsrfSecurityFilter.CSRF_SESSION_KEY);
        assertNotNull(csrfToken, "CSRF token should be populated in session");
    }

    @Test
    void shouldRejectPostWithoutCsrfToken() throws Exception {
        mockMvc.perform(post("/api/contact"))
                .andExpect(status().isForbidden());
    }

    @Test
    void shouldAllowPostWithValidCsrfHeader() throws Exception {
        MvcResult getResult = mockMvc.perform(get("/")).andReturn();
        MockHttpSession session = (MockHttpSession) getResult.getRequest().getSession(false);
        CsrfToken csrfToken = (CsrfToken) session.getAttribute(CsrfSecurityFilter.CSRF_SESSION_KEY);

        mockMvc.perform(post("/api/contact")
                .session(session)
                .header(CsrfSecurityFilter.CSRF_HEADER_NAME, csrfToken.getToken()))
                .andExpect(status().isOk());
    }
}
