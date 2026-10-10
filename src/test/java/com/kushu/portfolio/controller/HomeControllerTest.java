package com.kushu.portfolio.controller;

import com.kushu.portfolio.service.PortfolioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HomeController.class)
@Import(PortfolioService.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

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
}
