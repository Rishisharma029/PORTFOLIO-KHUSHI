package com.kushu.portfolio.controller;

import com.kushu.portfolio.model.ContactMessage;
import com.kushu.portfolio.model.ContactRequest;
import com.kushu.portfolio.model.ProfileData;
import com.kushu.portfolio.security.InputSanitizer;
import com.kushu.portfolio.service.ContactMessageRepository;
import com.kushu.portfolio.service.PortfolioService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;
import java.util.UUID;

/**
 * Controller handling portfolio routes and providing profile data to Thymeleaf view.
 */
@Controller
public class HomeController {

    private final PortfolioService portfolioService;
    private final ContactMessageRepository messageRepository;

    public HomeController(PortfolioService portfolioService, ContactMessageRepository messageRepository) {
        this.portfolioService = portfolioService;
        this.messageRepository = messageRepository;
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

    /**
     * API endpoint for incoming contact messages.
     * Sanitizes all fields prior to storage to guarantee security.
     */
    @PostMapping("/api/contact")
    @ResponseBody
    public ResponseEntity<Map<String, String>> submitContact(@RequestBody(required = false) ContactRequest request) {
        if (request == null) {
            return ResponseEntity.badRequest().body(Map.of("status", "error", "message", "Request body cannot be empty."));
        }

        // 1. Sanitize all inputs before storing
        String sanitizedName = InputSanitizer.sanitizeText(request.getName(), 100);
        String sanitizedEmail = InputSanitizer.sanitizeEmail(request.getEmail());
        String sanitizedSubject = InputSanitizer.sanitizeText(request.getSubject(), 150);
        String sanitizedMessage = InputSanitizer.sanitizeText(request.getMessage(), 2000);

        if (sanitizedName.isBlank() || sanitizedEmail.isBlank() || sanitizedMessage.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of(
                    "status", "error",
                    "message", "Please provide a valid name, email address, and message."
            ));
        }

        // 2. Store sanitized message in thread-safe repository
        String messageId = UUID.randomUUID().toString();
        ContactMessage message = new ContactMessage(messageId, sanitizedName, sanitizedEmail, sanitizedSubject, sanitizedMessage);
        messageRepository.save(message);

        return ResponseEntity.ok(Map.of(
                "status", "success",
                "message", "Thank you, " + sanitizedName + "! Your message has been safely received."
        ));
    }
}
