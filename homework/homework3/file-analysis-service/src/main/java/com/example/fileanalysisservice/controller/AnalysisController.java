package com.example.fileanalysisservice.controller;

import com.example.fileanalysisservice.service.PlagiarismChecker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping
public class AnalysisController {

    private final PlagiarismChecker plagiarismChecker;

    @Autowired
    public AnalysisController(PlagiarismChecker plagiarismChecker) {
        this.plagiarismChecker = plagiarismChecker;
    }

    @PostMapping("/analyze")
    public ResponseEntity<Void> analyzeWork(@RequestBody AnalysisRequest request) {
        try {
            plagiarismChecker.analyze(request.getWorkId(), request.getFileHash(), request.getFilePath());
            return ResponseEntity.accepted().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/works/{workId}/reports")
    public ResponseEntity<List<PlagiarismReportResponse>> getReports(@PathVariable Long workId) {
        List<PlagiarismReportResponse> reports = plagiarismChecker.getReportsByWorkId(workId).stream()
                .map(report -> new PlagiarismReportResponse(
                        report.getId(),
                        report.getWorkId(),
                        report.isPlagiarismDetected(),
                        report.getWordCloudUrl(),
                        report.getCreatedAt()
                ))
                .collect(Collectors.toList());

        return ResponseEntity.ok(reports);
    }

    private static class AnalysisRequest {
        private Long workId;
        private String fileHash;
        private String filePath;

        public Long getWorkId() {
            return workId;
        }

        public String getFileHash() {
            return fileHash;
        }

        public String getFilePath() {
            return filePath;
        }
    }

    private static class PlagiarismReportResponse {
        private Long id;
        private Long workId;
        private boolean plagiarismDetected;
        private String wordCloudUrl;
        private LocalDateTime createdAt;

        public PlagiarismReportResponse(Long id, Long workId, boolean plagiarismDetected,
                                        String wordCloudUrl, LocalDateTime createdAt) {
            this.id = id;
            this.workId = workId;
            this.plagiarismDetected = plagiarismDetected;
            this.wordCloudUrl = wordCloudUrl;
            this.createdAt = createdAt;
        }

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
    }
}