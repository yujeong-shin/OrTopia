package com.example.ordering_lecture.recommend.service;

import com.example.ordering_lecture.common.ErrorCode;
import com.example.ordering_lecture.common.OrTopiaException;
import com.example.ordering_lecture.review.repository.ReviewRepository;
import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.impl.model.jdbc.MySQLJDBCDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.springframework.stereotype.Service;
import org.mariadb.jdbc.MariaDbDataSource;

import java.sql.SQLException;
import java.util.List;

@Service
public class RecommendationService {
    private final byte RECOMMENDATION_COUNT = 3;
    private final ReviewRepository reviewRepository;
    public RecommendationService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    // 입력한 사용자에 대해 유사도 기반 맞춤형 상품 추천
    public List<RecommendedItem> getRecommendations(Long id) {
        MariaDbDataSource dataSource = new MariaDbDataSource();
        try {
            dataSource.setUrl("jdbc:mariadb://localhost:3306/recommendation");
        } catch (SQLException e) {
            throw new OrTopiaException(ErrorCode.NOT_SET_URL);
        }
        try {
            dataSource.setUser("root");
        } catch (SQLException e) {
            throw new OrTopiaException(ErrorCode.NOT_SET_USER);
        }
        try {
            dataSource.setPassword("1234");
        } catch (SQLException e) {
            throw new OrTopiaException(ErrorCode.NOT_SET_PASSWORD);
        }

        DataModel model = new MySQLJDBCDataModel(
                dataSource, // 데이터 소스
                "review", // 사용할 테이블 이름
                "buyer_id", // 사용자 ID 컬럼 이름
                "item_id", // 아이템 ID 컬럼 이름
                "score", // 평점 컬럼 이름
                null // 타임스탬프 컬럼 이름, 필요 없으면 null
        );

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

        // id번 유저에게 3개 아이템 추천
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
