package com.demo.aireportstudio.services;

import com.demo.aireportstudio.model.Knowledge;
import com.demo.aireportstudio.repository.KnowledgeRepository;
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
public class KnowledgeService {

    private static final Logger logger = LoggerFactory.getLogger(KnowledgeService.class);

    @Autowired
    private KnowledgeRepository knowledgeRepository;

    public List<Knowledge> findAll() {
        logger.debug("Finding all active knowledge entries");
        List<Knowledge> knowledgeList = knowledgeRepository.findAllByIsDeletedFalse();
        logger.debug("Found {} active knowledge entries", knowledgeList.size());
        return knowledgeList;
    }

    public Optional<Knowledge> findById(String id) {
        logger.debug("Finding knowledge by ID: {}", id);
        if (!StringUtils.hasText(id)) {
            logger.warn("Attempted to find knowledge with null or empty ID");
            return Optional.empty();
        }
        Optional<Knowledge> knowledge = knowledgeRepository.findByKnowledgeIdAndIsDeletedFalse(id);
        if (knowledge.isPresent()) {
            logger.debug("Found active knowledge with ID: {}", id);
        } else {
            logger.debug("No active knowledge found with ID: {}", id);
        }
        return knowledge;
    }

    public Optional<Knowledge> findByTitle(String title) {
        logger.debug("Finding knowledge by title: {}", title);
        if (!StringUtils.hasText(title)) {
            logger.warn("Attempted to find knowledge with null or empty title");
            return Optional.empty();
        }
        Optional<Knowledge> knowledge = knowledgeRepository.findByTitleAndIsDeletedFalse(title);
        if (knowledge.isPresent()) {
            logger.debug("Found active knowledge with title: {}", title);
        } else {
            logger.debug("No active knowledge found with title: {}", title);
        }
        return knowledge;
    }

    public Knowledge save(Knowledge knowledge) {
        logger.debug("Saving knowledge: {}", knowledge);

        // Ensure isDeleted is set for new entities
        if (knowledge.getIsDeleted() == null) {
            knowledge.setIsDeleted(false);
        }

        // Validate input
        validateKnowledge(knowledge);

        // Check for duplicate titles
        validateUniqueTitle(knowledge);

        Knowledge savedKnowledge = knowledgeRepository.save(knowledge);
        logger.info("Successfully saved knowledge with ID: {}", savedKnowledge.getKnowledgeId());
        return savedKnowledge;
    }

    public void deleteById(String id) {
        logger.debug("Deleting knowledge by ID: {}", id);
        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("Knowledge ID cannot be null or empty");
        }

        if (!knowledgeRepository.existsById(id)) {
            logger.warn("Attempted to delete non-existent knowledge with ID: {}", id);
            throw new IllegalArgumentException("Knowledge with ID '" + id + "' does not exist");
        }

        knowledgeRepository.deleteById(id);
        logger.info("Successfully deleted knowledge with ID: {}", id);
    }

    public boolean existsByTitle(String title) {
        logger.debug("Checking if active knowledge exists with title: {}", title);
        if (!StringUtils.hasText(title)) {
            return false;
        }
        return knowledgeRepository.existsByTitleAndIsDeletedFalse(title);
    }

    public long count() {
        long count = knowledgeRepository.count();
        logger.debug("Total knowledge entries count: {}", count);
        return count;
    }

    public List<Knowledge> findByTitleContaining(String keyword) {
        logger.debug("Finding active knowledge entries containing keyword: {}", keyword);
        if (!StringUtils.hasText(keyword)) {
            return List.of();
        }
        List<Knowledge> results = knowledgeRepository.findAllByIsDeletedFalse().stream()
                .filter(k -> k.getTitle().toLowerCase().contains(keyword.toLowerCase()) ||
                           k.getContent().toLowerCase().contains(keyword.toLowerCase()))
                .toList();
        logger.debug("Found {} active knowledge entries containing keyword: {}", results.size(), keyword);
        return results;
    }

    private void validateKnowledge(Knowledge knowledge) {
        if (knowledge == null) {
            throw new IllegalArgumentException("Knowledge cannot be null");
        }

        if (!StringUtils.hasText(knowledge.getTitle())) {
            throw new IllegalArgumentException("Knowledge title cannot be null or empty");
        }

        if (!StringUtils.hasText(knowledge.getContent())) {
            throw new IllegalArgumentException("Knowledge content cannot be null or empty");
        }

        // Validate title length
        if (knowledge.getTitle().length() > 500) {
            throw new IllegalArgumentException("Knowledge title cannot exceed 500 characters");
        }
    }

    private void validateUniqueTitle(Knowledge knowledge) {
        Optional<Knowledge> existing = knowledgeRepository.findByTitleAndIsDeletedFalse(knowledge.getTitle());

        if (knowledge.getKnowledgeId() == null || knowledge.getKnowledgeId().isEmpty()) {
            // New entry
            if (existing.isPresent()) {
                throw new IllegalArgumentException("Knowledge with title '" + knowledge.getTitle() + "' already exists");
            }
        } else {
            // Update - check if title conflicts with other entries
            if (existing.isPresent() && !existing.get().getKnowledgeId().equals(knowledge.getKnowledgeId())) {
                throw new IllegalArgumentException("Knowledge with title '" + knowledge.getTitle() + "' already exists");
            }
        }
    }
}
