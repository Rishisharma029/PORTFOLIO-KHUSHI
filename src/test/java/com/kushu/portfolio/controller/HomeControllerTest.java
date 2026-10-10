package com.kushu.portfolio.controller;

import com.kushu.portfolio.service.ContactMessageRepository;
import com.kushu.portfolio.service.PortfolioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
@Import({PortfolioService.class, ContactMessageRepository.class})
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ContactMessageRepository messageRepository;

    @Test
    void shouldReturnIndexPageWithProfileData() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(content().string(containsString("Khushi Singh")))
                .andExpect(content().string(containsString("Java Full Stack Developer")))
                .andExpect(content().string(containsString("Her Learning Journey")))
                .andExpect(content().string(containsString("DRAFT — OWNER REVIEW")))
                .andExpect(content().string(containsString("COLLAB CONTRIBUTION")));
    }

    @Test
    void shouldSanitizeAndStoreContactSubmission() throws Exception {
        var getResult = mockMvc.perform(get("/")).andReturn();
        var session = (org.springframework.mock.web.MockHttpSession) getResult.getRequest().getSession(false);
        var csrfToken = (com.kushu.portfolio.security.CsrfToken) session.getAttribute(com.kushu.portfolio.security.CsrfSecurityFilter.CSRF_SESSION_KEY);

        String jsonPayload = """
                {
                    "name": "Jane Doe <script>alert(1)</script>",
                    "email": "jane.doe@example.com",
                    "subject": "Collaboration <b>Inquiry</b>",
                    "message": "Hello Khushi! <img src='x' onerror='alert(2)'/> Loved your portfolio."
                }
                """;

        mockMvc.perform(post("/api/contact")
                .session(session)
                .header(com.kushu.portfolio.security.CsrfSecurityFilter.CSRF_HEADER_NAME, csrfToken != null ? csrfToken.getToken() : "")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jsonPayload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("success"))
                .andExpect(jsonPath("$.message").value(containsString("Jane Doe")));

        // Verify message stored in sanitized form
        var messages = messageRepository.findAll();
        org.junit.jupiter.api.Assertions.assertFalse(messages.isEmpty());
        var stored = messages.get(0);
        org.junit.jupiter.api.Assertions.assertFalse(stored.getName().contains("<script>"));
        org.junit.jupiter.api.Assertions.assertTrue(stored.getName().contains("&lt;script&gt;"));
    }

    @Test
    void shouldRenderThankYouPage() throws Exception {
        mockMvc.perform(get("/thank-you"))
                .andExpect(status().isOk())
                .andExpect(view().name("thank-you"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(content().string(containsString("Thank You")))
                .andExpect(content().string(containsString("Connection Established")))
                .andExpect(content().string(containsString("Back to Portfolio")));
    }
}
