package com.demo.aireportstudio.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "knowledge")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Knowledge {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "knowledge_id", columnDefinition = "VARCHAR(36)")
    private String knowledgeId;

    @Column(nullable = false, length = 500, unique = true)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted = false;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
