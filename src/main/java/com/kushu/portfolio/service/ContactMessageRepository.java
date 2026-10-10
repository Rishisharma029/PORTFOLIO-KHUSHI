package com.kushu.portfolio.service;

import com.kushu.portfolio.model.ContactMessage;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * Thread-safe in-memory repository for storing sanitized contact messages.
 * Uses a bounded FIFO deque to prevent unbounded memory growth.
 */
@Repository
public class ContactMessageRepository {

    private static final int MAX_STORED_MESSAGES = 100;
    private final ConcurrentLinkedDeque<ContactMessage> messages = new ConcurrentLinkedDeque<>();

    public void save(ContactMessage message) {
        if (message == null) {
            return;
        }
        messages.addFirst(message);
        while (messages.size() > MAX_STORED_MESSAGES) {
            messages.removeLast();
        }
    }

    public List<ContactMessage> findAll() {
        return Collections.unmodifiableList(new ArrayList<>(messages));
    }

    public int count() {
        return messages.size();
    }
}
