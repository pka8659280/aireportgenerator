package com.demo.aireportstudio.repository;

import com.demo.aireportstudio.model.Knowledge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KnowledgeRepository extends JpaRepository<Knowledge, String> {

    Optional<Knowledge> findByTitle(String title);
    boolean existsByTitle(String title);

    List<Knowledge> findAllByIsDeletedFalse();
    Optional<Knowledge> findByKnowledgeIdAndIsDeletedFalse(String id);
    Optional<Knowledge> findByTitleAndIsDeletedFalse(String title);
    boolean existsByTitleAndIsDeletedFalse(String title);
}
