package com.demo.aireportstudio.services;

import com.demo.aireportstudio.model.SystemPrompt;
import com.demo.aireportstudio.repository.SystemPromptRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SystemPromptService {

    private static final Logger logger = LoggerFactory.getLogger(SystemPromptService.class);

    @Autowired
    private SystemPromptRepository systemPromptRepository;

    public List<SystemPrompt> findAll() {
        logger.debug("Finding all active system prompts");
        List<SystemPrompt> prompts = systemPromptRepository.findAllByIsDeletedFalse();
        logger.debug("Found {} active system prompts", prompts.size());
        return prompts;
    }

    public Optional<SystemPrompt> findById(String id) {
        logger.debug("Finding system prompt by ID: {}", id);
        if (!StringUtils.hasText(id)) {
            logger.warn("Attempted to find system prompt with null or empty ID");
            return Optional.empty();
        }
        Optional<SystemPrompt> prompt = systemPromptRepository.findBySystemPromptIdAndIsDeletedFalse(id);
        if (prompt.isPresent()) {
            logger.debug("Found active system prompt with ID: {}", id);
        } else {
            logger.debug("No active system prompt found with ID: {}", id);
        }
        return prompt;
    }

    public Optional<SystemPrompt> findByTitle(String title) {
        logger.debug("Finding system prompt by title: {}", title);
        if (!StringUtils.hasText(title)) {
            logger.warn("Attempted to find system prompt with null or empty title");
            return Optional.empty();
        }
        Optional<SystemPrompt> prompt = systemPromptRepository.findByTitleAndIsDeletedFalse(title);
        if (prompt.isPresent()) {
            logger.debug("Found active system prompt with title: {}", title);
        } else {
            logger.debug("No active system prompt found with title: {}", title);
        }
        return prompt;
    }

    public SystemPrompt save(SystemPrompt systemPrompt) {
        logger.debug("Saving system prompt: {}", systemPrompt);

        // Ensure isDeleted is set for new entities
        if (systemPrompt.getIsDeleted() == null) {
            systemPrompt.setIsDeleted(false);
        }

        // Validate input
        validateSystemPrompt(systemPrompt);

        // Check for duplicate titles
        validateUniqueTitle(systemPrompt);

        SystemPrompt savedPrompt = systemPromptRepository.save(systemPrompt);
        logger.info("Successfully saved system prompt with ID: {}", savedPrompt.getSystemPromptId());
        return savedPrompt;
    }

    public void deleteById(String id) {
        logger.debug("Deleting system prompt by ID: {}", id);
        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("System prompt ID cannot be null or empty");
        }

        if (!systemPromptRepository.existsById(id)) {
            logger.warn("Attempted to delete non-existent system prompt with ID: {}", id);
            throw new IllegalArgumentException("System prompt with ID '" + id + "' does not exist");
        }

        systemPromptRepository.deleteById(id);
        logger.info("Successfully deleted system prompt with ID: {}", id);
    }

    public boolean existsByTitle(String title) {
        logger.debug("Checking if active system prompt exists with title: {}", title);
        if (!StringUtils.hasText(title)) {
            return false;
        }
        return systemPromptRepository.existsByTitleAndIsDeletedFalse(title);
    }

    public long count() {
        long count = systemPromptRepository.count();
        logger.debug("Total system prompts count: {}", count);
        return count;
    }

    public List<SystemPrompt> findByTitleContaining(String keyword) {
        logger.debug("Finding active system prompts containing keyword: {}", keyword);
        if (!StringUtils.hasText(keyword)) {
            return List.of();
        }
        List<SystemPrompt> results = systemPromptRepository.findAllByIsDeletedFalse().stream()
                .filter(p -> p.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                           p.getContent().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
        logger.debug("Found {} active system prompts containing keyword: {}", results.size(), keyword);
        return results;
    }

    private void validateSystemPrompt(SystemPrompt systemPrompt) {
        if (systemPrompt == null) {
            throw new IllegalArgumentException("System prompt cannot be null");
        }

        if (!StringUtils.hasText(systemPrompt.getTitle())) {
            throw new IllegalArgumentException("System prompt title cannot be null or empty");
        }

        if (!StringUtils.hasText(systemPrompt.getContent())) {
            throw new IllegalArgumentException("System prompt content cannot be null or empty");
        }

        // Validate title length
        if (systemPrompt.getTitle().length() > 200) {
            throw new IllegalArgumentException("System prompt title cannot exceed 200 characters");
        }
    }

    private void validateUniqueTitle(SystemPrompt systemPrompt) {
        Optional<SystemPrompt> existing = systemPromptRepository.findByTitleAndIsDeletedFalse(systemPrompt.getTitle());

        if (systemPrompt.getSystemPromptId() == null || systemPrompt.getSystemPromptId().isEmpty()) {
            // New entry
            if (existing.isPresent()) {
                throw new IllegalArgumentException("System prompt with title '" + systemPrompt.getTitle() + "' already exists");
            }
        } else {
            // Update - check if title conflicts with other entries
            if (existing.isPresent() && !existing.get().getSystemPromptId().equals(systemPrompt.getSystemPromptId())) {
                throw new IllegalArgumentException("System prompt with title '" + systemPrompt.getTitle() + "' already exists");
            }
        }
    }
}
