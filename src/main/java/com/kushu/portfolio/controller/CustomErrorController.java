package com.kushu.portfolio.controller;

import com.kushu.portfolio.model.ProfileData;
import com.kushu.portfolio.service.PortfolioService;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller handling errors and rendering the bespoke 404 page.
 */
@Controller
public class CustomErrorController implements ErrorController {

    private final PortfolioService portfolioService;

    public CustomErrorController(PortfolioService portfolioService) {
        this.portfolioService = portfolioService;
    }

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        ProfileData profile = portfolioService.getProfileData();
        model.addAttribute("profile", profile);

        Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);
        if (status != null) {
            int statusCode = Integer.parseInt(status.toString());
            if (statusCode == HttpStatus.NOT_FOUND.value()) {
                return "error/404";
            }
        }
        return "error/404";
    }

    @RequestMapping("/404")
    public String notFoundPage(Model model) {
        ProfileData profile = portfolioService.getProfileData();
        model.addAttribute("profile", profile);
        return "404";
    }
}
