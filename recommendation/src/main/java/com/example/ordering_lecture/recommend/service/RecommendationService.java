package com.example.ordering_lecture.recommend.service;

import com.example.ordering_lecture.common.ErrorCode;
import com.example.ordering_lecture.common.OrTopiaException;
import com.example.ordering_lecture.recommend.dto.RecommendationRedisData;
import com.example.ordering_lecture.redis.RedisService;
import com.example.ordering_lecture.token.JwtTokenProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import java.util.ArrayList;
import java.util.List;

@Service
public class RecommendationService {

    private final JwtTokenProvider jwtTokenProvider;
    private final byte RECOMMENDATION_COUNT = 3;
    private final RedisService redisService;
    public RecommendationService(JwtTokenProvider jwtTokenProvider, RedisService redisService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.redisService = redisService;
    }

    // 입력한 사용자에 대해 유사도 기반 맞춤형 상품 추천
    public List<RecommendationRedisData> getRecommendations(Long id ,String email,String role) {
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

        for (RecommendedItem recommendation : recommendations) {
            System.out.println(recommendation.getItemID());
        }

        List<RecommendationRedisData> recommendationRedisDatas = new ArrayList<>();
        for (RecommendedItem recommendation : recommendations) {
            // Feign : item 서버에서 해당 아이템 이미지 경로 얻어오기
            String imagePath = "test";
            // [itemId, itemImagePath] 형식으로 저장
            RecommendationRedisData recommendationRedisData = new RecommendationRedisData(recommendation.getItemID(), imagePath);
            recommendationRedisDatas.add(recommendationRedisData);
        }

        // redis에 저장
        redisService.setValues(id, recommendationRedisDatas);
        // token발행
        jwtTokenProvider.createRecommandToken(email,role);
        return recommendationRedisDatas;
    }

    public List<RecommendationRedisData> readRecommendationItems(Long id) {
        List<String> list = redisService.getValues(id);
        List<RecommendationRedisData> recommendationRedisDatas = new ArrayList<>();
        ObjectMapper objectMapper = new ObjectMapper();
        for(String str: list){
            RecommendationRedisData  recommendationRedisData = null;
            try {
                recommendationRedisData = objectMapper.readValue(str, RecommendationRedisData.class);
            } catch (JsonProcessingException e) {
                throw new OrTopiaException(ErrorCode.JSON_PARSE_ERROR);
            }
            recommendationRedisDatas.add(recommendationRedisData);
        }
        return recommendationRedisDatas;
    }
}
