package com.kushu.portfolio.controller;

import com.kushu.portfolio.service.ContactMessageRepository;
import com.kushu.portfolio.service.PortfolioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CustomErrorController.class)
@Import({PortfolioService.class, ContactMessageRepository.class})
public class CustomErrorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldRender404PageWithBespokeStyles() throws Exception {
        mockMvc.perform(get("/404"))
                .andExpect(status().isOk())
                .andExpect(view().name("404"))
                .andExpect(model().attributeExists("profile"))
                .andExpect(content().string(containsString("404")))
                .andExpect(content().string(containsString("ventured beyond the codebase")))
                .andExpect(content().string(containsString("Return to Homepage")));
    }
}
