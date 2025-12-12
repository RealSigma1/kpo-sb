package com.example.fileanalysisservice.service;

import com.example.fileanalysisservice.entity.PlagiarismReport;
import com.example.fileanalysisservice.repository.PlagiarismReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.List;

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

        String text = new String(fileBytes, StandardCharsets.UTF_8);
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

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    private String generateWordCloudUrl(String text) {
        String cleanedText = text
                .toLowerCase()
                .replaceAll("[^a-zA-Zа-яА-Я0-9]+", " ")
                .trim()
                .replaceAll("\\s+", " ");

        if (cleanedText.isEmpty()) {
            cleanedText = "empty";
        }

        int maxChars = 1500;
        if (cleanedText.length() > maxChars) {
            cleanedText = cleanedText.substring(0, maxChars);
        }

        String encodedText = URLEncoder.encode(cleanedText, StandardCharsets.UTF_8);

        return "https://quickchart.io/wordcloud?text=" + encodedText
                + "&format=png"
                + "&width=800"
                + "&height=400"
                + "&fontFamily=Arial"
                + "&fontScale=60"
                + "&scale=linear"
                + "&removeStopwords=false"
                + "&backgroundColor=white";
    }
}
