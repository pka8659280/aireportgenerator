package com.demo.aireportstudio.repository;

import com.demo.aireportstudio.model.UserPrompt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserPromptRepository extends JpaRepository<UserPrompt, String> {

    Optional<UserPrompt> findByTitle(String title);
    boolean existsByTitle(String title);

    List<UserPrompt> findAllByIsDeletedFalse();
    Optional<UserPrompt> findByUserPromptIdAndIsDeletedFalse(String id);
    Optional<UserPrompt> findByTitleAndIsDeletedFalse(String title);
    boolean existsByTitleAndIsDeletedFalse(String title);
}
