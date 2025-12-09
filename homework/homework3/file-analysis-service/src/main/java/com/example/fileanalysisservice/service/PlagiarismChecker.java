package com.example.fileanalysisservice.service;

import com.example.fileanalysisservice.entity.PlagiarismReport;
import com.example.fileanalysisservice.repository.PlagiarismReportRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class PlagiarismChecker {

    private final PlagiarismReportRepository reportRepository;

    @Autowired
    public PlagiarismChecker(PlagiarismReportRepository reportRepository) {
        this.reportRepository = reportRepository;
    }

    @Transactional
    public void analyze(Long workId, String fileHash, String filePath) throws IOException {
        boolean isPlagiarism = false;

        String text = extractTextFromFile(filePath);
        String wordCloudUrl = generateWordCloudUrl(text);

        PlagiarismReport report = new PlagiarismReport();
        report.setWorkId(workId);
        report.setPlagiarismDetected(isPlagiarism);
        report.setWordCloudUrl(wordCloudUrl);
        report.setCreatedAt(LocalDateTime.now());

        reportRepository.save(report);
    }

    public List<PlagiarismReport> getReportsByWorkId(Long workId) {
        return reportRepository.findByWorkId(workId);
    }

    private String extractTextFromFile(String filePath) throws IOException {
        Path path = Path.of(filePath);
        String content = new String(Files.readAllBytes(path));

        return content;
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