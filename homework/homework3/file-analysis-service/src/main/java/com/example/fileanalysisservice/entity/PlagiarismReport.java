package com.example.fileanalysisservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class PlagiarismReport {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long workId;
    private boolean plagiarismDetected;
    private String wordCloudUrl;
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public Long getWorkId() {
        return workId;
    }

    public boolean isPlagiarismDetected() {
        return plagiarismDetected;
    }

    public String getWordCloudUrl() {
        return wordCloudUrl;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setWorkId(Long workId) {
        this.workId = workId;
    }

    public void setPlagiarismDetected(boolean plagiarismDetected) {
        this.plagiarismDetected = plagiarismDetected;
    }

    public void setWordCloudUrl(String wordCloudUrl) {
        this.wordCloudUrl = wordCloudUrl;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}