package com.kushu.portfolio.controller;

import com.kushu.portfolio.model.ProfileData;
import com.kushu.portfolio.service.PortfolioService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller handling portfolio routes and providing profile data to Thymeleaf view.
 */
@Controller
public class HomeController {

    private final PortfolioService portfolioService;

    public HomeController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    /**
     * Renders the single-page portfolio.
     */
    @GetMapping("/")
    public String index(Model model) {
        ProfileData profile = portfolioService.getProfileData();
        model.addAttribute("profile", profile);
        return "index";
    }
}
