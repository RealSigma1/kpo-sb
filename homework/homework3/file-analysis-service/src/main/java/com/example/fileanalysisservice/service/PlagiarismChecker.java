package com.example.fileanalysisservice.service;

import com.example.fileanalysisservice.entity.PlagiarismReport;
import com.example.fileanalysisservice.repository.PlagiarismReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PlagiarismChecker {

    private final PlagiarismReportRepository reportRepository;

    @Autowired
    public PlagiarismChecker(PlagiarismReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Transactional
    public void analyze(Long workId, String filePath) throws IOException {
        Path path = Path.of(filePath);
        byte[] fileBytes = Files.readAllBytes(path);
        String text = new String(fileBytes);

        String fileHash = calculateSHA256(fileBytes);

        List<PlagiarismReport> existingReports = reportRepository.findByFileHash(fileHash);

        boolean isPlagiarism = existingReports.stream()
                .anyMatch(report -> !report.getWorkId().equals(workId));

        String wordCloudUrl = generateWordCloudUrl(text);

        PlagiarismReport report = new PlagiarismReport();
        report.setWorkId(workId);
        report.setFileHash(fileHash);
        report.setPlagiarismDetected(isPlagiarism);
        report.setWordCloudUrl(wordCloudUrl);
        report.setCreatedAt(LocalDateTime.now());

        reportRepository.save(report);
    }

    public List<PlagiarismReport> getReportsByWorkId(Long workId) {
        return reportRepository.findByWorkId(workId);
    }

    private String calculateSHA256(byte[] data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data);
            StringBuilder hex = new StringBuilder();
            for (byte b : hash) {
                hex.append(String.format("%02x", b));
            }
            return hex.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not supported", e);
        }
    }

    private String generateWordCloudUrl(String text) {
        String cleanedText = text.replaceAll("[^a-zA-Zа-яА-Я0-9\\s]", "")
                .toLowerCase()
                .trim();

        String[] words = cleanedText.split("\\s+");
        int maxWords = Math.min(words.length, 500);

        StringBuilder textForCloud = new StringBuilder();
        for (int i = 0; i < maxWords; i++) {
            if (!words[i].isEmpty()) {
                textForCloud.append(words[i]).append(" ");
            }
        }

        String encodedText = textForCloud.toString().trim().replace(" ", "%20");
        return "https://quickchart.io/wordcloud?text=" + encodedText +
                "&fontFamily=Arial&fontSize=15&scale=0.75&width=800&height=400&removeStopwords=true";
    }
}