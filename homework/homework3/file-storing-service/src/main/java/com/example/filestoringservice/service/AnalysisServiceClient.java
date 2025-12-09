package com.example.filestoringservice.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class AnalysisServiceClient {

    private final WebClient webClient;

    public AnalysisServiceClient(@Value("${analysis.service.url}") String analysisServiceUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(analysisServiceUrl)
                .build();
    }

    public void analyze(Long workId, String fileHash, String filePath) {
        webClient.post()
                .uri("/analyze")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(new AnalysisRequest(workId, fileHash, filePath))
                .retrieve()
                .toBodilessEntity()
                .block();
    }

    private static class AnalysisRequest {
        private Long workId;
        private String fileHash;
        private String filePath;

        public AnalysisRequest(Long workId, String fileHash, String filePath) {
            this.workId = workId;
            this.fileHash = fileHash;
            this.filePath = filePath;
        }

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
}