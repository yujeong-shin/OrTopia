package com.example.ordering_lecture.recommend.service;

import com.example.ordering_lecture.common.ErrorCode;
import com.example.ordering_lecture.common.OrTopiaException;
import com.example.ordering_lecture.review.entity.Review;
import com.example.ordering_lecture.review.repository.ReviewRepository;
import com.google.gson.Gson;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.stereotype.Service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@Service
public class RecommendationService {
    private final byte RECOMMENDATION_COUNT = 3;
    private final ReviewRepository reviewRepository;
    public RecommendationService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // DB에서 필요한 데이터를 분석 가능한 CSV 파일로 추출
    public String exportDataToCsv() {
        // TODO : 파일 경로 추후 수정
        String filePath = "./recommendation/src/main/java/com/example/ordering_lecture/recommend/Recommendation.csv";
        List<Review> reviews = reviewRepository.findAll();
        try (
                BufferedWriter writer = Files.newBufferedWriter(Paths.get(filePath));
                CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.DEFAULT);
        ) {
            for (Review review : reviews) {
                csvPrinter.printRecord(review.getBuyerId(), review.getItem().getId(), review.getScore());
            }
            csvPrinter.flush();
            return "ok";
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }
    }

    // 입력한 사용자에 대해 유사도 기반 맞춤형 상품 추천
    public List<RecommendedItem> getRecommendations(Long id) {
        DataModel model = null;
        try {
            // TODO : 파일 경로 추후 수정
            // 저장된 CSV 파일 읽기
            model = new FileDataModel(new File("./recommendation/src/main/java/com/example/ordering_lecture/recommend/Recommendation.csv"));
        } catch (IOException e) {
            throw new OrTopiaException(ErrorCode.NOT_FOUND_FILE);
        }

        // 유저 기준 유사성 측정
        UserSimilarity similarity = null;
        try {
            similarity = new PearsonCorrelationSimilarity(model);
        } catch (TasteException e) {
            throw new OrTopiaException(ErrorCode.NOT_MAKE_SIMILARITY);
        }

        // 0.1보다 큰 유사성을 가진 모든 것을 사용.
        UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);

        // 유저 기준 추천 모델 생성
        UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

        // id번 유저에게 RECOMMENDATION_COUNT개 아이템 추천  => (id, 3)
        List<RecommendedItem> recommendations;
        try {
            recommendations = recommender.recommend(id, RECOMMENDATION_COUNT);
        } catch (TasteException e) {
            throw new OrTopiaException(ErrorCode.NOT_MAKE_TASTE);
        }
        System.out.println("----- RecommendedItem -----");
        for (RecommendedItem recommendation : recommendations) {
            System.out.println(recommendation);
        }

//        // JSON 객체로 변환하여 리턴
//        Gson gson = new Gson();
//        return gson.toJson(recommendations);

        return recommendations;
    }
}
