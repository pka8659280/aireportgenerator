package com.demo.aireportstudio.repository;

import com.demo.aireportstudio.model.SystemPrompt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SystemPromptRepository extends JpaRepository<SystemPrompt, String> {

    Optional<SystemPrompt> findByTitle(String title);
    boolean existsByTitle(String title);

    List<SystemPrompt> findAllByIsDeletedFalse();
    Optional<SystemPrompt> findBySystemPromptIdAndIsDeletedFalse(String id);
    Optional<SystemPrompt> findByTitleAndIsDeletedFalse(String title);
    boolean existsByTitleAndIsDeletedFalse(String title);
}
