package com.example.filestoringservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class WorkSubmission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String studentName;
    private String assignmentName;
    private LocalDateTime submittedAt;
    private String filePath;
    private String fileHash;

    public WorkSubmission() {}

    public WorkSubmission(Long id, String studentName, String assignmentName, LocalDateTime submittedAt, String filePath, String fileHash) {
        this.id = id;
        this.studentName = studentName;
        this.assignmentName = assignmentName;
        this.submittedAt = submittedAt;
        this.filePath = filePath;
        this.fileHash = fileHash;
    }

    public Long getId() {
        return id;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getAssignmentName() {
        return assignmentName;
    }

    public LocalDateTime getSubmittedAt() {
        return submittedAt;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getFileHash() {
        return fileHash;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public void setAssignmentName(String assignmentName) {
        this.assignmentName = assignmentName;
    }

    public void setSubmittedAt(LocalDateTime submittedAt) {
        this.submittedAt = submittedAt;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setFileHash(String fileHash) {
        this.fileHash = fileHash;
    }
}