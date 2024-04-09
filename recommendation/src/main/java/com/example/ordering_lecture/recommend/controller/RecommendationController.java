package com.example.ordering_lecture.recommend.controller;

import com.example.ordering_lecture.common.OrTopiaResponse;
import com.example.ordering_lecture.recommend.dto.RecommendationRedisData;
import com.example.ordering_lecture.recommend.service.RecommendationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class RecommendationController {
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @GetMapping("/member/{id}/recommendations")
    public ResponseEntity<OrTopiaResponse> recommendations(@PathVariable Long id){
        List<RecommendationRedisData> recommendationRedisDatas = recommendationService.getRecommendations(id);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("create success", recommendationRedisDatas);
        return new ResponseEntity<>(orTopiaResponse, HttpStatus.CREATED);
    }

    @GetMapping("/check/{id}/recommendations")
    public ResponseEntity<OrTopiaResponse> readRecentItems(@PathVariable Long id){
        List<RecommendationRedisData> recommendationRedisDatas = recommendationService.readRecommendationItems(id);
        OrTopiaResponse orTopiaResponse = new OrTopiaResponse("read success",recommendationRedisDatas);
        return new ResponseEntity<>(orTopiaResponse,HttpStatus.OK);
    }
}
