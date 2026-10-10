package com.kushu.portfolio.model;

import java.time.Instant;

/**
 * Model representing a sanitized incoming contact or feedback message.
 */
public class ContactMessage {

    private final String id;
    private final String name;
    private final String email;
    private final String subject;
    private final String message;
    private final Instant receivedAt;

    public ContactMessage(String id, String name, String email, String subject, String message) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.subject = subject;
        this.message = message;
        this.receivedAt = Instant.now();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }

    public Instant getReceivedAt() {
        return receivedAt;
    }
}
