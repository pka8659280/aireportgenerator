package com.demo.aireportstudio.services;

import com.demo.aireportstudio.model.UserPrompt;
import com.demo.aireportstudio.repository.UserPromptRepository;
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
public class UserPromptService {

    private static final Logger logger = LoggerFactory.getLogger(UserPromptService.class);

    @Autowired
    private UserPromptRepository userPromptRepository;

    public List<UserPrompt> findAll() {
        logger.debug("Finding all active user prompts");
        List<UserPrompt> prompts = userPromptRepository.findAllByIsDeletedFalse();
        logger.debug("Found {} active user prompts", prompts.size());
        return prompts;
    }

    public Optional<UserPrompt> findById(String id) {
        logger.debug("Finding user prompt by ID: {}", id);
        if (!StringUtils.hasText(id)) {
            logger.warn("Attempted to find user prompt with null or empty ID");
            return Optional.empty();
        }
        Optional<UserPrompt> prompt = userPromptRepository.findByUserPromptIdAndIsDeletedFalse(id);
        if (prompt.isPresent()) {
            logger.debug("Found active user prompt with ID: {}", id);
        } else {
            logger.debug("No active user prompt found with ID: {}", id);
        }
        return prompt;
    }

    public Optional<UserPrompt> findByTitle(String title) {
        logger.debug("Finding user prompt by title: {}", title);
        if (!StringUtils.hasText(title)) {
            logger.warn("Attempted to find user prompt with null or empty title");
            return Optional.empty();
        }
        Optional<UserPrompt> prompt = userPromptRepository.findByTitleAndIsDeletedFalse(title);
        if (prompt.isPresent()) {
            logger.debug("Found active user prompt with title: {}", title);
        } else {
            logger.debug("No active user prompt found with title: {}", title);
        }
        return prompt;
    }

    public UserPrompt save(UserPrompt userPrompt) {
        logger.debug("Saving user prompt: {}", userPrompt);

        // Ensure isDeleted is set for new entities
        if (userPrompt.getIsDeleted() == null) {
            userPrompt.setIsDeleted(false);
        }

        // Validate input
        validateUserPrompt(userPrompt);

        // Check for duplicate titles
        validateUniqueTitle(userPrompt);

        UserPrompt savedPrompt = userPromptRepository.save(userPrompt);
        logger.info("Successfully saved user prompt with ID: {}", savedPrompt.getUserPromptId());
        return savedPrompt;
    }

    public void deleteById(String id) {
        logger.debug("Deleting user prompt by ID: {}", id);
        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("User prompt ID cannot be null or empty");
        }

        if (!userPromptRepository.existsById(id)) {
            logger.warn("Attempted to delete non-existent user prompt with ID: {}", id);
            throw new IllegalArgumentException("User prompt with ID '" + id + "' does not exist");
        }

        userPromptRepository.deleteById(id);
        logger.info("Successfully deleted user prompt with ID: {}", id);
    }

    public boolean existsByTitle(String title) {
        logger.debug("Checking if active user prompt exists with title: {}", title);
        if (!StringUtils.hasText(title)) {
            return false;
        }
        return userPromptRepository.existsByTitleAndIsDeletedFalse(title);
    }

    public long count() {
        long count = userPromptRepository.count();
        logger.debug("Total user prompts count: {}", count);
        return count;
    }

    public List<UserPrompt> findByTitleContaining(String keyword) {
        logger.debug("Finding active user prompts containing keyword: {}", keyword);
        if (!StringUtils.hasText(keyword)) {
            return List.of();
        }
        List<UserPrompt> results = userPromptRepository.findAllByIsDeletedFalse().stream()
                .filter(p -> p.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                           p.getContent().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
        logger.debug("Found {} active user prompts containing keyword: {}", results.size(), keyword);
        return results;
    }

    private void validateUserPrompt(UserPrompt userPrompt) {
        if (userPrompt == null) {
            throw new IllegalArgumentException("User prompt cannot be null");
        }

        if (!StringUtils.hasText(userPrompt.getTitle())) {
            throw new IllegalArgumentException("User prompt title cannot be null or empty");
        }

        if (!StringUtils.hasText(userPrompt.getContent())) {
            throw new IllegalArgumentException("User prompt content cannot be null or empty");
        }

        // Validate title length
        if (userPrompt.getTitle().length() > 200) {
            throw new IllegalArgumentException("User prompt title cannot exceed 200 characters");
        }
    }

    private void validateUniqueTitle(UserPrompt userPrompt) {
        Optional<UserPrompt> existing = userPromptRepository.findByTitleAndIsDeletedFalse(userPrompt.getTitle());

        if (userPrompt.getUserPromptId() == null || userPrompt.getUserPromptId().isEmpty()) {
            // New entry
            if (existing.isPresent()) {
                throw new IllegalArgumentException("User prompt with title '" + userPrompt.getTitle() + "' already exists");
            }
        } else {
            // Update - check if title conflicts with other entries
            if (existing.isPresent() && !existing.get().getUserPromptId().equals(userPrompt.getUserPromptId())) {
                throw new IllegalArgumentException("User prompt with title '" + userPrompt.getTitle() + "' already exists");
            }
        }
    }
}
