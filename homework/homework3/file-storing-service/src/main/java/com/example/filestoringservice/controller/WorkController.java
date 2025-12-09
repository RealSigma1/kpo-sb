package com.example.filestoringservice.controller;

import com.example.filestoringservice.entity.WorkSubmission;
import com.example.filestoringservice.repository.WorkSubmissionRepository;
import com.example.filestoringservice.service.AnalysisServiceClient;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/works")
public class WorkController {

    private final WorkSubmissionRepository repository;
    private final AnalysisServiceClient analysisServiceClient;

    @Autowired
    public WorkController(WorkSubmissionRepository repository,
                          AnalysisServiceClient analysisServiceClient) {
        this.repository = repository;
        this.analysisServiceClient = analysisServiceClient;
    }

    @PostMapping
    public ResponseEntity<WorkSubmission> submitWork(
            @RequestParam String studentName,
            @RequestParam String assignmentName,
            @RequestParam MultipartFile file) throws IOException {

        String filename = UUID.randomUUID() + "_" + file.getOriginalFilename();
        Path uploadDir = Paths.get("/uploads");
        Files.createDirectories(uploadDir);
        Path filePath = uploadDir.resolve(filename);

        file.transferTo(filePath);

        String fileHash = DigestUtils.sha256Hex(file.getBytes());

        WorkSubmission submission = new WorkSubmission();
        submission.setStudentName(studentName);
        submission.setAssignmentName(assignmentName);
        submission.setSubmittedAt(LocalDateTime.now());
        submission.setFilePath(filePath.toString());
        submission.setFileHash(fileHash);

        WorkSubmission savedSubmission = repository.save(submission);

        analysisServiceClient.analyze(savedSubmission.getId(), fileHash, filePath.toString());

        return ResponseEntity.status(HttpStatus.CREATED).body(savedSubmission);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FileSystemResource> getWorkFile(@PathVariable Long id) {
        Optional<WorkSubmission> submission = repository.findById(id);

        if (submission.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        FileSystemResource fileResource = new FileSystemResource(submission.get().getFilePath());
        if (!fileResource.exists()) {
            return ResponseEntity.notFound().build();
        }

        String filename = submission.get().getFilePath().substring(submission.get().getFilePath().lastIndexOf("/") + 1);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(fileResource);
    }
}