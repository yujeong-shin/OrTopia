package com.example.ordering_lecture.recommendation.service;

import com.example.ordering_lecture.review.entity.Review;
import com.example.ordering_lecture.review.repository.ReviewRepository;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class CsvExportService {
    private final ReviewRepository reviewRepository;
    public CsvExportService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }
    public String exportDataToCsv() {
        String filePath = "./item/src/main/java/com/example/ordering_lecture/recommendation/Recommendation.csv";
        List<Review> reviews = reviewRepository.findAll(); // 데이터를 가져오는 로직을 구현하세요.
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT.withHeader("id","buyerID", "itemID", "score"))
        ) {
            for (Review review : reviews) {
                csvPrinter.printRecord(review.getId(), review.getBuyerId(), review.getItem().getId(), review.getScore());
            }
            csvPrinter.flush();
            return "ok";
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }
}
